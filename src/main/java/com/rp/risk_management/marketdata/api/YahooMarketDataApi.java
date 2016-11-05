package com.rp.risk_management.marketdata.api;

import com.rp.risk_management.marketdata.model.Quote;
import com.rp.risk_management.marketdata.model.Stock;
import com.rp.risk_management.util.date.SimpleDate;
import com.rp.risk_management.util.date.SimpleDateHelper;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YahooMarketDataApi implements MarketDataApi
{
    @Override
    public Quote getMarketData(Stock stock,SimpleDate date) throws Exception
    {
        List<Quote> quotes = getMarketData(stock, date, SimpleDateHelper.addDays(date, 1));
        if (quotes.size() != 1)
            throw new IllegalStateException();
        return quotes.get(0);
    }

    @Override
    public List<Quote> getMarketData(Stock stock,SimpleDate fromDate, SimpleDate toDate) throws Exception
    {
        yahoofinance.Stock results = YahooFinance.get(stock.getStock(), fromDate.getCalendar(),toDate.getCalendar());

        return getQuotes(stock, results);
    }

    private List<Quote> getQuotes(Stock stock, yahoofinance.Stock results) throws IOException {
        List<Quote> ret = new ArrayList<>(results.getHistory().size());
        for (HistoricalQuote historicalQuote : results.getHistory())
        {
            ret.add(buildQuote(stock,historicalQuote));
        }
        return ret;
    }

    private Quote buildQuote(Stock stock, HistoricalQuote quote) {
        return new Quote(stock,new SimpleDate(quote.getDate()),quote.getOpen(),quote.getLow(),quote.getHigh(),quote.getClose(),quote.getAdjClose(),quote.getVolume());
    }

}
