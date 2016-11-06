package com.rp.risk_management;

import com.rp.risk_management.marketdata.api.MarketDataApi;
import com.rp.risk_management.marketdata.api.YahooMarketDataApi;
import com.rp.risk_management.marketdata.model.Quote;
import com.rp.risk_management.marketdata.model.Stock;
import com.rp.risk_management.util.FileHelper;
import com.rp.risk_management.util.ResourceHelper;
import com.rp.risk_management.util.date.SimpleDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class CsvToMarketDataApiTest
{
    @Before
    public void setUp() throws Exception
    {
    }

    @Test
    public void test() throws Exception
    {
        String[] filenames = {"MSFT_Apr2012_Apr2013.csv","GOOG_Tester.csv","GOOG_190913_181013.csv","MSFT_15082013_15112013.csv","APPLE.csv"};
        Holding[] holdings = {new Holding(new Stock("MSFT"),new SimpleDate(2012,4,1),new SimpleDate(2013,4,1)),//MSFT_Apr2012_Apr2013.csv
            new Holding(new Stock("GOOG"),new SimpleDate(2004,8,19),new SimpleDate(2006,8,25)),//GOOG_Tester.csv
            new Holding(new Stock("GOOG"),new SimpleDate(2013,9,19),new SimpleDate(2013,10,18)),//GOOG_190913_181013.csv
            new Holding(new Stock("MSFT"),new SimpleDate(2013,8,13),new SimpleDate(2013,11,15)),//MSFT_15082013_15112013
            new Holding(new Stock("AAPL"),new SimpleDate(2013,3,7),new SimpleDate(2013,12,3))};//APPLE.csv

        for (int i =0 ; i < filenames.length ; i++) {
            test(filenames[i], holdings[i]);
        }
    }

    private void test(String filename,Holding holding) throws Exception
    {
//        System.out.println("Testing ["+filename+"]");
//        File file = ResourceHelper.getInstance().getResource(filename);
//        List<Double> ret=FileHelper.getClosingPrices(file);
//        MarketDataApi marketDataApi = new YahooMarketDataApi();
//        List<Quote> quotes =marketDataApi.getMarketData(holding.stock_,holding.startDate_,holding.endDate_);
//        Assert.assertEquals("Failed test for ["+filename+"] ",quotes.size(),ret.size());
//        for (int i = 0 ; i < ret.size() ; i++)
//        {
//            double closingValue =ret.get(i);
//            Quote quote =quotes.get(i);
//
//            Assert.assertEquals("Failed test for ["+filename+"] on ["+i+"]",closingValue,quote.getClose().doubleValue(),0.1);
//        }
    }

    private static class Holding
    {
        final Stock stock_;
        final SimpleDate startDate_;
        final SimpleDate endDate_;

        Holding(Stock stock, SimpleDate startDate, SimpleDate endDate) {
            stock_ = stock;
            startDate_ = startDate;
            endDate_ = endDate;
        }
    }
}
