package com.rp.risk_management.util.date;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SimpleDate implements Comparable<SimpleDate>
{
    public static final SimpleDateFormat simpleDateFormat_ = new SimpleDateFormat("yyyyMMdd");

    private Object lock_ = new Object();
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

    public SimpleDate(Calendar calendar) {
        this(calendar.getTime());
    }

    public SimpleDate(int yyyy, int mm, int dd)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,yyyy);
        calendar.set(Calendar.MONTH,mm-1);
        calendar.set(Calendar.DAY_OF_MONTH,dd);

        // strip off time
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        date_ = calendar.getTime();
    }

    @Override
    public int compareTo(SimpleDate o) {
        return o.date_.compareTo(this.date_);
    }

    public Date getDate() {
        return date_;
    }

    public Calendar getCalendar()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date_);

        return calendar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleDate)) return false;

        SimpleDate that = (SimpleDate) o;

        if (!simpleDateFormat_.equals(that.simpleDateFormat_)) return false;
        return date_.equals(that.date_);

    }

    @Override
    public int hashCode() {
        int result = simpleDateFormat_.hashCode();
        result = 31 * result + date_.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SimpleDate{" +
                "date_=" + getyyyymmdd() +
                '}';
    }

    public String getyyyymmdd()
    {
        synchronized (lock_)
        {
            return simpleDateFormat_.format(date_);
        }
    }
}
