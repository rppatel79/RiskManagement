package com.rp.risk_management.analytics.portfolio;

import com.rp.risk_management.marketdata.model.Stock;
import com.rp.risk_management.model.Asset;
import com.rp.risk_management.model.Portfolio;
import com.rp.risk_management.util.ResourceHelper;
import com.rp.risk_management.util.date.SimpleDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BackTesting_UnitTest
{
    /**
     * @return a pre-configured portfolio with one stock.
     */
    private static Portfolio getPortfolioWithOneStock() throws Exception {
        File prevStockData = ResourceHelper.getInstance().getResource("MSFT_Apr2012_Apr2013.csv");
        List<File> stockPriceDataFiles = new ArrayList<File>();
        stockPriceDataFiles.add( prevStockData );
        List<Double> portfolioValues = new ArrayList<Double>();
        double msftInvestment = 1000000.0;
        portfolioValues.add( msftInvestment );
        Asset msft = new Asset( new Stock("MSFT"), msftInvestment,
                new SimpleDate(2012,4,2),new SimpleDate(2013,4,1));
        Portfolio portfolio = new Portfolio(Collections.singletonList(msft),null);

        return portfolio;
    }

    @Before
    public void setUp() throws Exception
    {
    }

    @Test
    public void testRunModelBuildingTestForOneStock()throws Exception
    {
        SimulationSetup simulationSetup = new SimulationSetup(getPortfolioWithOneStock(),VarUtils.MB,99,1);
        BackTesting backTesting = new BackTesting(simulationSetup);
        BackTesting.BackTestingResults results = backTesting.backTestPortfolio();
        Assert.assertEquals(1, results.acceptableExceptions);
        Assert.assertEquals(3, results.numberOfExceptions);
    }

    @Test
    public void testRunHistoricalSimulationTestForOneStock() throws Exception {
        SimulationSetup simulationSetup = new SimulationSetup(getPortfolioWithOneStock(), VarUtils.HS, 99, 1);
        BackTesting backTesting = new BackTesting(simulationSetup);
        BackTesting.BackTestingResults results=backTesting.backTestPortfolio();
        Assert.assertEquals(1, results.acceptableExceptions);
        Assert.assertEquals(0, results.numberOfExceptions);
    }

    //This test case is not consistently passing.  Need to fix it...
//    @Test
//    public void testRunMonteCarloTestForOneStock() {
//        //for ( int i = 0 ; i < 2 ; i++)
//        {
//            SimulationSetup simulationSetup = new SimulationSetup(getPortfolioWithOneStock(), VarUtils.MC, 99, 1);
//            BackTesting backTesting = new BackTesting(simulationSetup);
//            BackTesting.BackTestingResults results = backTesting.backTestPortfolio();
//            Assert.assertEquals(1, results.acceptableExceptions);
//            Assert.assertEquals(3, results.numberOfExceptions);
//        }
//    }
}
