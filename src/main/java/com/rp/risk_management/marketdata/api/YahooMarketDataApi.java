package com.rp.risk_management.marketdata.api;

import com.rp.risk_management.marketdata.model.Quote;
import com.rp.risk_management.marketdata.model.Stock;
import com.rp.risk_management.util.date.SimpleDate;
import com.rp.risk_management.util.date.SimpleDateHelper;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YahooMarketDataApi implements MarketDataApi
{
    @Override
    public Quote getMarketData(Stock stock,SimpleDate date) throws Exception
    {
        List<Quote> quotes = getMarketData(stock, date, SimpleDateHelper.addDays(date, 1));
        if (quotes.size() != 2)
            throw new IllegalStateException("Expected [2] historical record, but recieved ["+quotes.size()+"]");
        return quotes.get(1);
    }

    @Override
    public List<Quote> getMarketData(Stock stock,SimpleDate fromDate, SimpleDate toDate) throws Exception
    {
        return getMarketData(stock,fromDate, toDate, Interval.DAILY);
    }

    @Override
    public List<Quote> getMarketData(Stock stock,SimpleDate fromDate, SimpleDate toDate, Interval interval) throws Exception
    {
        yahoofinance.histquotes.Interval yahooInterval = convertInterval(interval);
        yahoofinance.Stock results = YahooFinance.get(stock.getStock(), fromDate.getCalendar(),toDate.getCalendar(), yahooInterval);

        return getQuotes(stock, results);
    }

    private yahoofinance.histquotes.Interval convertInterval(Interval interval) {
        switch (interval)
        {
            case DAILY:
                return yahoofinance.histquotes.Interval.DAILY;
            case MONTHLY:
                return yahoofinance.histquotes.Interval.MONTHLY;
            case WEEKLY:
                return yahoofinance.histquotes.Interval.WEEKLY;
            default:
                throw new IllegalArgumentException("Unknown interval ["+interval+"]");
        }
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
