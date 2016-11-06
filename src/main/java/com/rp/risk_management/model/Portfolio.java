/**
 * 
 */
package com.rp.risk_management.model;

import com.rp.risk_management.analytics.portfolio.VarUtils;
import com.rp.risk_management.marketdata.model.Quote;
import com.rp.risk_management.util.FileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent a portfolio (a collection of assets and options) in the program.
 */
public class Portfolio
{
    /** List of options held in this portfolio. */
    private List<Option> options;

    private List<List<Quote>>   stockQuotes_;

    /** List of investments made in assets of this portfolio. */
    private List<Double> investments;

    
    /**
     * Initialise a portfolio with a list of assets and options.
     * 
     * @param assets list of assets to add to this portfolio
     * @param options list of options to add to this portfolio
     */
    public Portfolio( List<Asset> assets, List<Option> options )
    {
        this.options = options;

        updatePortfolioAssets(assets);
    }

    /**
     * Initialise a portfolio using a list of options list of stock data and a list of investments.
     * 
     */
    public Portfolio( List<Option> options, List<List<Quote>> stockPriceData,
                      List<Double> investments )
    {
        this.options = options;
        this.stockQuotes_ = stockPriceData;
        this.investments = investments;
        assert investments.size() == stockQuotes_.size();
    }

    /**
     * Updates the assets and historical stock price data held in this portfolio.
     */
    private void updatePortfolioAssets(List<Asset> assets)
    {
        stockQuotes_ = new ArrayList<List<Quote>>(assets.size());
        investments = new ArrayList<Double>();

        for( Asset asset : assets )
        {
            stockQuotes_.add( asset.getQuotes() );
            investments.add( asset.getInvestment() );
        }
    }

    /**
     * @return
     *         options in this portfolio
     */
    public List<Option> getOptions()
    {
        return options;
    }

    public List<List<Quote>> getStockQuotes() {
        return stockQuotes_;
    }

    public double[] getReturns(int idx )
    {
        return VarUtils.computeDailyReturns(FileHelper.getClosingPrices(stockQuotes_.get(idx)));
    }

    public List<double[]> getReturns( )
    {
        return FileHelper.getReturnsFromQuotes(stockQuotes_);
    }


    /**
     * @return the investments made in this portfolio.
     */
    public List<Double> getInvestments()
    {
        return investments;
    }

    /**
     * Returns the total value of the investments within the portfolio.
     */
    public double getAssetsValue()
    {
        double sum = 0.0;
        for( double value : investments )
        {
            sum += value;
        }

        return sum;
    }

}
