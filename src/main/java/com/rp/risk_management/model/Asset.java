package com.rp.risk_management.model;

import java.io.File;

/**
 * Representation of an asset.
 * Holds parameters for historical stock price data location, an identifier and the investment.
 */
public class Asset
{
    /** Historical stock price data file for asset. (csv) */
    private File   data;
    /** Identifier for asset. */
    private String ID;
    /** Amount of money invested in the asset. */
    private double investment;

    /** Constructor to initialise an asset. */
    public Asset( File data, String iD, double investment )
    {
        this.data = data;
        ID = iD;
        this.investment = investment;
    }


    /**
     * @return the historical price data for this asset
     */
    public File getData()
    {
        return data;
    }

    /**
     * @return the identifier of this asset
     */
    public String getID()
    {
        return ID;
    }


    /**
     * @return the investment in this asset
     */
    public double getInvestment()
    {
        return investment;
    }


}
