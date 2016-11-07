package com.rp.risk_management.analytics.portfolio;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.rp.risk_management.marketdata.model.Stock;
import com.rp.risk_management.model.Asset;
import com.rp.risk_management.model.Portfolio;
import com.rp.risk_management.util.ResourceHelper;
import com.rp.risk_management.util.date.SimpleDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StressTester_UnitTest
{
    @Before
    public void setUp() throws Exception
    {
    }

    @Test
    public void testStressReturnsForOneStock() throws Exception
    {
        int totalDays=100;
        double[] upOrDownArray = new double[totalDays];
        Arrays.fill(upOrDownArray, -0.04);


        StressTester st = new StressTester( new Portfolio(Collections.singletonList(getTestAsset()),null), totalDays,totalDays/4 );
        StressTester.StressTesterResults results =st.run(upOrDownArray);

        double[] simulatedValues=results.getSimulatedValues();
        Arrays.sort(simulatedValues);

        double[] losses=results.getLosses();
        Arrays.sort(losses);

        Assert.assertEquals(9.14,simulatedValues[0],0.1);
        Assert.assertEquals(1000.0,simulatedValues[simulatedValues.length-1],0.1);

        Assert.assertEquals(0.0,losses[0],0.1);
        Assert.assertEquals(990.86,losses[simulatedValues.length-1],0.1);
    }
    
    @Test
    public void testStressReturnsForMultipleStocks() throws Exception
    {
        List<Asset> assets = new ArrayList<>();
        assets.add(getTestAsset());
        assets.add(new Asset( new Stock("AAPL"), 2000.0 ,
                new SimpleDate(2013,3,7),new SimpleDate(2013,12,3)));//ResourceHelper.getInstance().getResource("APPLE.csv" )
        Portfolio p = new Portfolio(assets,null);


        int totalDays=100;
        StressTester st = new StressTester( p,totalDays,totalDays/4 );

        double[] upOrDownArray = new double[totalDays];
        Arrays.fill(upOrDownArray, -0.04);

        StressTester.StressTesterResults results =st.run(upOrDownArray);

        double[] simulatedValues=results .getSimulatedValues();
        Arrays.sort(simulatedValues);

        double[] losses=results.getLosses();
        Arrays.sort(losses);

        Assert.assertEquals(27.47,simulatedValues[0],0.1);
        Assert.assertEquals(3000.0,simulatedValues[simulatedValues.length-1],0.1);

        Assert.assertEquals(0.0,losses[0],0.1);
        Assert.assertEquals(2972.53,losses[simulatedValues.length-1],0.1);
    }

    private Asset getTestAsset() throws Exception
    {
        File prevStockData = ResourceHelper.getInstance().getResource("MSFT_Apr2012_Apr2013.csv" );
        ArrayList<File> stockPriceDataFiles = new ArrayList<>();
        stockPriceDataFiles.add( prevStockData );
        ArrayList<Double> portfolioValues = new ArrayList<>();
        double msftInvestment = 1000.0;
        portfolioValues.add( msftInvestment );
        return new Asset( new Stock("MSFT"), msftInvestment ,new SimpleDate(2012,4,2),new SimpleDate(2013,4,1));

    }
}
