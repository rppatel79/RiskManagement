package com.rp.risk_management.analytics.portfolio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import com.rp.risk_management.analytics.simulation.MonteCarlo;
import com.rp.risk_management.marketdata.model.Stock;
import com.rp.risk_management.model.Asset;
import com.rp.risk_management.model.Portfolio;
import com.rp.risk_management.util.date.SimpleDate;
import com.rp.risk_management.util.model.PortfolioUtil;
import org.junit.Test;

public class MonteCarloSimulation_UnitTest
{
    public void setUp() throws Exception
    {
    }

    @Test
    public void shouldReturnCorrectVARForTwoStocks()throws Exception
    {
        List<Asset> stockPortfolio = new ArrayList<>();
        {
            Asset asset = new Asset(new Stock("AAPL"),1000.0,
                new SimpleDate(2013,3,7),new SimpleDate(2013,12,3));//ResourceHelper.getInstance().getResource("APPLE.csv" ),
            stockPortfolio.add(asset);
        }
        {
            Asset asset = new Asset(new Stock("MSFT"),2000.0,
                    new SimpleDate(2013,8,13),new SimpleDate(2013,11,15));//ResourceHelper.getInstance().getResource("MSFT_15082013_15112013.csv")
            stockPortfolio.add(asset);
        }
        Portfolio portfolio = new Portfolio(stockPortfolio,null);

        List<MonteCarlo.SimulationResults> simulation = new ArrayList<>(MonteCarloSimulation.DEFAULT_TIME_PERIOD);

        for (int i =0 ; i < MonteCarloSimulation.DEFAULT_NUMBER_OF_SIMULATIONS ; i++)
        {
            int portfolioSize = PortfolioUtil.getAssetInvestment(portfolio).size();
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
        MonteCarloSimulation.MonteCarloResults results =sim.computeValueAtRiskForPortfolio(simulation);//TODO
        assertTrue( results.maximumVaR >= results.finalVaR);
        assertEquals("final risk_management",-(8.8612843164656),results.finalVaR,0.001);
        assertEquals("max risk_management",-(8.8612843164656),results.maximumVaR,0.001);
    }

}
