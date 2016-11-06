package com.rp.risk_management.analytics.portfolio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.rp.risk_management.marketdata.model.Stock;
import com.rp.risk_management.model.Asset;
import com.rp.risk_management.model.Portfolio;
import com.rp.risk_management.util.ResourceHelper;
import com.rp.risk_management.util.date.SimpleDate;
import junit.framework.TestCase;
import org.junit.Assert;

public class HistoricalSimulation_UnitTest extends TestCase
{

    private HistoricalSimulation hs;
    private ArrayList<Double>    stockValues;
    private ArrayList<File>      stockDataFiles;
    private int                  confidence;
    private double               calculatedVaR;

    public void setUp() throws Exception
    {
        stockValues = new ArrayList<>();
        stockDataFiles = new ArrayList<>();
    }

    public void testShouldComputeCorrectVaRUsingHistoricalSimulation() throws Exception
    {
//        stockValues.clear();
//        stockValues.add( 1000.0 );
        confidence = 99;
        setupWithLargeStockDataFile( confidence );
        calculatedVaR = hs.computeValueAtRisk();
        Assert.assertEquals(65.0745,calculatedVaR,0.001);
    }

    public void testShouldComputeVaRUsingHistoricalSimulationForTwoStocks() throws Exception
    {
        List<Asset> allAssets = new ArrayList<>(3);
        {
            Asset asset1 = new Asset(new Stock("GOOG"), 1000.0, new SimpleDate(2004, 8, 19), new SimpleDate(2006, 8, 25));//GOOG_Tester
            Asset asset3 = new Asset(new Stock("MSFT"), 2000.0, new SimpleDate(2013, 8, 13), new SimpleDate(2013, 11, 15));//MSFT_15082013_15112013

            allAssets.add(asset1);
            allAssets.add(asset3);
        }

        /*
        stockValues.clear();
        stockValues.add( 1000.0 );
        stockValues.add( 2000.0 );
        stockDataFiles.clear();
        stockDataFiles.add( ResourceHelper.getInstance().getResource("GOOG_Tester.csv" ) );
        stockDataFiles.add( ResourceHelper.getInstance().getResource("MSFT_15082013_15112013.csv" ) );
        */
        confidence = 99;
        hs = new HistoricalSimulation( new Portfolio(allAssets,null), confidence );

        double var =hs.computeValueAtRisk();
        Assert.assertEquals(104.0,var,0.001);
    }

    public void testShouldComputeVaRUsingHistoricalSimulationForThreeStocks() throws Exception
    {
        List<Asset> allAssets = new ArrayList<>(3);
        {
            Asset asset1 = new Asset(new Stock("GOOG"), 1000.0, new SimpleDate(2004, 8, 19), new SimpleDate(2006, 8, 25));//GOOG_Tester
            Asset asset2 = new Asset(new Stock("GOOG"), 1500.0, new SimpleDate(2013, 9, 19), new SimpleDate(2013, 10, 18));//GOOG_190913_181013
            Asset asset3 = new Asset(new Stock("MSFT"), 2000.0, new SimpleDate(2013, 8, 13), new SimpleDate(2013, 11, 15));//MSFT_15082013_15112013

            allAssets.add(asset1);
            allAssets.add(asset2);
            allAssets.add(asset3);
        }
/*
        stockValues.clear();
        stockValues.add( 1000.0 );
        stockValues.add( 1500.0 );
        stockValues.add( 2000.0 );
        stockDataFiles.clear();
        stockDataFiles.add( ResourceHelper.getInstance().getResource( "GOOG_Tester.csv" ) );
        stockDataFiles.add( ResourceHelper.getInstance().getResource( "GOOG_190913_181013.csv" ) );
        stockDataFiles.add( ResourceHelper.getInstance().getResource( "MSFT_15082013_15112013.csv" ) );
*/
        confidence = 99;

        hs = new HistoricalSimulation( new Portfolio(allAssets, null), confidence );
        double var = hs.computeValueAtRisk();

        Assert.assertEquals(86.0,var,0.001);
    }

    private void setupWithLargeStockDataFile(final int confidence ) throws Exception
    {
//        stockDataFiles.clear();
//        stockDataFiles.add( ResourceHelper.getInstance().getResource( "GOOG_Tester.csv" ) );
        List<Asset> allAssets = new ArrayList<>(3);
        {
            Asset asset1 = new Asset(new Stock("GOOG"), 1000.0, new SimpleDate(2004, 8, 19), new SimpleDate(2006, 8, 25));//GOOG_Tester

            allAssets.add(asset1);
        }

        hs = new HistoricalSimulation( new Portfolio(allAssets,null),
                                       confidence );
    }

}
