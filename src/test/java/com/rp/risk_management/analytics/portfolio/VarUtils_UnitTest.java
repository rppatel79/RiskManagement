package com.rp.risk_management.analytics.portfolio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import com.rp.risk_management.marketdata.api.MarketDataApi;
import com.rp.risk_management.marketdata.api.YahooMarketDataApi;
import com.rp.risk_management.marketdata.model.Quote;
import com.rp.risk_management.marketdata.model.Stock;
import com.rp.risk_management.util.FileHelper;
import com.rp.risk_management.util.ResourceHelper;
import com.rp.risk_management.util.date.SimpleDate;
import org.junit.Test;

public class VarUtils_UnitTest
{
    private final double delta = 0.0001;

    @Test
    public void shouldReturnCorrectNumberOfReturnsFromMSFTFile() throws Exception
    {
        MarketDataApi marketDataApi = new YahooMarketDataApi();
        List<Double> closingPrices = FileHelper.getClosingPrices(marketDataApi.getMarketData(new Stock("MSFT"),new SimpleDate(2013,8,13),new SimpleDate(2013,11,15)));
        double[] dailyReturns=VarUtils.computeDailyReturns(closingPrices);
        assertTrue( closingPrices.size()-1 == dailyReturns.length );
    }

    @Test
    public void shouldReturnCorrectSquareOfNumber()
    {
        double sent = 1;
        double received = VarUtils.square( sent );
        assertTrue( received == sent );
        sent = 2;
        received = VarUtils.square( sent );
        assertTrue( received == sent * sent );
        sent = 20;
        received = VarUtils.square( sent );
        assertTrue( received == sent * sent );
    }

    @Test
    public void shouldReturnCorrectSquareRootOfNumber()
    {
        double sent = 1;
        double received = VarUtils.root( sent );
        assertTrue( received*received == sent );
        sent = 4;
        received = VarUtils.root( sent );
        assertTrue( received*received == sent );
        sent = 625;
        received = VarUtils.root( sent );
        assertTrue( received*received == sent );
    }

    @Test
    public void shouldReturnCorrectRoundedNumber()
    {
        double sent = 1.03;
        double received = VarUtils.round( sent );
        assertTrue( received == 1 );
        sent = 2.51;
        received = VarUtils.round( sent );
        assertTrue( received == 3.0 );
        sent = 20.496;
        received = VarUtils.round( sent );
        assertTrue( received == 20 );
    }

    @Test
    public void shouldComputeCorrectVolatilityUsingStandardFormula() throws Exception
    {
        MarketDataApi marketDataApi = new YahooMarketDataApi();
        List<Quote> quotes = marketDataApi.getMarketData(new Stock("GOOG"),new SimpleDate(2013,9,19),new SimpleDate(2013,10,18));
        double[] returnsFromFile = VarUtils.computeDailyReturns(FileHelper.getClosingPrices(quotes));
        double computedVolatility = VarUtils.computeVolatility_Standard( returnsFromFile );
        double expectedVolatility = 0.0298958;
        assertEquals( expectedVolatility, computedVolatility, delta );
    }

    @Test
    public void shouldComputeCorrectVolatilityUsingEWMA() throws Exception
    {
        MarketDataApi marketDataApi = new YahooMarketDataApi();
        List<Quote> quotes = marketDataApi.getMarketData(new Stock("GOOG"),new SimpleDate(2013,9,19),new SimpleDate(2013,10,18));
        double[] returns = VarUtils.computeDailyReturns(FileHelper.getClosingPrices(quotes) );
        double computedVolatility = VarUtils.computeVolatility_EWMA( returns );
        double expectedVolatility = 0.06011;
        assertEquals( expectedVolatility, computedVolatility, delta );
    }

