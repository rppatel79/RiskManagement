package com.rp.risk_management.util.model;

import com.rp.risk_management.marketdata.model.Quote;
import com.rp.risk_management.model.Asset;
import com.rp.risk_management.model.Portfolio;
import com.rp.risk_management.util.FileHelper;

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
        for (Asset asset : portfolio.getAssets())
            ret.add(asset.getInvestment());

        return ret;
    }

    /**
     * @param portfolio
     */
    public static List<List<Quote>> getStockQuotes(Portfolio portfolio) {
        List<List<Quote>> allQuotes = new ArrayList<>();
        for (Asset stocks : portfolio.getAssets())
            allQuotes.add(stocks.getQuotes());

        return allQuotes;
    }

    /**
     * @param portfolio
     */
    public static List<double[]> getReturns(Portfolio portfolio)
    {
        return FileHelper.getReturnsFromQuotes(getStockQuotes(portfolio));
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
