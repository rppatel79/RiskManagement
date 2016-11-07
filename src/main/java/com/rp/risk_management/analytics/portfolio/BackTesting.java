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

    /** Number of days to getOptionPrice VaR for testing. */
    private final int NUMBER_OF_DAYS_TO_TEST = 100;

    /** Confidence level to getOptionPrice VaR for testing. */
    private final int confidence_;
    /** Time period to getOptionPrice VaR for testing. */
    private final int timePeriod_;
    /** The portfolio to run the backtest on. */
    private Portfolio portfolio_;
    /** Model to use to estimate VaR for backtesting. */
    final private String model_;

    public BackTesting( SimulationSetup setup )
    {
        this.portfolio_ = setup.getPortfolio();
        this.confidence_ = setup.getConfidenceLevel();
        this.model_ = setup.getModel();
        this.timePeriod_ = setup.getTimeHorizon();
    }

    BackTestingResults backTestPortfolio()
    {
        BackTestingResults ret;
        switch(model_)
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
                throw new IllegalArgumentException("Unable to find model_ ["+ model_ +"]");
        }

        return ret;
    }

    private BackTestingResults backTestModelBuilding()
    {
        if (portfolio_.getAssets().size() != 1 && portfolio_.getOptions() != null)
            throw new IllegalArgumentException("Only support for single stock within a portfolio_");

        ModelBuilding mb = new ModelBuilding(portfolio_, confidence_, timePeriod_);

        double[] estimations = mb.computeForBackTesting(NUMBER_OF_DAYS_TO_TEST);
        return compareEstimationsWithActualLosses_OneStock( estimations );
    }

    private BackTestingResults backTestHistoricalSimulation()
    {
        HistoricalSimulation hs = new HistoricalSimulation(portfolio_, confidence_, timePeriod_);

        double[] estimations = hs.estimateVaRForBackTestingOneStock(NUMBER_OF_DAYS_TO_TEST);
        return compareEstimationsWithActualLosses_OneStock( estimations );
    }

    private BackTestingResults backTestMonteCarloSimulation(double[][] stockValues)
    {
        MonteCarloSimulation mc = new MonteCarloSimulation(portfolio_, confidence_, 1 );
        double[] estimations = mc.estimateVaRForBacktesting_OneStock(NUMBER_OF_DAYS_TO_TEST, stockValues );
        return compareEstimationsWithActualLosses_OneStock( estimations);

    }

    /**
     * Compares estimation of VaR with actual losses and updates the result.
     * @param estimations VaR estimations to test
     */
    private BackTestingResults compareEstimationsWithActualLosses_OneStock(double[] estimations)
    {
        if (portfolio_.getAssets().size() != 1 && portfolio_.getOptions() != null)
            throw new IllegalArgumentException("Only support for single stock within a portfolio_");

        double initialValue = PortfolioUtil.getAssetInvestment(portfolio_).get(0);
        double[] allReturns = VarUtils.computeDailyReturns(FileHelper.getClosingPrices( PortfolioUtil.getStockQuotes(portfolio_).get(0) ));
        int dayToStart = allReturns.length - 1 - NUMBER_OF_DAYS_TO_TEST;

        int position = dayToStart;
        int numberOfExceptions = 0;
        double[] returnsToUse = Arrays.copyOfRange( allReturns, position, allReturns.length - 1 );
        double[] returnsOverVarHorizon = VarUtils.getReturnsOverVarHorizon( returnsToUse,
                timePeriod_);
        // reduce days as number of returns over time horizon decreases as time horizon grows
        for(int day = 0; day < NUMBER_OF_DAYS_TO_TEST - (timePeriod_ - 1) ; day++ )
        {
            double actualLoss = initialValue
                                 - ( initialValue * Math.exp( returnsOverVarHorizon[day] ) );

            if( actualLoss > estimations[day] )
            {
                numberOfExceptions++;
            }
            position++;
        }

        int acceptableExceptions = (int) Math.floor( NUMBER_OF_DAYS_TO_TEST
                                                     * ( 1 - ( ( (double) confidence_) / 100 ) ) );

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
