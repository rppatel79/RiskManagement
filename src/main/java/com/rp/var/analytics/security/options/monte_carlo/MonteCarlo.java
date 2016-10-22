package com.rp.var.analytics.security.options.monte_carlo;

import com.rp.var.analytics.portfolio.MonteCarloSimulation;
import com.rp.var.analytics.security.options.OptionPricer;
import com.rp.var.analytics.util.PreventValueCalculator;
import com.rp.var.model.Option;
import org.apache.commons.math3.stat.StatUtils;

import java.util.Arrays;

public class MonteCarlo implements OptionPricer
{
    private final Option option_;
    private final int numberOfSimulations_;
    private final int timePeriod_;
    private final MonteCarloResults results_;

    public MonteCarlo(Option option)
    {
        this(option, MonteCarloSimulation.DEFAULT_NUMBER_OF_SIMULATIONS, MonteCarloSimulation.DEFAULT_TIME_PERIOD);
    }

    public MonteCarlo(Option option, int numberOfSimulations, int timePeriod)
    {
        option_ = option;
        numberOfSimulations_ = numberOfSimulations;
        timePeriod_ = timePeriod;
        results_ =priceOptionUsingMonteCarlo();
    }

    @Override
    public double getOptionPrice()
    {
        return results_.finalValueOfOption_;
    }

    public MonteCarloResults getMonteCarloResults() {
        return results_;
    }

    /**
     * Uses the Monte-Carlo option pricing model alongside the Monte Carlo simulation model to price
     * an option.
     *
     * @return final and minimum simulated option values for VaR computation.
     */
    private MonteCarloResults priceOptionUsingMonteCarlo()
    {
        double[] finalMinValues = new double[2];
        // generate large number of random possible price paths
        double[][] prices = com.rp.var.analytics.simulation.MonteCarlo.simulatePrices( option_.getInitialStockPrice(),
                option_.getDailyVolatility(),numberOfSimulations_, timePeriod_ );
        // calculate exercise value/payoff of option for each path
        // intrinsic value - Call option - Max[Sn-X, 0], Put option - Max[X-Sn,
        // 0]
        double[] exerciseValues = new double[numberOfSimulations_];
        double currentPrice = 0.0;
        double exerciseValue = 0.0;
        for( int i = 0 ; i < numberOfSimulations_ ; i++ )
        {
            currentPrice = prices[i][timePeriod_ - 1];
            // TODO DOES THIS WORK FOR AMERICAN AND EUROPEAN OPTIONS?!
            if( option_.getOptionType() == Option.OptionType.Call )
            {
                exerciseValue = Math
                        .max( ( currentPrice - option_.getStrike() ), 0 );
            }
            else
            {
                exerciseValue = Math
                        .max( ( option_.getStrike() - currentPrice ), 0 );
            }
            exerciseValues[i] = exerciseValue;
        }

        // take average of the payoffs
        double meanExerciseValue = StatUtils.mean( exerciseValues );

        int discountPeriod = option_.getTimeToMaturity() - timePeriod_;
        if( discountPeriod < 1 )
        {
            // for safety
            discountPeriod = option_.getTimeToMaturity();
        }
        double finalValueOfOption = PreventValueCalculator.getDiscountedValue( meanExerciseValue, option_.getInterest(),
                option_.getTimeToMaturity() - timePeriod_ );

        Arrays.sort( exerciseValues );

        double minExerciseValue = exerciseValues[0];

        double minValueOfOption = PreventValueCalculator.getDiscountedValue( minExerciseValue, option_.getInterest(),
                option_.getTimeToMaturity() - timePeriod_ );

        MonteCarloResults results = new MonteCarloResults();
        results.finalValueOfOption_= finalValueOfOption;
        results.minValueOfOption_= minValueOfOption;
        return results;
    }

}
