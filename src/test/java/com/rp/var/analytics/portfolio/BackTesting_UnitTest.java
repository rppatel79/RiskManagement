package com.rp.var.analytics.portfolio;

import com.rp.var.model.Asset;
import com.rp.var.model.Portfolio;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BackTesting_UnitTest
{
    /**
     * @return a pre-configured portfolio with one stock.
     */
    private static Portfolio getPortfolioWithOneStock()
    {
        File prevStockData = new File( "test-classes/MSFT_Apr2012_Apr2013.csv" );
        List<File> stockPriceDataFiles = new ArrayList<File>();
        stockPriceDataFiles.add( prevStockData );
        List<Double> portfolioValues = new ArrayList<Double>();
        double msftInvestment = 1000000.0;
        portfolioValues.add( msftInvestment );
        Asset msft = new Asset( prevStockData, "MSFT", msftInvestment );
        Portfolio portfolio = new Portfolio();
        portfolio.addAsset( msft );
        return portfolio;
    }

    @Before
    public void setUp() throws Exception
    {
    }

    @Test
    public void testRunModelBuildingTestForOneStock()
    {
        SimulationSetup simulationSetup = new SimulationSetup(getPortfolioWithOneStock(),VarUtils.MB,99,1);
        BackTesting backTesting = new BackTesting(simulationSetup);
        BackTesting.BackTestingResults results = backTesting.backTestPortfolio();
        Assert.assertEquals(1, results.acceptableExceptions);
        Assert.assertEquals(3, results.numberOfExceptions);
    }

    @Test
    public void testRunHistoricalSimulationTestForOneStock() {
        SimulationSetup simulationSetup = new SimulationSetup(getPortfolioWithOneStock(), VarUtils.HS, 99, 1);
        BackTesting backTesting = new BackTesting(simulationSetup);
        BackTesting.BackTestingResults results=backTesting.backTestPortfolio();
        Assert.assertEquals(1, results.acceptableExceptions);
        Assert.assertEquals(0, results.numberOfExceptions);
    }

    @Test
    public void testRunMonteCarloTestForOneStock() {
        //for ( int i = 0 ; i < 2 ; i++)
        {
            SimulationSetup simulationSetup = new SimulationSetup(getPortfolioWithOneStock(), VarUtils.MC, 99, 1);
            BackTesting backTesting = new BackTesting(simulationSetup);
            BackTesting.BackTestingResults results = backTesting.backTestPortfolio();
            Assert.assertEquals(1, results.acceptableExceptions);
            Assert.assertEquals(3, results.numberOfExceptions);
        }
    }
}
