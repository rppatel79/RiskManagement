package com.rp.risk_management.analytics.security.options.monte_carlo;

public class MonteCarloResults {
    public final double finalValueOfOption_;
    public final double minValueOfOption_;

    public MonteCarloResults(double finalValueOfOption, double minValueOfOption)
    {
        finalValueOfOption_ = finalValueOfOption;
        minValueOfOption_ = minValueOfOption;
    }
}
