package com.rp.risk_management.analytics.portfolio;

import com.rp.risk_management.model.Portfolio;

/**
 * Combines the user-selected VaR model_, option pricing model_, confidence level, time horizon and
 * user-selected portfolio_ to use when computing VaR.
 */
public class SimulationSetup
{
    /** The model_ to use for computing VaR. */
    private final String model_;
    /** The confidence level to getOptionPrice VaR at. */
    private final int confidenceLevel_;
    /** The time horizon to getOptionPrice VaR over. */
    private final int timeHorizon_;
    /** The portfolio_ to getOptionPrice VaR for. */
    private final Portfolio portfolio_;

    /**
     * @return user-selected portfolio_
     */
    public Portfolio getPortfolio()
    {
        return portfolio_;
    }

    /**
     * Creates a simulation setup using the user-selected VaR model_, option pricing model_,
     * confidence level, time horizon and user-selected portfolio_ to use.
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
        this.portfolio_ = selectedPortfolio;
        this.model_ = selectedModel;
        this.confidenceLevel_ = confidenceLevel;
        this.timeHorizon_ = timeHorizon;
    }

    /**
     * @return the user-selected VaR model_
     */
    public String getModel()
    {
        return model_;
    }

    /**
     * @return the confidenceLevel_ to getOptionPrice VaR at.
     */
    public int getConfidenceLevel()
    {
        return confidenceLevel_;
    }

    /**
     * @return the timeHorizon_ to getOptionPrice VaR over.
     */
    public int getTimeHorizon()
    {
        return timeHorizon_;
    }

}
