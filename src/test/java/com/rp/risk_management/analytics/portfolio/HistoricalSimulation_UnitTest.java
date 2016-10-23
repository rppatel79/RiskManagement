package com.rp.risk_management.analytics.portfolio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.rp.risk_management.model.Portfolio;
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
        stockValues = new ArrayList<Double>();
        stockDataFiles = new ArrayList<File>();
    }

    public void testShouldComputeCorrectVaRUsingHistoricalSimulation()
    {
        stockValues.clear();
        stockValues.add( 1000.0 );
        confidence = 99;
        setupWithLargeStockDataFile( stockValues, confidence );
        calculatedVaR = hs.computeValueAtRisk();
        Assert.assertEquals(65.0745,calculatedVaR,0.001);
    }

    public void testShouldComputeVaRUsingHistoricalSimulationForTwoStocks()
    {
        stockValues.clear();
        stockValues.add( 1000.0 );
        stockValues.add( 2000.0 );
        stockDataFiles.clear();
        stockDataFiles.add( new File( "test-classes/GOOG_Tester.csv" ) );
        stockDataFiles.add( new File( "test-classes/MSFT_15082013_15112013.csv" ) );
        confidence = 99;
        hs = new HistoricalSimulation( new Portfolio(null, stockDataFiles, stockValues), confidence );

        double var =hs.computeValueAtRisk();
        Assert.assertEquals(104.0,var,0.001);
    }

    public void testShouldComputeVaRUsingHistoricalSimulationForThreeStocks()
    {
        stockValues.clear();
        stockValues.add( 1000.0 );
        stockValues.add( 1500.0 );
        stockValues.add( 2000.0 );
        stockDataFiles.clear();
        stockDataFiles.add( new File( "test-classes/GOOG_Tester.csv" ) );
        stockDataFiles.add( new File( "test-classes/GOOG_190913_181013.csv" ) );
        stockDataFiles.add( new File( "test-classes/MSFT_15082013_15112013.csv" ) );
        confidence = 99;

        hs = new HistoricalSimulation( new Portfolio(null, stockDataFiles, stockValues), confidence );
        double var = hs.computeValueAtRisk();

        Assert.assertEquals(86.0,var,0.001);
    }

    private void setupWithLargeStockDataFile(
                                              final List<Double> portfolioValues,
                                              final int confidence )
    {
        stockDataFiles.clear();
        stockDataFiles.add( new File( "test-classes/GOOG_Tester.csv" ) );
        hs = new HistoricalSimulation( new Portfolio(null, stockDataFiles, portfolioValues),
                                       confidence );
    }

}
