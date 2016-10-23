package com.rp.risk_management.analytics.security.options.monte_carlo;

import com.rp.risk_management.analytics.portfolio.MonteCarloSimulation;
import com.rp.risk_management.analytics.security.options.BlackScholes;
import com.rp.risk_management.analytics.security.options.OptionPricer;
import com.rp.risk_management.analytics.util.PreventValueCalculator;
import com.rp.risk_management.model.Option;
import org.apache.commons.math3.stat.StatUtils;

import java.util.Arrays;
import java.util.Random;

public class MonteCarloBlackScholes implements OptionPricer
{
    /**
     * Random number generator using to generate Gaussian (Normal) and regularly distributed random
     * numbers for simulating prices.
     */
    private static final Random rng                 = new Random();

    private final Option option_;
    private final int numberOfSimulations_;
    private final int timePeriod_;
    private final MonteCarloResults results_;

    public MonteCarloBlackScholes(Option option)
    {
        this(option, null, MonteCarloSimulation.DEFAULT_NUMBER_OF_SIMULATIONS, MonteCarloSimulation.DEFAULT_TIME_PERIOD);
    }

    public MonteCarloBlackScholes(Option option, double[] simulationPerDay, int numberOfSimulations, int timePeriod)
    {
        option_ = option;
        numberOfSimulations_ = numberOfSimulations;
        timePeriod_ = timePeriod;

        if (simulationPerDay == null)
        {
            simulationPerDay=generateSimulation(numberOfSimulations,timePeriod);
        }

        results_ =priceOptionUsingBlackScholes(option_,simulationPerDay,numberOfSimulations_,timePeriod_);
    }

    private double[] generateSimulation(int numberOfSimulations, int timePeriod)
    {
        double[] simulationPerDay = new double[numberOfSimulations*timePeriod];
        for (int i = 0 ; i < simulationPerDay.length ; i++)
            simulationPerDay[i] = rng.nextGaussian();

        return simulationPerDay;
    }

    @Override
    public double getOptionPrice() {
        return results_.finalValueOfOption_;
    }

    public MonteCarloResults getMonteCarloResults() {
        return results_;
    }

    /**
     * Uses the Black-Scholes option pricing model alongside the Monte Carlo simulation model to
     * price an option.
     *
     * @param option
     * @return final and minimum simulated option values for VaR computation.
     */
    private static MonteCarloResults priceOptionUsingBlackScholes(Option option, double[] simulationPerDay, int numberOfSimulations, int timePeriod )
    {
        double interest = option.getInterest(), strike = option.getStrike(), dailyVolatility = option
                .getDailyVolatility();
        double initialStockPrice = option.getInitialStockPrice();
        int timeToMaturity = option.getTimeToMaturity();
        int numShares = 1, numOptions = 1;

        double[] finalDayPrices = new double[numberOfSimulations];
        double[] minPrices = new double[numberOfSimulations];
        double[][] optionPrices = new double[numberOfSimulations][timePeriod];

        for( int sim = 0 ; sim < numberOfSimulations ; sim++ )
        {
            double stockPrice = initialStockPrice;

            for( int day = 0 ; day < timePeriod ; day++ )
            {
                double optionPrice = 0;
                stockPrice = numShares
                        * ( stockPrice + ( dailyVolatility * stockPrice * simulationPerDay[(sim+1)*(day+1)] ) );
                BlackScholes bs = new BlackScholes(option.getOptionType(), stockPrice, strike, timeToMaturity
                        - day, interest,
                        dailyVolatility );
                optionPrice = numOptions
                        * bs.getOptionPrice( );
                // TODO check time to maturity decreased by 1 every run
                optionPrices[sim][day] = optionPrice;
            }

            double[] simPrices = Arrays.copyOf( optionPrices[sim],
                    optionPrices[sim].length );
            finalDayPrices[sim] = simPrices[timePeriod - 1];
            Arrays.sort( simPrices );
            minPrices[sim] = simPrices[0];
        }

        Arrays.sort( minPrices );
        int discountPeriod = option.getTimeToMaturity() - timePeriod;
        if( discountPeriod < 1 )
        {
            // for safety
            discountPeriod = option.getTimeToMaturity();
        }
        double meanFinalDayValue = StatUtils.mean( finalDayPrices );
        double meanMinValue = StatUtils.mean( minPrices );
        double discountedFinalValue = PreventValueCalculator.getDiscountedValue( meanFinalDayValue,
                option.getInitialStockPrice(),
                discountPeriod );
        double discountedMinValue = PreventValueCalculator.getDiscountedValue( meanMinValue, option.getInterest(),
                discountPeriod );
        // double VaR = initialOptionPrice - finalDayValue;
        // double maxVaR = initialOptionPrice - minPrices[0];

        MonteCarloResults monteCarloResults = new MonteCarloResults();
        monteCarloResults.finalValueOfOption_ = discountedFinalValue;
        monteCarloResults.minValueOfOption_ = discountedMinValue;
        return monteCarloResults;
    }

}
