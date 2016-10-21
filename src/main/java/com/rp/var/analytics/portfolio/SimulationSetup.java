package com.rp.var.analytics.portfolio;

import com.rp.var.model.Portfolio;

/**
 * Combines the user-selected VaR model, option pricing model, confidence level, time horizon and
 * user-selected portfolio to use when computing VaR.
 */
public class SimulationSetup
{
    /** The model to use for computing VaR. */
    private String    model;
    /** The confidence level to getOptionPrice VaR at. */
    private int       confidenceLevel;
    /** The time horizon to getOptionPrice VaR over. */
    private int       timeHorizon;
    /** The portfolio to getOptionPrice VaR for. */
    private Portfolio portfolio;

    /**
     * @return user-selected portfolio
     */
    public Portfolio getPortfolio()
    {
        return portfolio;
    }

    /**
     * Creates a simulation setup using the user-selected VaR model, option pricing model,
     * confidence level, time horizon and user-selected portfolio to use.
     * 
     * @param selectedPortfolio
     * @param selectedModel
     * @param confidenceLevel
     * @param timeHorizon
     */
    public SimulationSetup( Portfolio selectedPortfolio, String selectedModel,
                            int confidenceLevel,
                            int timeHorizon )
    {
        this.portfolio = selectedPortfolio;
        this.model = selectedModel;
        this.confidenceLevel = confidenceLevel;
        this.timeHorizon = timeHorizon;
    }

    /**
     * @return the user-selected VaR model
     */
    public String getModel()
    {
        return model;
    }

    /**
     * @return the confidenceLevel to getOptionPrice VaR at.
     */
    public int getConfidenceLevel()
    {
        return confidenceLevel;
    }

    /**
     * @return the timeHorizon to getOptionPrice VaR over.
     */
    public int getTimeHorizon()
    {
        return timeHorizon;
    }

}
