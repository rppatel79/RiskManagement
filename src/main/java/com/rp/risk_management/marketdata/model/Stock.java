package com.rp.risk_management.marketdata.model;

public class Stock
{
    private final String stock_;

    public Stock(String stock) {
        stock_ = stock;
    }

    public String getStock() {
        return stock_;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "stock_='" + stock_ + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stock)) return false;

        Stock stock = (Stock) o;

        return stock_.equals(stock.stock_);

    }

    @Override
    public int hashCode() {
        return stock_.hashCode();
    }
}
