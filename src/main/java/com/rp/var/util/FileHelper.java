package com.rp.var.util;

import com.rp.var.analytics.portfolio.VarUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHelper
{
    private static final Logger logger_ = Logger.getLogger(FileHelper.class);

    /** Used to split the stock price data file contents into a array of strings. */
    private static final String COMMA            = ",";

    /**
     * Uses {@code getReturnsFromFile(File file)} to iteratively extract files from a list and
     * generate a list of returns for respective files.
     *
     * @param files List of csv files to parse for returns.
     * @return List of arrays containing returns for each input file.
     */
    public static List<double[]> getReturnsFromFiles(List<File> files )
    {
        List<double[]> returns = new ArrayList<double[]>();
        for( File stockData : files )
        {
            returns.add( VarUtils.computeDailyReturns(getClosingPrices( stockData ) ));
        }

        return returns;
    }

    /**
     * Saves the series of closing prices from the historical stock data into a list of prices.
     * @param file historical stock data
     * @return list of closing prices
     */
    public static List<Double> getClosingPrices( File file )
    {
        List<Double> closePrices = new ArrayList<Double>();
        try
        {
            BufferedReader reader = new BufferedReader( new FileReader( file ) );
            String nextLine;
            String comma = COMMA;
            int counter = 0; // skip storing column heading
            int closePriceColumn = 0;
            while( ( nextLine = reader.readLine() ) != null )
            {
                if( counter == 0 )
                {
                    String[] line = nextLine.split( comma );
                    for( int i = 0 ; i < line.length ; i++ )
                    {
                        if( line[i].contains( "Close" ) )
                        {
                            closePriceColumn = i;
                            break;
                        }
                    }
                }
                else
                {
                    String[] line = nextLine.split( comma );
                    closePrices.add( Double.parseDouble( line[closePriceColumn] ) );
                }
                counter++;
            }
            reader.close();
        }
        catch( IOException e )
        {
            logger_.error( "File" + file.getName() + " has a problem.",e);
            throw new IllegalArgumentException(e);
        }
        return closePrices;
    }
}
