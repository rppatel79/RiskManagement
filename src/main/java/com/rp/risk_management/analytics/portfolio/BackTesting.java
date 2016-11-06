package com.rp.risk_management.analytics.portfolio;

import com.rp.risk_management.model.Portfolio;
import com.rp.risk_management.util.FileHelper;

import java.io.File;
import java.util.Arrays;
/**
 * Runs a backtesting routine on a user-defined setup.
 */
public class BackTesting
{
    /** Confidence level to getOptionPrice VaR for testing. */
    private int       confidence         = 99;
    /** Time period to getOptionPrice VaR for testing. */
    private int       timePeriod         = 1;
    /** Number of days to getOptionPrice VaR for testing. */
    private int       numberOfDaysToTest = 100;
    /** The portfolio to run the backtest on. */
    private Portfolio portfolio;
    /** Model to use to estimate VaR for backtesting. */
    private String    model;

    public BackTesting( SimulationSetup setup )
    {
        this.portfolio = setup.getPortfolio();
        this.confidence = setup.getConfidenceLevel();
        this.model = setup.getModel();
        this.timePeriod = setup.getTimeHorizon();
    }

    BackTestingResults backTestPortfolio()
    {
        BackTestingResults ret;
        switch( model )
        {
            case VarUtils.MB:
                ret=backTestModelBuilding();
                break;
            case VarUtils.HS:
                ret=backTestHistoricalSimulation();
                break;
            case VarUtils.MC:
                ret=backTestMonteCarloSimulation(null);//todo
                break;
            default:
                throw new IllegalArgumentException("Unable to find model ["+model+"]");
        }

        return ret;
    }

    private BackTestingResults backTestModelBuilding()
    {
        //output += "Backtesting Model Building:\n";
        ModelBuilding mb = new ModelBuilding( portfolio, confidence, timePeriod );

        double value = portfolio.getInvestments().get(0);
        double[] returns = VarUtils.computeDailyReturns(FileHelper.getClosingPrices( portfolio.getStockQuotes().get(0) ));
        int position = returns.length - 1 - numberOfDaysToTest;
        double[] estimations = mb.computeForBackTesting( returns, numberOfDaysToTest );
        return compareEstimationsWithActualLosses_OneStock( estimations, returns, position, value );
    }

    private BackTestingResults backTestHistoricalSimulation()
    {
        HistoricalSimulation hs = new HistoricalSimulation( portfolio, confidence, timePeriod );

        double[] estimations = hs.estimateVaRForBackTestingOneStock( numberOfDaysToTest );
        double[] allReturns = VarUtils.computeDailyReturns(FileHelper.getClosingPrices( portfolio.getStockQuotes()
                                                                    .get( 0 ) ));
        int position = allReturns.length - 1 - numberOfDaysToTest;
        return compareEstimationsWithActualLosses_OneStock( estimations, allReturns, position,
                                                     portfolio.getAssetsValue() );
    }

    private BackTestingResults backTestMonteCarloSimulation(double[][] stockValues)
    {
        MonteCarloSimulation mc = new MonteCarloSimulation( portfolio, confidence, 1 );
        double[] allReturns = VarUtils.computeDailyReturns(FileHelper.getClosingPrices( portfolio.getStockQuotes()
                                                                    .get( 0 ) ));
        double[] estimations = mc.estimateVaRForBacktesting_OneStock( numberOfDaysToTest, stockValues );
        int position = allReturns.length - 1 - numberOfDaysToTest;
        return compareEstimationsWithActualLosses_OneStock( estimations, allReturns, position,
                                                     portfolio.getAssetsValue() );

    }

    /**
     * Compares estimation of VaR with actual losses and updates the result.
     * @param estimations VaR estimations to test
     * @param allReturns complete series of returns of stock data
     * @param dayToStart day to start comparing VaR and actual losses from returns
     * @param initialValue of the asset
     */
    private BackTestingResults compareEstimationsWithActualLosses_OneStock( double[] estimations,
                                                              double[] allReturns, int dayToStart,
                                                              double initialValue )
    {
        int position = dayToStart;
        int numberOfExceptions = 0;
        double[] returnsToUse = Arrays.copyOfRange( allReturns, position, allReturns.length - 1 );
        double[] returnsOverVarHorizon = VarUtils.getReturnsOverVarHorizon( returnsToUse,
                                                                            timePeriod );
        // reduce days as number of returns over time horizon decreases as time horizon grows
        for( int day = 0 ; day < numberOfDaysToTest - (timePeriod - 1) ; day++ )
        {
            double actualLoss = initialValue
                                 - ( initialValue * Math.exp( returnsOverVarHorizon[day] ) );

            if( actualLoss > estimations[day] )
            {
                numberOfExceptions++;
            }
            position++;
        }

        int acceptableExceptions = (int) Math.floor( numberOfDaysToTest
                                                     * ( 1 - ( ( (double) confidence ) / 100 ) ) );

        BackTestingResults results = new BackTestingResults();
        results.acceptableExceptions = acceptableExceptions;
        results.numberOfExceptions = numberOfExceptions;

        return results;
    }

    static class BackTestingResults
    {
        int acceptableExceptions;
        int numberOfExceptions;
    }
}
