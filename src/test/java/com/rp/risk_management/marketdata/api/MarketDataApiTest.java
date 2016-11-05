package com.rp.risk_management.marketdata.api;

import com.rp.risk_management.marketdata.model.Quote;
import com.rp.risk_management.marketdata.model.Stock;
import com.rp.risk_management.util.date.SimpleDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class MarketDataApiTest {
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetQuote() throws Exception {
        MarketDataApi marketDataApi = new YahooMarketDataApi();

        Stock stock = new Stock("AAPL");
        SimpleDate startDate = new SimpleDate(2015, 11, 3);
        SimpleDate endDate = new SimpleDate(2015, 11, 4);

        List<Quote> quotes = marketDataApi.getMarketData(stock,startDate,endDate);

        Quote quote;
        {
            Assert.assertEquals(1,quotes.size());
            quote = quotes.get(0);
            Assert.assertEquals(stock, quote.getStock());
            Assert.assertEquals(startDate, quote.getSimpleDate());
            Assert.assertEquals(116.439316, quote.getAdjClose().doubleValue(), 0.0001);
            Assert.assertEquals(119.25, quote.getHigh().doubleValue(), 0.0001);
            Assert.assertEquals(114.22, quote.getLow().doubleValue(), 0.0001);
            Assert.assertEquals(116.55, quote.getOpen().doubleValue(), 0.0001);
            Assert.assertEquals(119.03, quote.getClose().doubleValue(), 0.0001);
            Assert.assertEquals(7.85617E7, quote.getVolume().doubleValue(), 0.0001);
        }
        Quote quote2 = marketDataApi.getMarketData(stock,startDate);
        Assert.assertEquals(quote, quote2);
    }
}