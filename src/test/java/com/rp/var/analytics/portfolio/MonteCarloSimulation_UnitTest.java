package com.rp.var.analytics.portfolio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.rp.var.analytics.simulation.MonteCarlo;
import com.rp.var.model.Asset;
import com.rp.var.model.Portfolio;
import org.junit.Test;

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
        Portfolio portfolio = new Portfolio(stockPortfolio,null);

        List<MonteCarlo.SimulationResults> simulation = new ArrayList<>(MonteCarloSimulation.DEFAULT_TIME_PERIOD);

        for (int i =0 ; i < MonteCarloSimulation.DEFAULT_NUMBER_OF_SIMULATIONS ; i++)
        {
            int portfolioSize = portfolio.getInvestments().size();
            double[] minStockReturn = new double[portfolioSize];
            double[] finalStockReturn = new double[portfolioSize];
            for (int stockId = 0 ; stockId < portfolioSize ; stockId++) {
                minStockReturn[stockId] = 0.01;
                finalStockReturn[stockId] = 0.2;
            }
            MonteCarlo.SimulationResults simulationResults = new MonteCarlo.SimulationResults(minStockReturn,finalStockReturn);
            simulation.add(simulationResults);
        }

        MonteCarloSimulation sim = new MonteCarloSimulation(portfolio,99, 10);
        MonteCarloSimulation.MonteCarloResults results =sim.computeValueAtRisk(simulation);//TODO
        assertTrue( results.maximumVaR >= results.finalVaR);
        assertEquals("final var",3000-(-8.8612843164656),results.finalVaR,0.001);
        assertEquals("max var",3000-(-8.8612843164656),results.maximumVaR,0.001);
    }

}
