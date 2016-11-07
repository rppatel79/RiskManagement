package com.rp.risk_management.analytics.portfolio;

import com.rp.risk_management.model.Portfolio;
import com.rp.risk_management.util.model.PortfolioUtil;

import java.util.Arrays;
import java.util.Random;

/**
 * Implements the stress testing technique to see what affect a large decline in stock prices has on
 * the value of the portfolio_.
 */
public class StressTester
{
    /** The factor by which stock values must decline in the event of a crash. */
    private final double crashFactor_ = 0.50;
    /** The portfolio_ to stress test. */
    private final Portfolio portfolio_;
    /** The number of days to conduct the stress test over. */
    private final int totalDays_;
    /** The day at which the crash should happen. */
    private final int crashDay_;
    /** Random number generator used to simulate changes in portfolio_ values from day to day. */
    private final Random rng_ = new Random();

    /**
     * Initialise a stress tester using a portfolio_.
     */
    public StressTester( Portfolio pf, int totalDays, int crashDay )
    {
        this.portfolio_ = pf;
        this.totalDays_ = totalDays;
        this.crashDay_ = crashDay;
    }

    /**
     * Run the stress test by simulating the portfolio values before and after the crash and
     * building the result from that data.
     */
    public StressTesterResults run()
    {
        return run(generateRandomDailyChange(totalDays_));
    }

    StressTesterResults run(double[] dailyUpOrDownArray)
    {
        return simulateCrashAndRecordLosses(dailyUpOrDownArray);
    }

    private double[] generateRandomDailyChange(int numDays)
    {
        double[] ret = new double[numDays];

        for (int i = 0 ; i < ret.length ; i++) {
            int n = rng_.nextInt(5);
            double upOrDown = ((double) n) / 100;
            if (rng_.nextInt(2) == 0) {
                upOrDown = upOrDown * -1;
            }
            ret[i]=upOrDown;
        }

        return ret;
    }

    /**
     * Simulates stock price movement on either side of the crash as a function of the previous
     * stock price multiplied by some random return.
     * On the day of the crash, the portfolio_ value declines by the crash factor defined earlier.
     */
    private StressTesterResults simulateCrashAndRecordLosses(double[] dailyUpOrDown)
    {
        double initialValue = VarUtils.sumOf( PortfolioUtil.getAssetInvestment(portfolio_) );
        double[] simulatedValues = new double[totalDays_];
        double[] losses = new double[totalDays_];
        simulatedValues[0] = initialValue;
        losses[0] = 0.0;
        for(int day = 1; day < totalDays_; day++ )
        {
            // value today = value yday + (value yday * some random number)
            if( day == crashDay_ - 1 )
            {
                double valueAtCrash = simulatedValues[day - 1] * crashFactor_;
                simulatedValues[day] = valueAtCrash;

            }
            else
            {
                double upOrDown = dailyUpOrDown[day];
                double valueOnDay = simulatedValues[day - 1]
                                    + ( simulatedValues[day - 1] * upOrDown );
                simulatedValues[day] = VarUtils.roundTwoDP( valueOnDay );
            }

            losses[day] = VarUtils.roundTwoDP( initialValue - simulatedValues[day] );
        }

        return new StressTesterResults(simulatedValues, losses);
    }

    public static class StressTesterResults
    {
        private final double[] simulatedValues;
        private final double[] losses;

        private StressTesterResults(double[] simulatedValues, double[] losses) {
            this.simulatedValues = simulatedValues;
            this.losses = losses;
        }

        public double getMinValue()
        {
            return simulatedValues[0];
        }
        public double getMaxValue()
        {
            return simulatedValues[simulatedValues.length - 1];
        }

        public double getMinLoss()
        {
            return losses[0];
        }
        public double getMaxLoss()
        {
            return losses[losses.length-1];
        }

        public double[] getSimulatedValues() {
            return simulatedValues;
        }

        public double[] getLosses() {
            return losses;
        }
    }

}
