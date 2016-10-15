package com.rp.var.analytics.security.options;

import com.rp.var.analytics.portfolio.VarUtils;
import com.rp.var.model.Option;

/**
 * A class to implement the Black-Scholes formula for calculating option prices.
 * 
 * @author Nishant
 * 
 */
public class BlackScholes implements OptionPricer
{   
    /** Identifier of a call option.
     * @deprecated */
    public static final int CALL = 0;
    /** Identifier of a put option.
     * @deprecated  */
    public static final int PUT  = 1;

    private final int flag;
    private final double S;
    private final double X;
    private final double T;
    private final double r;
    private final double v;

    private final double computedOptionPrice;
    /**
     * Computes the price of a European Call/Put option based on the parameters specified.
     *
     * @param flag
     *            0 for call, 1 for put
     * @param S
     *            Starting price of the option
     * @param X
     *            Strike price of the option
     * @param T
     *            Time left to maturity
     * @param r
     *            Risk-free interest rate
     * @param v
     *            Volatility of the option
     * @return
     */
    public BlackScholes(Option.OptionType optionType, double S, double X, double T, double r,
                        double v )
    {
        this.flag=optionType == Option.OptionType.Call? 0:1;
        this.S=S;
        this.X=X;
        this.T=T;
        this.r=r;
        this.v=v;

        computedOptionPrice = compute(flag, S, X, T, r,v);
    }

    public BlackScholes(Option option,double rate,double volatility)
    {
        this(option.getOptionType(),option.getInitialStockPrice(),option.getStrike(),option.getTimeToMaturity(),rate, volatility);
    }

    public double getOptionPrice( )
    {
        return computedOptionPrice;
    }

    private static final double compute(int flag, double S, double X, double T, double r,
                                        double v )
    {
        double d1, d2;

        d1 = ( Math.log( S / X ) + ( r + v * v / 2 ) * T ) / ( v * Math.sqrt( T ) );
        d2 = d1 - v * Math.sqrt( T );

        if( flag == CALL )
        {
            return S * VarUtils.CNDF( d1 ) - X * Math.exp( -r * T ) * VarUtils.CNDF( d2 );
        }
        else if( flag == PUT )
        {
            return X * Math.exp( -r * T ) * VarUtils.CNDF( -d2 ) - S * VarUtils.CNDF( -d1 );
        }
        else
            return -1.0;
    }
}
