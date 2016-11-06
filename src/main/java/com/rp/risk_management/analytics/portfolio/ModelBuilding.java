/**
 * 
 */
package com.rp.risk_management.analytics.portfolio;

import com.rp.risk_management.marketdata.model.Quote;
import com.rp.risk_management.model.Portfolio;
import com.rp.risk_management.util.FileHelper;
import com.rp.risk_management.util.model.PortfolioUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class implements the Model-Building Value at Risk model. 
 */
public class ModelBuilding
{
    /** List of investments made. */
    private List<Double> portfolioValues;
    /** List of historical price quotes for assets in same order to investments. */
    private List<List<Quote>> allStockQuotes_;
    /** The confidence at which to getOptionPrice VaR. */
    private int               confidence;
    /** The time period to getOptionPrice VaR over. */
    private int               timePeriod;
    /** The number of standard deviations our risk is unlikely to exceed, affected by the confidence level. */
    private double            zDelta;
    /** The number of assets currently held in this model. */
    private int               numberOfStocks;

    /**
     * Initialises a Model-Building VaR model using a portfolio.
     * @param p portfolio to getOptionPrice VaR for
     * @param confidence
     * @param timePeriod
     */
    public ModelBuilding(Portfolio p, int confidence, int timePeriod )
    {
        this.confidence = confidence;
        this.timePeriod = timePeriod;
        this.portfolioValues = PortfolioUtil.getAssetInvestment(p);
        this.numberOfStocks = portfolioValues.size();
        this.allStockQuotes_ = PortfolioUtil.getStockQuotes(p);
        computeZDelta();
    }

    /**
     * Method used to getOptionPrice VaR for multiple stocks by calculating the variance of the portfolio.
     * @return VaR
     */
    public double computeForMultipleStocks()
    {
        ArrayList<double[]> returnList = new ArrayList<double[]>();

        for( List<Quote> stockQuotes : allStockQuotes_ )
        {
            double[] returnsFromFile = VarUtils.computeDailyReturns(FileHelper.getClosingPrices( stockQuotes ));
            returnList.add( returnsFromFile );
        }

        double portfolioVariance = getPortfolioVariance( returnList );

        // VaR = stdDevUpperBound * volatilityOfPortfolio *
        // Math.sqrt(numberOfDays);
        double VaR = zDelta * VarUtils.root( portfolioVariance )
                     * VarUtils.root( timePeriod );

        return VaR;
    }

    /**
     * Computes the variance of a portfolio of assets by generating a covariance matrix.
     * @param returnList a list of series of returns from stock file data
     * @return total variance of the portfolio.
     */
    private double getPortfolioVariance( ArrayList<double[]> returnList )
    {
        double[][] covarianceMatrix = VarUtils.generateCovarianceMatrix(
                                                                         returnList, numberOfStocks );

        double portfolioVariance = 0.0;

        for( int i = 0 ; i < covarianceMatrix.length ; i++ )
        {
            for( int j = 0 ; j < covarianceMatrix[0].length ; j++ )
            {
                // value_stock1 * value_stock2 * covariance_stock1stock2
                double x = portfolioValues.get( i ) * portfolioValues.get( j )
                           * covarianceMatrix[i][j];
                portfolioVariance += x;
            }
        }
        return portfolioVariance;
    }

    /**
     * For one stock in portfolio.
     * Use the returns until current day to estimate volatility and thus the VaR for next day.
     * @param returns
     * @param numberOfDaysToTest
     * @return
     */
    public double[] computeForBackTesting( double[] returns, int numberOfDaysToTest )
    {
        int numberOfReturnsToUse = returns.length - 1 - numberOfDaysToTest;
        double portfolioValue = portfolioValues.get( 0 );
        double[] estimations = new double[numberOfDaysToTest];
        // calculate one-day VaR for day+1 -> numberOfDaysToTest from returns to date
        for( int day = 0 ; day < numberOfDaysToTest ; day++ )
        {
            double[] returnsToUse = Arrays.copyOf( returns, numberOfReturnsToUse );
            double volatility = VarUtils.computeVolatility_GARCH( returnsToUse );
            estimations[day] = getVaR( volatility, portfolioValue );
            numberOfReturnsToUse++;
        }

        return estimations;
    }
    
    /**
     * Computes VaR using the volatility and value of the portfolio.
     * @param volatility of the portfolio
     * @param portfolioValue total value of the portfolio
     * @return VaR estimate using these parameters
     */
    private double getVaR( double volatility, double portfolioValue )
    {
        double VaR = 0.0;
        double stdDevDailyValueChange = volatility * portfolioValue;
        double oneDayVaR = zDelta * stdDevDailyValueChange;
        VaR = oneDayVaR * Math.sqrt( timePeriod );
        return VaR;
    }

    /**
     * Computes Value at Risk for two stocks using pre-defined volatilities and
     * covariances.
     * 
     * @param value1
     * @param value2
     * @param dailyVolatility1
     * @param dailyVolatility2
     * @param covariance
     * @return
     */
    public double computeForTwoStocks( double dailyVolatility1,
                                       double dailyVolatility2, double covariance )
    {
        double stdDevChange1 = portfolioValues.get( 0 ) * dailyVolatility1;
        double stdDevChange2 = portfolioValues.get( 1 ) * dailyVolatility2;

        double VaR = VarUtils.root( VarUtils.square( stdDevChange1 )
                                    + VarUtils.square( stdDevChange2 ) + 2 * covariance
                                    * stdDevChange1 * stdDevChange2 )
                     * zDelta * VarUtils.root( timePeriod );
        return VaR;
    }
    
    /**
     * Computes the zDelta for the confidence level provided.
     */
    private void computeZDelta()
    {
        switch( confidence )
        {
            case 99:
                zDelta = 2.33;
                break;
            case 98:
                zDelta = 2.05;
                break;
            case 97:
                zDelta = 1.88;
                break;
            case 96:
                zDelta = 1.75;
                break;
            case 95:
                zDelta = 1.65;
                break;
            case 90:
                zDelta = 1.29;
                break;
            case 85:
                zDelta = 1.04;
                break;
            case 80:
                zDelta = 0.84;
                break;
            case 75:
                zDelta = 0.68;
                break;
            default:
                throw new IllegalArgumentException("Unable to convert confidence of ["+confidence+"] to zDelta");
        }

    }
   
}
