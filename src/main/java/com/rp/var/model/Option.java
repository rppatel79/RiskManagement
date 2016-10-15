package com.rp.var.model;

import java.io.File;

/**
 * Represents an option in the program.
 * Holds all possible parameters for VaR computation of this option.
 */
public class Option
{
    public enum OptionStyle {European, American, Bermuda}
    public enum OptionType {Call, Put}

    /** Option parameters. */
    // TODO daily or annual volatility, annual = daily * sqrt(252)
    private double interest, stockPrice, strike, dailyVolatility;
    private int    timeToMaturity;
    /** Four letter code for the stock on which this option is held. */
    private String stockID;
    /** Number of shares of the underlying asset held undert his option. */
    private int    numShares;
    /** Historical price data of the option's underlying asset. */
    private File   priceData;

    private final OptionStyle optionStyle_;
    private final OptionType optionType_;

    /**
     * Creates an option using standard option parameters
     * 
     * @param stockPrice
     * @param strike
     * @param interest annual?
     * @param dailyVolatility
     * @param timeToMaturity in days
     * @param stockID of the underlying asset.
     * @param optionType MUST BE ONE OF VARUTILS.AMERICAN/EUROPEAN-CALL/PUT
     * @param priceData of the underlying asset.
     */
    public Option( double stockPrice, int numShares, double strike, double interest, double dailyVolatility,
                   int timeToMaturity, String stockID, File priceData, OptionStyle optionStyle, OptionType optionType )
    {
        this.stockPrice = stockPrice;
        this.strike = strike;
        this.interest = interest;
        this.dailyVolatility = dailyVolatility;
        this.timeToMaturity = timeToMaturity;
        this.stockID = stockID;
        this.priceData = priceData;
        this.numShares = numShares;

        optionStyle_=optionStyle;
        optionType_=optionType;
    }

    /**
     * @return the interest
     * @deprecated
     */
    public double getInterest()
    {
        return interest;
    }

    /**
     * @param interest the interest to set
     */
    public void setInterest( double interest )
    {
        this.interest = interest;
    }

    /**
     * @return the strike
     */
    public double getStrike()
    {
        return strike;
    }

    public OptionType getOptionType() {
        return optionType_;
    }

    /**
     * @return the dailyVolatility
     * @deprecated TODO REMOVE
     */
    public double getDailyVolatility()
    {
        return dailyVolatility;
    }

    /**
     * @return the initialStockPrice
     */
    public double getInitialStockPrice()
    {
        return stockPrice;
    }

    /**
     * @param initialStockPrice the initialStockPrice to set
     */
    public void setInitialStockPrice( double initialStockPrice )
    {
        this.stockPrice = initialStockPrice;
    }

    /**
     * @return the timeToMaturity in days
     */
    public int getTimeToMaturity()
    {
        return timeToMaturity;
    }

    /**
     * @param timeToMaturity the time to maturity of the Option in days
     */
    public void setTimeToMaturity( int timeToMaturity )
    {
        this.timeToMaturity = timeToMaturity;
    }

    /**
     * @return the stockID
     */
    public String getStockID()
    {
        return stockID;
    }

    /**
     * @param stockID the stockID to set
     */
    public void setStockID( String stockID )
    {
        this.stockID = stockID;
    }

    public OptionStyle getOptionStyle() {
        return optionStyle_;
    }

    /**
     * @return the priceData
     */
    public File getPriceData()
    {
        return priceData;
    }

    /**
     * @param priceData the priceData to set
     */
    public void setPriceData( File priceData )
    {
        this.priceData = priceData;
    }

    /**
     * @return the numShares
     */
    public int getNumShares()
    {
        return numShares;
    }

}
