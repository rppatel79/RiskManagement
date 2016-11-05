package com.rp.risk_management.util.date;

import java.util.Calendar;

public class SimpleDateHelper
{
    private SimpleDateHelper(){}

    public static SimpleDate addDays(SimpleDate simpleDate,int day)
    {
        Calendar cal=simpleDate.getCalendar();
        cal.set(Calendar.DATE,simpleDate.getCalendar().get(Calendar.DATE)+day);
        return new SimpleDate(cal);
    }
}
