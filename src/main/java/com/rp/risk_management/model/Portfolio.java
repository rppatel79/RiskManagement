/**
 * 
 */
package com.rp.risk_management.model;

import com.rp.risk_management.util.model.PortfolioUtil;

import java.util.List;

/**
 * Represent a portfolio (a collection of assets and options_) in the program.
 */
public class Portfolio
{
    /** List of options held in this portfolio. */
    private final List<Option> options_;
    /** List of stocks held in this portfolio. */
    private final List<Asset> assets_;
    
    /**
     * Initialise a portfolio with a list of assets and options_.
     * 
     * @param assets list of assets to add to this portfolio
     * @param options list of options_ to add to this portfolio
     */
    public Portfolio( List<Asset> assets, List<Option> options )
    {
        this.options_ = options;
        this.assets_ = assets;
    }

    /**
     * @return options_ in this portfolio
     */
    public List<Option> getOptions()
    {
        return options_;
    }


    public List<Asset> getAssets() {
        return assets_;
    }

}
