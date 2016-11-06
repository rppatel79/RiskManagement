package com.rp.risk_management.model;

import com.rp.risk_management.marketdata.api.MarketDataApi;
import com.rp.risk_management.marketdata.api.YahooMarketDataApi;
import com.rp.risk_management.marketdata.model.Quote;
import com.rp.risk_management.marketdata.model.Stock;
import com.rp.risk_management.util.date.SimpleDate;

import java.io.File;
import java.util.List;

/**
 * Representation of an asset.
 * Holds parameters for historical stock price data location, an identifier and the investment.
 */
public class Asset
{
    /** Amount of money invested in the asset. */
    private double investment;
    private Stock stock_;
    private List<Quote> quotes_;

    /** Constructor to initialise an asset.
     * */
    public Asset(Stock stock, double investment, SimpleDate startDate, SimpleDate endDate) throws Exception
    {
        stock_ = stock;
        this.investment = investment;

        MarketDataApi marketDataApi = new YahooMarketDataApi();
        quotes_ =marketDataApi.getMarketData(stock_,startDate,endDate);
    }


    /**
     * @return the investment in this asset
     */
    public double getInvestment()
    {
        return investment;
    }

    public List<Quote> getQuotes() {
        return quotes_;
    }
}
