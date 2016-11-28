package com.rp.risk_management.util.model;

import com.rp.risk_management.marketdata.api.MarketDataApi;
import com.rp.risk_management.marketdata.api.YahooMarketDataApi;
import com.rp.risk_management.marketdata.model.Quote;
import com.rp.risk_management.model.Position;
import com.rp.risk_management.model.Portfolio;
import com.rp.risk_management.util.QuoteHelper;

import java.util.ArrayList;
import java.util.List;

public class PortfolioUtil
{
    /**
     *
     * @param portfolio
     * @return the investments made in this portfolio.
     */
    public static List<Double> getAssetInvestment(Portfolio portfolio)
    {
        List<Double> ret = new ArrayList<>();
        for (Position position : portfolio.getPositions())
            ret.add(position.getInvestment());

        return ret;
    }

    /**
     * @param portfolio
     */
    public static List<List<Quote>> getStockQuotes(Portfolio portfolio)
    {
        List<List<Quote>> allQuotes = new ArrayList<>();

        MarketDataApi marketDataApi = new YahooMarketDataApi();

        for (Position position : portfolio.getPositions()) {
            try {
                allQuotes.add(marketDataApi.getMarketData(position.getStock(), position.getStartDate(), position.getEndDate()));
            }
            catch (Exception ex)
            {
                throw new RuntimeException("Unable to get market data for the position ["+position+"]",ex);
            }
        }
        return allQuotes;
    }

    /**
     * @param portfolio
     */
    public static List<double[]> getReturns(Portfolio portfolio)
    {
        return QuoteHelper.getReturnsFromQuotes(getStockQuotes(portfolio));
    }

    /**
     * Returns the total value of the investments within the portfolio.
     * @param portfolio
     */
    public static double getAssetsValue(Portfolio portfolio)
    {
        double sum = 0.0;
        for( double value : getAssetInvestment(portfolio) )
        {
            sum += value;
        }

        return sum;
    }
}
