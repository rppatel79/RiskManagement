package com.rp.risk_management.analytics.portfolio;

import com.rp.risk_management.model.Portfolio;
import com.rp.risk_management.util.model.PortfolioUtil;

import java.util.Arrays;
import java.util.Random;

/**
 * Implements the stress testing technique to see what affect a large decline in stock prices has on
 * the value of the portfolio.
 */
public class StressTester
{
    /** The factor by which stock values must decline in the event of a crash. */
    private final double crashFactor = 0.50;
    /** The portfolio to stress test. */
    private final Portfolio portfolio;
    /** The number of days to conduct the stress test over. */
    private final int totalDays;
    /** The day at which the crash should happen. */
    private final int crashDay;
    /** Random number generator used to simulate changes in portfolio values from day to day. */
    private final Random rng = new Random();

    /** Array of portfolio values simulated before and after the crassh. */
    private double[]       simulatedValues;
    /** Array of losses occurring after portfolio values are simulated during the stress test. */
    private double[]       losses;

    /** The result of the stress test. */
    private String output;

    /**
     * Initialise a stress tester using a portfolio.
     * 
     * @param pf
     */
    public StressTester( Portfolio pf )
    {
        this(pf, 100, 100/4);
    }

    /**
     * Initialise a stress tester using a portfolio.
     */
    public StressTester( Portfolio pf, int totalDays, int crashDay )
    {
        this.portfolio = pf;
        this.totalDays = totalDays;
        this.crashDay = crashDay;
    }

    /**
     * Run the stress test by simulating the portfolio values before and after the crash and
     * building the result from that data.
     */
    public void run()
    {
        run(generateRandomDailyChange(totalDays));
    }

    void run(double[] dailyUpOrDownArray)
    {
        simulateCrashAndRecordLosses(dailyUpOrDownArray);
        buildOutput();
    }

    private double[] generateRandomDailyChange(int numDays)
    {
        double[] ret = new double[numDays];

        for (int i = 0 ; i < ret.length ; i++) {
            int n = rng.nextInt(5);
            double upOrDown = ((double) n) / 100;
            if (rng.nextInt(2) == 0) {
                upOrDown = upOrDown * -1;
            }
            ret[i]=upOrDown;
        }

        return ret;
    }

    /**
     * Builds up a results from the results of the stress test.
     */
    private void buildOutput()
    {
        output = "Original portfolio value: " + PortfolioUtil.getAssetsValue(portfolio) + "\n";
        Arrays.sort( simulatedValues );
        Arrays.sort( losses );
        double minValue = simulatedValues[0];
        double maxValue = simulatedValues[simulatedValues.length - 1];
        output += ( "Min | Max portfolio value = " + minValue + " | " + maxValue );
        output += "\n";
        double minLoss = losses[0];
        double maxLoss = losses[losses.length - 1];
        output += ( "Min | Max portfolio loss  = " + minLoss + " | " + maxLoss );
        output += ( "\n \n NOTE: Negative loss indicates a profit." );
    }

    /**
     * Simulates stock price movement on either side of the crash as a function of the previous
     * stock price multiplied by some random return.
     * On the day of the crash, the portfolio value declines by the crash factor defined earlier.
     */
    private void simulateCrashAndRecordLosses(double[] dailyUpOrDown)
    {
        double initialValue = VarUtils.sumOf( PortfolioUtil.getAssetInvestment(portfolio) );
        simulatedValues = new double[totalDays];
        losses = new double[totalDays];
        simulatedValues[0] = initialValue;
        losses[0] = 0.0;
        for( int day = 1 ; day < totalDays ; day++ )
        {
            // value today = value yday + (value yday * some random number)
            if( day == crashDay - 1 )
            {
                double valueAtCrash = simulatedValues[day - 1] * crashFactor;
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
    }

    /**
     * @return the losses experienced by the portfoliod during the stress test.
     */
    public double[] getLosses()
    {
        return losses;
    }

    /**
     * 
     * @return the maximum loss experienced by the portfolio during the stress test.
     */
    public double getMaxLoss()
    {
        return losses[losses.length - 1];
    }


    /**
     * @return the simulatedValues
     */
    public double[] getSimulatedValues()
    {
        return simulatedValues;
    }

    /**
     * @return the result of the stress test
     */
    public String getResult()
    {
        return output;
    }

}
