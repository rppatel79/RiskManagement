package com.rp.risk_management.analytics.security.options;

import com.rp.risk_management.analytics.portfolio.VarUtils;
import com.rp.risk_management.model.Option;

/**
 * A class to implement the Black-Scholes formula for calculating option prices.
 * 
 * @author Nishant
 * 
 */
public class BlackScholes implements OptionPricer
{   
    private final double S;
    private final double X;
    private final double T;
    private final double r;
    private final double v;

    private final double computedOptionPrice;
    /**
     * Computes the price of a European Call/Put option based on the parameters specified.
     *
     * @param optionType call or put
     * @param S Starting price of the option
     * @param X  Strike price of the option
     * @param T Time left to maturity
     * @param r Risk-free interest rate
     * @param v Volatility of the option
     */
    public BlackScholes(Option.OptionType optionType, double S, double X, double T, double r,
                        double v )
    {
        this.S=S;
        this.X=X;
        this.T=T;
        this.r=r;
        this.v=v;

        computedOptionPrice = compute(optionType, S, X, T, r,v);
    }

    public BlackScholes(Option option,double rate,double volatility)
    {
        this(option.getOptionType(),option.getInitialStockPrice(),option.getStrike(),option.getTimeToMaturity(),rate, volatility);
    }

    @Override
    public double getOptionPrice( )
    {
        return computedOptionPrice;
    }

    private static final double compute(Option.OptionType optionType, double S, double X, double T, double r,
                                        double v )
    {
        double d1, d2;

        d1 = ( Math.log( S / X ) + ( r + v * v / 2 ) * T ) / ( v * Math.sqrt( T ) );
        d2 = d1 - v * Math.sqrt( T );

        switch (optionType)
        {
            case Call:
            {
                return S * VarUtils.CNDF( d1 ) - X * Math.exp( -r * T ) * VarUtils.CNDF( d2 );
            }
            case Put:
            {
                return X * Math.exp( -r * T ) * VarUtils.CNDF( -d2 ) - S * VarUtils.CNDF( -d1 );
            }
            default:
            {
                throw new IllegalArgumentException("Unknown option type ["+optionType+"]");
            }
        }
    }
}
