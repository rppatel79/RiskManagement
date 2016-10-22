package com.rp.var.analytics.portfolio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rp.var.model.Asset;
import com.rp.var.model.Portfolio;
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
        List<Asset> stockPortfolio = new ArrayList<>();
        {
            Asset asset = new Asset();
            asset.setData(new File("test-classes/APPLE.csv" ));
            asset.setInvestment(1000.0);
            stockPortfolio.add(asset);
        }
        {
            Asset asset = new Asset();
            asset.setData(new File("test-classes/MSFT_15082013_15112013.csv"));
            asset.setInvestment(2000.0);
            stockPortfolio.add(asset);
        }
        Portfolio portfolio = new Portfolio();
        portfolio.setAssets(stockPortfolio);

        List<MonteCarloSimulation.SimulationResults> simulation = new ArrayList<>(MonteCarloSimulation.DEFAULT_TIME_PERIOD);

        for (int i =0 ; i < MonteCarloSimulation.DEFAULT_NUMBER_OF_SIMULATIONS ; i++)
        {
            MonteCarloSimulation.SimulationResults simulationResults = new MonteCarloSimulation.SimulationResults();

            int portfolioSize = portfolio.getInvestments().size();
            simulationResults.minStockReturn = new double[portfolioSize];
            simulationResults.finalStockReturn = new double[portfolioSize];
            for (int stockId = 0 ; stockId < portfolioSize ; stockId++) {
                simulationResults.minStockReturn[stockId] = 0.01;
                simulationResults.finalStockReturn[stockId] = 0.2;
            }
            simulation.add(simulationResults);
        }

        MonteCarloSimulation sim = new MonteCarloSimulation(portfolio,99, 10);
        sim.computeValueAtRisk(simulation);
        double monteCarloFinalVar = sim.getMonteCarloFinalVar();
        double monteCarloMaximumVar = sim.getMonteCarloMaximumVar();
        assertTrue( monteCarloMaximumVar >= monteCarloFinalVar );
        assertEquals("final var",-8.86128,monteCarloFinalVar,0.001);
        assertEquals("max var",-0.44244,monteCarloMaximumVar,0.001);
    }

}
