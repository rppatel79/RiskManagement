package com.rp.var.analytics.portfolio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rp.var.util.FileHelper;
import org.junit.Before;
import org.junit.Test;

import com.rp.var.analytics.portfolio.MonteCarloSimulation;

public class MonteCarloSimulation_UnitTest
{
    public void setUp() throws Exception
    {
    }

    @Test
    public void shouldReturnCorrectVARForTwoStocks()
    {
        List<Double> values= new ArrayList<Double>();
        List<File>   stockFiles= new ArrayList<File>();

        stockFiles.clear();
        stockFiles.add( new File( "test-classes/APPLE.csv" ) );
        stockFiles.add( new File( "test-classes/MSFT_15082013_15112013.csv" ) );
        values.clear();
        values.add( 1000.0 );
        values.add( 2000.0 );

        List<MonteCarloSimulation.SimulationResults> simulation = new ArrayList<>(MonteCarloSimulation.DEFAULT_TIME_PERIOD);


        for (int i =0 ; i < MonteCarloSimulation.DEFAULT_NUMBER_OF_SIMULATIONS ; i++)
        {
            MonteCarloSimulation.SimulationResults simulationResults = new MonteCarloSimulation.SimulationResults();
            simulationResults.minStockReturn = new double[stockFiles.size()];
            simulationResults.finalStockReturn = new double[stockFiles.size()];
            for (int stockId = 0 ; stockId < stockFiles.size() ; stockId++) {
                simulationResults.minStockReturn[stockId] = 0.01;
                simulationResults.finalStockReturn[stockId] = 0.2;
            }
            simulation.add(simulationResults);
        }

        MonteCarloSimulation sim = new MonteCarloSimulation( values, stockFiles, 99, 10 );
        sim.computeValueAtRisk(simulation);
        double monteCarloFinalVar = sim.getMonteCarloFinalVar();
        double monteCarloMaximumVar = sim.getMonteCarloMaximumVar();
        assertTrue( monteCarloMaximumVar >= monteCarloFinalVar );
        assertEquals("final var",-8.86128,monteCarloFinalVar,0.001);
        assertEquals("max var",-0.44244,monteCarloMaximumVar,0.001);
    }

}