    @Test
    public void shouldComputeCorrectVolatilityUsingGARCH()throws Exception
    {
        MarketDataApi marketDataApi = new YahooMarketDataApi();
        List<Quote> quotes = marketDataApi.getMarketData(new Stock("GOOG"),new SimpleDate(2013,9,19),new SimpleDate(2013,10,18));
        double[] returnsFromFile = VarUtils.computeDailyReturns(FileHelper.getClosingPrices(quotes));
        double computedVolatility = VarUtils.computeVolatility_GARCH( returnsFromFile );
        double expectedVolatility = 0.04019688;
        assertEquals( expectedVolatility, computedVolatility, delta );
    }

    /**
     * <a
     * href="http://www.investopedia.com/articles/financial-theory/11/calculating-covariance.asp">
     * Source</a>
     */
    @Test
    public void shouldReturnExpectedCovarianceForReturns()
    {
        double[] returns1 = { 1.1, 1.7, 2.1, 1.4, 0.2 };
        double[] returns2 = { 3, 4.2, 4.9, 4.1, 2.5 };
        double covariance = VarUtils.getCovariance( returns1, returns2 );
        assertTrue( covariance == 0.665 );
    }

    @Test
    public void shouldReturnCorrectCovarianceUsingManualCalculation()
    {
        double[] returns = { 1, 2, 3 };
        double covariance = VarUtils.getCovarianceManually( returns, returns );
        assertEquals( covariance, VarUtils.getCovariance( returns, returns ), 0.01 );

        double[] returns1 = { 1, 2, 3, 2, 1 };
        double[] returns2 = { 1, 2, 3, 2, 1, 2 };
        covariance = VarUtils.getCovarianceManually( returns1, returns2 );
        assertEquals( covariance, VarUtils.getCovariance( returns1, returns2 ),
                      0.01 );

        double[] oppReturns = { 3, 2, 1, 2, 3 };
        covariance = VarUtils.getCovarianceManually( returns, oppReturns );
        assertEquals( covariance, VarUtils.getCovariance( returns, oppReturns ),
                      0.01 );
    }

    @Test
    public void shouldDecomposeMatrix()
    {
        // values from online page
        // http://www.sitmo.com/article/generating-correlated-random-numbers/

        double[][] inputMatrix = { { 1, 0.6, 0.3 }, { 0.6, 1, 0.5 },
                { 0.3, 0.5, 1 } };
        double[][] decomposedMatrix = VarUtils.decomposeMatrix( inputMatrix );
        double[][] expectedDecomposedMatrix = { { 1, 0.0, 0.0 },
                { 0.6, 0.8, 0.0 }, { 0.3, 0.4, 0.866 } };

        Arrays.deepEquals( decomposedMatrix, expectedDecomposedMatrix );

        // tests
        compareDoubleArrays(decomposedMatrix, expectedDecomposedMatrix, delta);
    }

    private static void compareDoubleArrays(double[][] decomposedMatrix, double[][] expectedDecomposedMatrix, double delta) {
        assertEquals(expectedDecomposedMatrix.length,decomposedMatrix.length);
        for (int i =0; i < expectedDecomposedMatrix.length ; i++)
        {
            double[] expectedRow =expectedDecomposedMatrix[i];
            double[] decomposedRow =decomposedMatrix[i];


            assertEquals(expectedRow.length,decomposedRow.length);

            for (int j = 0 ; j < expectedRow.length ; j++)
            {
                assertEquals(expectedRow[j],decomposedRow[j],delta);
            }
        }
    }

    @Test
    public void shouldGetCorrectPercentileFromSeries()
    {
        double[] data = new double[100];
        for( int i = 0 ; i < 100 ; i++ )
        {
            data[i] = i + 1;
        }
        int percentile = 10;
        double received = VarUtils.getPercentile( data, percentile );
        assertEquals( received, 90.0, 1.0 );
    }

    @Test
    public void shouldGetCorrectReturnsOverHorizon()
    {
        double[] returns = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        @SuppressWarnings("unused")
        double[] returnsOverVarHorizon = VarUtils.getReturnsOverVarHorizon( returns, 2 );
        // tested by hand
    }

}
