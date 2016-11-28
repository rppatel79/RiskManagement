package com.rp.risk_management.marketdata.api;


import com.rp.risk_management.marketdata.model.Quote;
import com.rp.risk_management.marketdata.model.Stock;
import com.rp.risk_management.model.Position;
import com.rp.risk_management.util.date.SimpleDate;

import java.util.List;

public interface MarketDataApi
{
    public enum Interval{DAILY,WEEKLY,MONTHLY}

    public Quote getMarketData(Stock symbol, SimpleDate date)throws Exception;
    public List<Quote> getMarketData(Position position)throws Exception;
    public List<Quote> getMarketData(Stock stock, SimpleDate fromDate, SimpleDate toDate) throws Exception;
    public List<Quote> getMarketData(Stock stock,SimpleDate fromDate, SimpleDate toDate, Interval interval) throws Exception;
}
