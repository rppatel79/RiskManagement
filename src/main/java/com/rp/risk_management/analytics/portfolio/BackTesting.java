package com.rp.risk_management.analytics.portfolio;

import com.rp.risk_management.model.Portfolio;
import com.rp.risk_management.util.FileHelper;
import com.rp.risk_management.util.model.PortfolioUtil;

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
        if (portfolio.getAssets().size() != 1 && portfolio.getOptions() != null)
            throw new IllegalArgumentException("Only support for single stock within a portfolio");

        ModelBuilding mb = new ModelBuilding( portfolio, confidence, timePeriod );

        double[] estimations = mb.computeForBackTesting( numberOfDaysToTest );
        return compareEstimationsWithActualLosses_OneStock( estimations );
    }

    private BackTestingResults backTestHistoricalSimulation()
    {
        HistoricalSimulation hs = new HistoricalSimulation( portfolio, confidence, timePeriod );

        double[] estimations = hs.estimateVaRForBackTestingOneStock( numberOfDaysToTest );
        return compareEstimationsWithActualLosses_OneStock( estimations );
    }

    private BackTestingResults backTestMonteCarloSimulation(double[][] stockValues)
    {
        MonteCarloSimulation mc = new MonteCarloSimulation( portfolio, confidence, 1 );
        double[] estimations = mc.estimateVaRForBacktesting_OneStock( numberOfDaysToTest, stockValues );
        return compareEstimationsWithActualLosses_OneStock( estimations);

    }

    /**
     * Compares estimation of VaR with actual losses and updates the result.
     * @param estimations VaR estimations to test
     */
    private BackTestingResults compareEstimationsWithActualLosses_OneStock(double[] estimations)
    {
        if (portfolio.getAssets().size() != 1 && portfolio.getOptions() != null)
            throw new IllegalArgumentException("Only support for single stock within a portfolio");

        double initialValue = PortfolioUtil.getAssetInvestment(portfolio).get(0);
        double[] allReturns = VarUtils.computeDailyReturns(FileHelper.getClosingPrices( PortfolioUtil.getStockQuotes(portfolio).get(0) ));
        int dayToStart = allReturns.length - 1 - numberOfDaysToTest;

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
