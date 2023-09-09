package com.rp.risk_management.marketdata.api;

import com.rp.risk_management.marketdata.model.Quote;
import com.rp.risk_management.marketdata.model.Stock;
import com.rp.risk_management.util.date.SimpleDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MarketDataApiTest {
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetQuoteVsQuotes() throws Exception {
        MarketDataApi marketDataApi = new YahooMarketDataApi();

        Stock stock = new Stock("AAPL");
        SimpleDate startDate = new SimpleDate(2016, 11, 3);
        SimpleDate endDate = new SimpleDate(2016, 11, 4);

        List<Quote> quotes = marketDataApi.getMarketData(stock, startDate, endDate);

        Quote startDateQuote;
        {
            Assert.assertEquals(1, quotes.size());

            Quote endDateQuote = quotes.get(0);
            startDateQuote = quotes.get(1);

            Assert.assertEquals(stock, startDateQuote.getStock());
            Assert.assertEquals(startDate, startDateQuote.getSimpleDate());
            Assert.assertEquals(109.83, startDateQuote.getAdjClose().doubleValue(), 0.01);
            Assert.assertEquals(111.46, startDateQuote.getHigh().doubleValue(), 0.01);
            Assert.assertEquals(109.55, startDateQuote.getLow().doubleValue(), 0.01);
            Assert.assertEquals(110.98, startDateQuote.getOpen().doubleValue(), 0.01);
            Assert.assertEquals(109.83, startDateQuote.getClose().doubleValue(), 0.01);
            Assert.assertEquals(26932600, startDateQuote.getVolume().doubleValue(), 1000000);//Yahoo seems to give inconsistent values


            Assert.assertEquals(stock, endDateQuote.getStock());
            Assert.assertEquals(endDate, endDateQuote.getSimpleDate());
            Assert.assertEquals(108.834, endDateQuote.getAdjClose().doubleValue(), 0.01);
            Assert.assertEquals(110.25, endDateQuote.getHigh().doubleValue(), 0.01);
            Assert.assertEquals(108.11, endDateQuote.getLow().doubleValue(), 0.01);
            Assert.assertEquals(108.53, endDateQuote.getOpen().doubleValue(), 0.01);
            Assert.assertEquals(108.84, endDateQuote.getClose().doubleValue(), 0.01);
            Assert.assertEquals(30666900, endDateQuote.getVolume().doubleValue(), 1000000);//Yahoo seems to give inconsistent values
        }
        Quote quote2 = marketDataApi.getMarketData(stock, startDate);

        // compare all values except volume -- Yahoo seems to give inconsistent values
        Assert.assertEquals(startDateQuote.getStock(), quote2.getStock());
        Assert.assertEquals(startDateQuote.getSimpleDate(), quote2.getSimpleDate());
        Assert.assertEquals(startDateQuote.getAdjClose(), quote2.getAdjClose());
        Assert.assertEquals(startDateQuote.getHigh(), quote2.getHigh());
        Assert.assertEquals(startDateQuote.getLow(), quote2.getLow());
        Assert.assertEquals(startDateQuote.getOpen(), quote2.getOpen());
        Assert.assertEquals(startDateQuote.getClose(), quote2.getClose());
    }

    @Test
   public void testGetQuotes() throws Exception {
        MarketDataApi marketDataApi = new YahooMarketDataApi();

        Stock stock = new Stock("AAPL");
        SimpleDate startDate = new SimpleDate(2014, 11, 3);
        SimpleDate endDate = new SimpleDate(2015, 11, 4);

        List<Quote> quotes = marketDataApi.getMarketData(stock, startDate, endDate);
        Assert.assertEquals(253, quotes.size());
    }
}