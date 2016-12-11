package com.rp.risk_management.analytics.security.options.monte_carlo;

import com.rp.risk_management.analytics.portfolio.MonteCarloSimulation;
import com.rp.risk_management.analytics.security.options.BinomialTree;
import com.rp.risk_management.analytics.security.options.OptionPricer;
import com.rp.risk_management.util.PreventValueCalculator;
import com.rp.risk_management.model.Option;
import org.apache.commons.math3.stat.StatUtils;

import java.util.Arrays;

public class MonteCarloBinomialTree
{
    private final Option option_;
    private final int numberOfSimulations_;
    private final int timePeriod_;
    private final MonteCarloResults results_;

    public MonteCarloBinomialTree(Option option)
    {
        this(option, MonteCarloSimulation.DEFAULT_NUMBER_OF_SIMULATIONS, MonteCarloSimulation.DEFAULT_TIME_PERIOD);
    }

    public MonteCarloBinomialTree(Option option, int numberOfSimulations, int timePeriod)
    {
        option_ = option;
        numberOfSimulations_ = numberOfSimulations;
        timePeriod_ = timePeriod;
        results_ =priceOptionUsingBinomialTree(option,numberOfSimulations,timePeriod);
    }

    public MonteCarloResults getMonteCarloResults() {
        return results_;
    }

    /**
     * Uses the Binomial Tree option pricing model alongside the Monte Carlo simulation model to
     * price an option.
     *
     * @param option
     * @return final and minimum simulated option values for VaR computation.
     */
    private static MonteCarloResults priceOptionUsingBinomialTree(Option option, int numberOfSimulations, int timePeriod )
    {
        double[][] stockPrices = com.rp.risk_management.analytics.simulation.MonteCarlo.simulatePrices( option.getInitialStockPrice(),
                option.getDailyVolatility() ,numberOfSimulations, timePeriod);
        if (stockPrices.length != numberOfSimulations)
            throw new IllegalArgumentException("Simulated stock prices should be ["+numberOfSimulations+"] length");
        for (int i =0 ; i < stockPrices.length ; i++)
            if (stockPrices[i].length != timePeriod)
                throw new IllegalArgumentException("Simulated stock prices.  Each simulation must contain ["+timePeriod+"] values");

        double[][] optionPrices = new double[numberOfSimulations][timePeriod];
        double[] finalDayOptionPrices = new double[numberOfSimulations];
        double[] minOptionPrices = new double[numberOfSimulations];

        for( int simulation = 0 ; simulation < numberOfSimulations ; simulation++ )
        {
            for( int day = 0 ; day < timePeriod ; day++ )
            {
                OptionPricer bt = new BinomialTree( stockPrices[simulation][day], option.getStrike(),
                        option.getTimeToMaturity() - day,
                        option.getDailyVolatility(), option.getInterest(),
                        option.getOptionType(), option.getOptionStyle() );
                // discount option price to today
                optionPrices[simulation][day] = PreventValueCalculator.getDiscountedValue( bt.getOptionPrice(),
                        option.getInterest(),
                        option.getTimeToMaturity()
                                - day );
            }
            finalDayOptionPrices[simulation] = optionPrices[simulation][timePeriod];
            Arrays.sort( optionPrices[simulation] );
            minOptionPrices[simulation] = optionPrices[simulation][0];
        }

        // get average of option prices
        double meanFinalPrice = StatUtils.mean( finalDayOptionPrices );
        double meanMinPrice = StatUtils.mean( minOptionPrices );

        int discountPeriod = option.getTimeToMaturity() - timePeriod;
        if( discountPeriod < 1 )
        {
            // for safety
            discountPeriod = option.getTimeToMaturity();
        }

        double discountedFinalValue = PreventValueCalculator.getDiscountedValue( meanFinalPrice, option.getInterest(),
                discountPeriod );
        double discountedMinValue = PreventValueCalculator.getDiscountedValue( meanMinPrice, option.getInterest(),
                discountPeriod );

        MonteCarloResults monteCarloResults = new MonteCarloResults(discountedFinalValue,discountedMinValue);
        return monteCarloResults;
    }

    @Override
    public String toString() {
        return "MonteCarloBinomialTree{" +
                "option_=" + option_ +
                ", numberOfSimulations_=" + numberOfSimulations_ +
                ", timePeriod_=" + timePeriod_ +
                ", results_=" + results_ +
                '}';
    }
}
