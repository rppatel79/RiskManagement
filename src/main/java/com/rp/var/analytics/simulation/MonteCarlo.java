package com.rp.var.analytics.simulation;

import com.rp.var.analytics.portfolio.MonteCarloSimulation;

import java.util.Arrays;
import java.util.Random;

public class MonteCarlo {
    /**
     * Random number generator using to generate Gaussian (Normal) and regularly distributed random
     * numbers for simulating prices.
     */
    private static final Random rng                 = new Random();

    /**
     * Simulated normally distributed prices for a stock's initial price and its volatility.
     *
     * @param stockValue
     * @param volatility
     * @return 2D array containing prices over the time period for simulation.
     */
    public static double[][] simulatePrices( double stockValue, double volatility, int numberOfSimulations, int timePeriod )
    {
        double[][] stockValues = new double[numberOfSimulations][timePeriod];

        double possibleStockValue;

        for( int sim = 0 ; sim < numberOfSimulations ; sim++ )
        {
            for( int day = 0 ; day < timePeriod ; day++ )
            {
                if( day == 0 )
                {
                    possibleStockValue = stockValue
                                         + ( volatility * rng.nextGaussian() * stockValue );
                    stockValues[sim][day] = possibleStockValue;
                }
                else
                {
                    possibleStockValue = stockValues[sim][day - 1]
                                         + ( volatility * rng.nextGaussian() * stockValues[sim][day - 1] );
                    stockValues[sim][day] = possibleStockValue;
                }
            }
        }
        return stockValues;
    }

    /**
     * Simulates normally distributed returns for each asset over the specified time period using
     * the Monte Carlo simulation model.
     *
     * @return list of returns containing simulated returns for each asset in the portfolio.
     */
    public final static SimulationResults simulateReturns(int numberOfStocks, int timePeriod)
    {
        double[][] simulatedReturns = new double[numberOfStocks][timePeriod];
        double[] minReturns = new double[numberOfStocks];

        // simulate returns
        for( int stock = 0 ; stock < numberOfStocks ; stock++ )
        {
            for( int day = 0 ; day < timePeriod ; day++ )
            {
                simulatedReturns[stock][day] = rng.nextGaussian();
            }
        }

        double[] finalDayReturns = new double[numberOfStocks];

        // record minimum and final day returns for each stock
        for( int i = 0 ; i < numberOfStocks ; i++ )
        {
            finalDayReturns[i] = simulatedReturns[i][timePeriod - 1];

            double[] returnsForStock = Arrays.copyOf( simulatedReturns[i],
                    timePeriod );
            Arrays.sort( returnsForStock );
            minReturns[i] = returnsForStock[0];
        }

        return new SimulationResults(minReturns,finalDayReturns);
    }

    public static class SimulationResults
    {
        public final double[] minStockReturn;
        public final double[] finalStockReturn;

        public SimulationResults(double[] minStockReturn, double[] finalStockReturn) {
            this.minStockReturn = minStockReturn;
            this.finalStockReturn = finalStockReturn;
        }

    }
}
