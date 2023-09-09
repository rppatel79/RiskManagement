package com.rp.risk_management.analytics.security.options;

import com.rp.risk_management.model.Option;
import junit.framework.TestCase;
import org.junit.Assert;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

public class CoxRossRubinstein_UnitTest extends TestCase {
    public void testShouldCreateBinomialTreeCorrectly() {
        //Hull SSM (2014): page 142, Problem 13.17: American put option, 2 time steps
        // S_0 = 1500, K = 1480, τ = 1, σ = 0.18, r = 0.04, q = 0.025
        Option option = new Option(1500.0,
                -1,
                1480,
                -1,
                -1,
                1,
                Collections.emptyList(),
                Option.OptionStyle.American,
                Option.OptionType.Put);

        OptionPricer bt = new CoxRossRubinsteinPricer(2, option, 0.04, 0.18, 0.025);
        double optionPrice = bt.getOptionPrice();
        Assert.assertEquals(78.41, optionPrice, 0.01);
    }

    public void testShouldCreateBinomialTreeCorrectly2() {
        // i created this test to compare with
        Option option = new Option(408.43,
                -1,
                410,
                -1,
                -1,
                (int) LocalDate.of(2023, 9, 7).until(LocalDate.of(2023, 9, 15), ChronoUnit.DAYS),
                Collections.emptyList(),
                Option.OptionStyle.American,
                Option.OptionType.Put);

        OptionPricer bt3 = new CoxRossRubinsteinPricer(100, option, 0.05, 0.111, 0.0);


        Assert.assertEquals(3.327538691067, bt3.getOptionPrice(), 0.000000001);
    }
}
