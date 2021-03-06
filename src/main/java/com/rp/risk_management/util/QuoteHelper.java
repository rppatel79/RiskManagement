package com.rp.risk_management.util;

import com.rp.risk_management.analytics.portfolio.VarUtils;
import com.rp.risk_management.marketdata.model.Quote;
import java.util.ArrayList;
import java.util.List;

public class QuoteHelper
{
    //private static final Logger logger_ = Logger.getLogger(QuoteHelper.class);

    /**
     * Uses {@code getReturnsFromQuotes(List<List<Quote>>)} to iteratively extract returns from a list of quotes
     *
     * @return List of arrays containing returns for each List of Quotes
     */
    public static List<double[]> getReturnsFromQuotes(List<List<Quote>> allQuotes )
    {
        List<double[]> returns = new ArrayList<>();
        for( List<Quote> stockQuotes : allQuotes )
        {
            returns.add( VarUtils.computeDailyReturns(getClosingPrices( stockQuotes ) ));
        }

        return returns;
    }

    /**
     * Saves the series of closing prices from the historical stock data into a list of prices.
     * @param quotes historical stock data
     * @return list of closing prices
     * TODO Move me to another class
     */
    public static List<Double> getClosingPrices( List<Quote> quotes )
    {
        List<Double> ret = new ArrayList<>(quotes.size());
        for (Quote quote : quotes)
            ret.add(quote.getClose().doubleValue());

        return ret;
    }
}
