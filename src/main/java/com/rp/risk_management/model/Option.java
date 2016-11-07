package com.rp.risk_management.model;

import com.rp.risk_management.marketdata.model.Quote;

import java.util.*;

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
    private double interest_, stockPrice_, strike_, dailyVolatility_;
    private int    timeToMaturity;
    /** Number of shares of the underlying asset held undert his option. */
    private final int numShares;
    /** Historical price data of the option's underlying asset.*/
    private final List<Quote> underlyingPrices_;

    private final OptionStyle optionStyle_;
    private final OptionType optionType_;

    /**
     * Creates an option using standard option parameters
     * 
     * @param stockPrice stock price
     * @param strike strike_ price of the option
     * @param interest annualized rate
     * @param dailyVolatility the daily vol
     * @param timeToMaturity in days
     * @param optionType MUST BE ONE OF VARUTILS.AMERICAN/EUROPEAN-CALL/PUT
     * @param underlyingPrices of the underlying asset.
     */
    public Option( double stockPrice, int numShares, double strike, double interest, double dailyVolatility,
                   int timeToMaturity, List<Quote> underlyingPrices, OptionStyle optionStyle, OptionType optionType )
    {
        this.stockPrice_ = stockPrice;
        this.strike_ = strike;
        this.interest_ = interest;
        this.dailyVolatility_ = dailyVolatility;
        this.timeToMaturity = timeToMaturity;
        this.underlyingPrices_ = Collections.unmodifiableList(underlyingPrices);
        this.numShares = numShares;

        optionStyle_=optionStyle;
        optionType_=optionType;
    }

    /**
     * @return the interest_
     * @deprecated
     */
    public double getInterest()
    {
        return interest_;
    }

    /**
     * @return the strike_
     */
    public double getStrike()
    {
        return strike_;
    }

    public OptionType getOptionType() {
        return optionType_;
    }

    /**
     * @return the dailyVolatility_
     * @deprecated TODO REMOVE
     */
    public double getDailyVolatility()
    {
        return dailyVolatility_;
    }

    /**
     * @return the initialStockPrice
     */
    public double getInitialStockPrice()
    {
        return stockPrice_;
    }

    /**
     * @param initialStockPrice the initialStockPrice to set
     * TODO Does this need to be immutable?
     */
    public void setInitialStockPrice( double initialStockPrice )
    {
        this.stockPrice_ = initialStockPrice;
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
     * TODO Does this need to be immutable?
     */
    public void setTimeToMaturity( int timeToMaturity )
    {
        this.timeToMaturity = timeToMaturity;
    }

    public OptionStyle getOptionStyle() {
        return optionStyle_;
    }

    public List<Quote> getUnderlyingPrices() {
        return underlyingPrices_;
    }

    /**
     * @return the numShares
     */
    public int getNumShares()
    {
        return numShares;
    }

}
