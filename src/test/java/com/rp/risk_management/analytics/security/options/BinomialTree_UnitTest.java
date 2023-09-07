package com.rp.risk_management.analytics.security.options;

import com.rp.risk_management.model.Option;
import junit.framework.TestCase;
import org.junit.Assert;

public class BinomialTree_UnitTest extends TestCase
{
    public void setUp() {
    }

    public void testShouldCreateBinomialTreeCorrectly()
    {
        double t = 5.0/12.0;
        OptionPricer bt = new BinomialTree( 50, 50, t, 0.4, 0.1, Option.OptionType.Put, Option.OptionStyle.American );
        double optionPrice =bt.getOptionPrice();
        Assert.assertEquals(4.1835, optionPrice, 0.0001);
    }
}
