package com.rp.risk_management.model;

import com.rp.risk_management.marketdata.model.Stock;
import com.rp.risk_management.util.date.SimpleDate;

import java.util.List;

/**
 * Representation of an position.
 * Holds parameters for historical stock price data location, an identifier and the investment_.
 */
public class Position
{
    /** Amount of money invested in the asset. */
    private final double investment_;
    private final Stock stock_;
    private final SimpleDate startDate_;
    private final SimpleDate endDate_;

    /** Constructor to initialise an asset.
     * */
    public Position(Stock stock, double investment, SimpleDate startDate, SimpleDate endDate) {
        investment_ = investment;
        stock_ = stock;
        startDate_ = startDate;
        endDate_ = endDate;
    }

    /**
     * @return the investment_ in this asset
     */
    public double getInvestment()
    {
        return investment_;
    }

    public Stock getStock() {
        return stock_;
    }

    public SimpleDate getStartDate() {
        return startDate_;
    }

    public SimpleDate getEndDate() {
        return endDate_;
    }
}
