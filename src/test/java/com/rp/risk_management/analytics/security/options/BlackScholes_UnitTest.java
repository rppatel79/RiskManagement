package com.rp.risk_management.analytics.security.options;

import com.rp.risk_management.model.Option;
import junit.framework.TestCase;

public class BlackScholes_UnitTest extends TestCase
{
    public void setUp() throws Exception
    {

    }

    public void testShouldReturnCorrectCallPrice()
    {
        BlackScholes bs = new BlackScholes(Option.OptionType.Call, 80, 100, 0.5, 0.07, 0.03 * Math.sqrt( 252 ) );
        double callPrice = bs.getOptionPrice( );
        assertEquals( 5.29, callPrice, 0.01 );
    }

    public void testShouldReturnCorrectPutPrice()
    {
        BlackScholes bs = new BlackScholes(Option.OptionType.Put, 80, 100, 0.5, 0.07, 0.03 * Math.sqrt( 252 ) );
        double putPrice = bs.getOptionPrice( );
        assertEquals( 21.85, putPrice, 0.01 );
    }

}
