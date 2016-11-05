package com.rp.risk_management.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SimpleDate implements Comparable<SimpleDate>
{
    private SimpleDateFormat simpleDateFormat_ = new SimpleDateFormat("yyyyMMdd");

    private final Date date_;
    public SimpleDate(Date date)
    {
        try {
            String formatedDate = simpleDateFormat_.format(date);
            date_ = simpleDateFormat_.parse(formatedDate);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unable to format date");
        }
    }

    public SimpleDate(int yyyy, int mm, int dd)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,yyyy);
        calendar.set(Calendar.MONTH,mm);
        calendar.set(Calendar.DATE,dd);

        date_ = calendar.getTime();
    }

    @Override
    public int compareTo(SimpleDate o) {
        return o.date_.compareTo(this.date_);
    }
}
