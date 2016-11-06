package com.rp.risk_management.analytics.portfolio;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.rp.risk_management.marketdata.model.Stock;
import com.rp.risk_management.model.Asset;
import com.rp.risk_management.model.Portfolio;
import com.rp.risk_management.util.ResourceHelper;
import com.rp.risk_management.util.date.SimpleDate;
import org.junit.Before;
import org.junit.Test;

public class ModelBuilding_UnitTest
{
    private final double            delta = 1.0;

    @Before
    public void setUp() throws Exception
    {
    }

    @Test
    public void shouldReturnCorrectTenDayVaRForGoogleStocks() throws Exception
    {
        List<Asset> allAssets = new ArrayList<>(3);
        {
            Asset asset1 = new Asset(new Stock("GOOG"), 1000000.0, new SimpleDate(2013, 9, 19), new SimpleDate(2013, 10, 18));//GOOG_190913_181013

            allAssets.add(asset1);
        }

        int confidence = 99;
        int  timePeriod = 10;
        ModelBuilding modelBuilding = new ModelBuilding( new Portfolio(allAssets, null), confidence, timePeriod);
        double calculatedVaR = modelBuilding.computeForMultipleStocks();
        double expectedVaR = 220276.0;
        assertEquals( expectedVaR, calculatedVaR, delta );
    }


    @Test
    public void shouldComputeCorrectVaRForThreeStocks() throws Exception
    {
        List<Asset> allAssets = new ArrayList<>(3);
        {
            Asset asset1 = new Asset(new Stock("GOOG"), 1000000.0, new SimpleDate(2013, 9, 19), new SimpleDate(2013, 10, 18));//GOOG_190913_181013
            Asset asset2 = new Asset(new Stock("GOOG"), 1500000.0, new SimpleDate(2004, 8, 19), new SimpleDate(2006, 8, 25));//GOOG_Tester
            Asset asset3 = new Asset(new Stock("MSFT"), 2000000.0, new SimpleDate(2013, 8, 13), new SimpleDate(2013, 11, 15));//MSFT_15082013_15112013

            allAssets.add(asset1);
            allAssets.add(asset2);
            allAssets.add(asset3);
        }

        int confidence = 99;
        int timePeriod = 10;
        ModelBuilding modelBuilding = new ModelBuilding( new Portfolio(allAssets,null), confidence, timePeriod);
        double calculatedVaR = modelBuilding.computeForMultipleStocks();
        double expectedVaR = 413122.0;
        assertEquals( expectedVaR, calculatedVaR, delta );
    }
}
