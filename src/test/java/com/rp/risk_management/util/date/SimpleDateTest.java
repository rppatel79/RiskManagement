package com.rp.risk_management.util.date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

public class SimpleDateTest {
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test() throws Exception {
        SimpleDate simpleDate1 = new SimpleDate(2015, 11, 1);
        Calendar calendar = simpleDate1.getCalendar();

        Assert.assertEquals(simpleDate1, new SimpleDate(calendar));
        Assert.assertEquals(simpleDate1, new SimpleDate(calendar.getTime()));
    }
}
