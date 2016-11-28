package com.rp.risk_management.analytics.portfolio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.rp.risk_management.marketdata.model.Stock;
import com.rp.risk_management.model.Position;
import com.rp.risk_management.model.Portfolio;
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
        List<Position> allPositions = new ArrayList<>(3);
        {
            Position position1 = new Position(new Stock("GOOG"), 1000.0, new SimpleDate(2004, 8, 19), new SimpleDate(2006, 8, 25));//GOOG_Tester
            Position position3 = new Position(new Stock("MSFT"), 2000.0, new SimpleDate(2013, 8, 13), new SimpleDate(2013, 11, 15));//MSFT_15082013_15112013

            allPositions.add(position1);
            allPositions.add(position3);
        }

        confidence = 99;
        hs = new HistoricalSimulation( new Portfolio(allPositions,null), confidence );

        double var =hs.computeValueAtRisk();
        Assert.assertEquals(104.0,var,0.001);
    }

    public void testShouldComputeVaRUsingHistoricalSimulationForThreeStocks() throws Exception
    {
        List<Position> allPositions = new ArrayList<>(3);
        {
            Position position1 = new Position(new Stock("GOOG"), 1000.0, new SimpleDate(2004, 8, 19), new SimpleDate(2006, 8, 25));//GOOG_Tester
            Position position2 = new Position(new Stock("GOOG"), 1500.0, new SimpleDate(2013, 9, 19), new SimpleDate(2013, 10, 18));//GOOG_190913_181013
            Position position3 = new Position(new Stock("MSFT"), 2000.0, new SimpleDate(2013, 8, 13), new SimpleDate(2013, 11, 15));//MSFT_15082013_15112013

            allPositions.add(position1);
            allPositions.add(position2);
            allPositions.add(position3);
        }

        confidence = 99;

        hs = new HistoricalSimulation( new Portfolio(allPositions, null), confidence );
        double var = hs.computeValueAtRisk();

        Assert.assertEquals(86.0,var,0.001);
    }

    private void setupWithLargeStockDataFile(final int confidence ) throws Exception
    {
//        stockDataFiles.clear();
//        stockDataFiles.add( ResourceHelper.getInstance().getResource( "GOOG_Tester.csv" ) );
        List<Position> allPositions = new ArrayList<>(3);
        {
            Position position1 = new Position(new Stock("GOOG"), 1000.0, new SimpleDate(2004, 8, 19), new SimpleDate(2006, 8, 25));//GOOG_Tester

            allPositions.add(position1);
        }

        hs = new HistoricalSimulation( new Portfolio(allPositions,null),
                                       confidence );
    }

}
