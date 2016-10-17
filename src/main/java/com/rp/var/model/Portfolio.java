/**
 * 
 */
package com.rp.var.model;

import com.rp.var.analytics.portfolio.VarUtils;
import com.rp.var.util.FileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represent a portfolio (a collection of assets and options) in the program.
 */
public class Portfolio
{
    /** List of options held in this portfolio. */
    private List<Option> options;
    /** List of historical stock data for the assets in this portfolio. */
    private List<File>   stockPriceDataFiles;
    /** List of investments made in assets of this portfolio. */
    private List<Double> investments;
    /** List of assets held in this portfolio. */
    //private ArrayList<Asset>  assets;
    /** Name of the portfolio for identification. */
    private String            name = "";
    
    
    /**
     * Initialises a blank portfolio. Assets and options can be added later.
     */
    public Portfolio()
    {
        //assets = new ArrayList<Asset>();
        investments = new ArrayList<Double>();
        stockPriceDataFiles = new ArrayList<File>();
        options = new ArrayList<Option>();
    }

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
    public Portfolio( List<Option> options, List<File> stockPriceData,
                      List<Double> investments )
    {
        this.options = options;
        this.stockPriceDataFiles = stockPriceData;
        this.investments = investments;
    }

    /**
     * Updates the assets and historical stock price data held in this portfolio.
     */
    private void updatePortfolioAssets(List<Asset> assets)
    {
        stockPriceDataFiles = new ArrayList<File>();
        investments = new ArrayList<Double>();

        for( Asset asset : assets )
        {
            stockPriceDataFiles.add( asset.getData() );
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

    /**
     * @return a list of stock data files from this portfolio
     */
    public List<File> getStockPriceDataFiles()
    {
        return stockPriceDataFiles;
    }

    public List<Double> getClosingPrices(int idx)
    {
        return FileHelper.getClosingPrices(stockPriceDataFiles.get(idx));
    }

    public double[] getReturns( int idx )
    {
        return VarUtils.computeDailyReturns(FileHelper.getClosingPrices(stockPriceDataFiles.get(idx)));
    }

    public List<double[]> getReturns( )
    {
        return FileHelper.getReturnsFromFiles(stockPriceDataFiles);
    }


    /**
     * @return the investments made in this portfolio.
     */
    public List<Double> getInvestments()
    {
        return investments;
    }

    /**
     * @param investments the investments to set
     */
    public void setInvestments( List<Double> investments )
    {
        this.investments = investments;
    }

    /**
     * Add an asset to this portfolio.
     * @param asset
     */
    public void addAsset( Asset asset )
    {
        updatePortfolioAssets(Collections.singletonList(asset));
    }

    /**
     * @param assets the assets to set
     */
    public void setAssets( List<Asset> assets )
    {
        updatePortfolioAssets(assets);
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

    /**
     * @return the name of the portfolio
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set name of this portfolio.
     * @param name the name to set
     */
    public void setName( String name )
    {
        this.name = name;
    }
}
