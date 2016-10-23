package com.rp.var.analytics.portfolio;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.rp.var.model.Asset;
import com.rp.var.model.Portfolio;
import com.rp.var.analytics.portfolio.StressTester;
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
    public void testStressReturnsForOneStock()
    {
        int totalDays=100;
        double[] upOrDownArray = new double[totalDays];
        Arrays.fill(upOrDownArray, -0.04);


        StressTester st = new StressTester( new Portfolio(Collections.singletonList(getTestAsset()),null), totalDays,totalDays/4 );
        st.run(upOrDownArray);

        double[] simulatedValues=st.getSimulatedValues();
        Arrays.sort(simulatedValues);

        double[] losses=st.getLosses();
        Arrays.sort(losses);

        Assert.assertEquals(9.14,simulatedValues[0],0.1);
        Assert.assertEquals(1000.0,simulatedValues[simulatedValues.length-1],0.1);

        Assert.assertEquals(0.0,losses[0],0.1);
        Assert.assertEquals(990.86,losses[simulatedValues.length-1],0.1);
    }
    
    @Test
    public void testStressReturnsForMultipleStocks()
    {
        List<Asset> assets = new ArrayList<>();
        assets.add(getTestAsset());
        assets.add(new Asset( new File( "test-classes/APPLE.csv" ), "APPL", 2000.0 ));
        Portfolio p = new Portfolio(assets,null);


        int totalDays=100;
        StressTester st = new StressTester( p,totalDays,totalDays/4 );

        double[] upOrDownArray = new double[totalDays];
        Arrays.fill(upOrDownArray, -0.04);

        st.run(upOrDownArray);

        double[] simulatedValues=st.getSimulatedValues();
        Arrays.sort(simulatedValues);

        double[] losses=st.getLosses();
        Arrays.sort(losses);

        Assert.assertEquals(27.47,simulatedValues[0],0.1);
        Assert.assertEquals(3000.0,simulatedValues[simulatedValues.length-1],0.1);

        Assert.assertEquals(0.0,losses[0],0.1);
        Assert.assertEquals(2972.53,losses[simulatedValues.length-1],0.1);
    }

    /**
     * @param simulatedValues
     */
    private void compareValues( StressTester st)
    {
        double[] simulatedValues = st.getSimulatedValues();
        double[] losses = st.getLosses();
        double minValue = simulatedValues[0];
        double maxValue = simulatedValues[simulatedValues.length - 1];
        System.out.println( "Min|Max portfolio value = " + minValue + " | " + maxValue );
        double minLoss = losses[0];
        double maxLoss = losses[losses.length - 1];
        System.out.println( "Min|Max portfolio loss  = " + minLoss + " | " + maxLoss );
        
    }

    private Asset getTestAsset()
    {
        File prevStockData = new File( "test-classes/MSFT_Apr2012_Apr2013.csv" );
        ArrayList<File> stockPriceDataFiles = new ArrayList<File>();
        stockPriceDataFiles.add( prevStockData );
        ArrayList<Double> portfolioValues = new ArrayList<Double>();
        double msftInvestment = 1000.0;
        portfolioValues.add( msftInvestment );
        Asset msft = new Asset( prevStockData, "MSFT", msftInvestment );
        return msft;
    }
}
