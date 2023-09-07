package com.rp.risk_management.analytics.security.options;

import com.rp.risk_management.model.Option;
import junit.framework.TestCase;

public class BlackScholes_UnitTest extends TestCase
{
    public void setUp() throws Exception
    {

    }

    public void testShouldReturnCorrectCallPrice() {
        BlackScholes bs = new BlackScholes(Option.OptionType.Call, 80, 100, 0.5, 0.07, 0.03 * Math.sqrt(252));
        double callPrice = bs.getOptionPrice();
        assertEquals(5.29, callPrice, 0.01);
    }


    public void testShouldReturnCorrectCallPrice2() {
        //Hull SSM (2014), page 166, Problem 15.13: European call option
        // S_0 = 52, K = 50, τ = 0.25, σ = 0.3, r = 0.12, q = 0
        BlackScholes bs = new BlackScholes(Option.OptionType.Call, 52, 50, 0.25, 0.12, 0.3);
        double callPrice = bs.getOptionPrice();
        assertEquals(5.06, callPrice, 0.01);
    }

    public void testShouldReturnCorrectPutPrice() {
        BlackScholes bs = new BlackScholes(Option.OptionType.Put, 80, 100, 0.5, 0.07, 0.03 * Math.sqrt(252));
        double putPrice = bs.getOptionPrice();
        assertEquals(21.85, putPrice, 0.01);
    }

}
