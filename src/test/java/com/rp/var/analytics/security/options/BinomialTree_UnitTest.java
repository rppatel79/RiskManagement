package com.rp.var.analytics.security.options;

import com.rp.var.analytics.portfolio.VarUtils;
import com.rp.var.analytics.security.options.OptionPricer;
import com.rp.var.model.Option;
import junit.framework.TestCase;
import org.junit.Assert;

import com.rp.var.analytics.security.options.BinomialTree;

public class BinomialTree_UnitTest extends TestCase
{
    public void setUp() throws Exception
    {
    }

    public void testShouldCreateBinomialTreeCorrectly()
    {
        double t = 5.0/12.0;
        OptionPricer bt = new BinomialTree( 50, 50, t, 0.4, 0.1, Option.OptionType.Put, Option.OptionStyle.American );
        double optionPrice =bt.getOptionPrice();
        Assert.assertEquals(4.0873,optionPrice,0.0001);
    }

}
