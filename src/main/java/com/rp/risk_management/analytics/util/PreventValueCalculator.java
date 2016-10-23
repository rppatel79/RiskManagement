package com.rp.risk_management.analytics.util;

import com.rp.risk_management.analytics.portfolio.VarUtils;

public class PreventValueCalculator {
    /**
     * computes the discounted value of an option in relation to the following variables
     *
     * @param meanValue avg exercise value of option
     * @param interest
     * @param timeToMaturity
     * @return discounted value
     */
    public static double getDiscountedValue( double meanValue, double interest, double timeToMaturity )
    {
        // discount average to today = value of the option
        // PV = C / (1+interest)^numberofperiodsofinterest
        // e.g. 1000 in 5 years at 10%, PV = 1000 / (1+0.10)^5
        // if in days, convert to years
        timeToMaturity = timeToMaturity / VarUtils.DAYS_IN_YEAR;
        double denominator = Math.pow( ( 1 + interest ), timeToMaturity );
        double valueOfOption = meanValue / denominator;
        return valueOfOption;
    }
}
