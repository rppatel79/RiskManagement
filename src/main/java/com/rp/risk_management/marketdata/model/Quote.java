package com.rp.risk_management.marketdata.model;

import com.rp.risk_management.util.date.SimpleDate;

import java.math.BigDecimal;

public class Quote
{
    private final Stock stock_;
    private final SimpleDate simpleDate_;
    private final BigDecimal open_;
    private final BigDecimal low_;
    private final BigDecimal high_;
    private final BigDecimal close_;
    private final BigDecimal adjClose_;
    private final Long volume_;

    public Quote(Stock stock, SimpleDate simpleDate, BigDecimal open, BigDecimal low, BigDecimal high, BigDecimal close, BigDecimal adjClose, Long volume)
    {
        stock_ = stock;
        simpleDate_ = simpleDate;
        open_ = open;
        low_ = low;
        high_ = high;
        close_ = close;
        adjClose_ = adjClose;
        volume_ = volume;
    }

    public Stock getStock() {
        return stock_;
    }

    public SimpleDate getSimpleDate() {
        return simpleDate_;
    }

    public BigDecimal getOpen() {
        return open_;
    }

    public BigDecimal getLow() {
        return low_;
    }

    public BigDecimal getHigh() {
        return high_;
    }

    public BigDecimal getClose() {
        return close_;
    }

    public BigDecimal getAdjClose() {
        return adjClose_;
    }

    public Long getVolume() {
        return volume_;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "stock_=" + stock_ +
                ", simpleDate_=" + simpleDate_ +
                ", open_=" + open_ +
                ", low_=" + low_ +
                ", high_=" + high_ +
                ", close_=" + close_ +
                ", adjClose_=" + adjClose_ +
                ", volume_=" + volume_ +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quote)) return false;

        Quote quote = (Quote) o;

        if (!stock_.equals(quote.stock_)) return false;
        if (!simpleDate_.equals(quote.simpleDate_)) return false;
        if (!open_.equals(quote.open_)) return false;
        if (!low_.equals(quote.low_)) return false;
        if (!high_.equals(quote.high_)) return false;
        if (!close_.equals(quote.close_)) return false;
        if (!adjClose_.equals(quote.adjClose_)) return false;
        return volume_.equals(quote.volume_);

    }

    @Override
    public int hashCode() {
        int result = stock_.hashCode();
        result = 31 * result + simpleDate_.hashCode();
        result = 31 * result + open_.hashCode();
        result = 31 * result + low_.hashCode();
        result = 31 * result + high_.hashCode();
        result = 31 * result + close_.hashCode();
        result = 31 * result + adjClose_.hashCode();
        result = 31 * result + volume_.hashCode();
        return result;
    }
}
