package com.github.ttran17.day17;

import java.util.List;

public class City
{
    protected final int nRows;
    protected final int nCols;

    protected final int[][] layout;

    protected City( List<String> lines )
    {
        nRows = lines.size( );
        nCols = lines.get( 0 ).trim( ).length( );

        layout = new int[nRows][nCols];

        for ( int row = 0; row < nRows; row++ )
        {
            String line = lines.get( row );
            for ( int col = 0; col < nCols; col++ )
            {
                layout[row][col] = Integer.parseInt( Character.toString( line.charAt( col ) ) );
            }
        }
    }

    protected boolean isValidLocation( int row, int col )
    {
        return row > -1 && row < nRows && col > -1 && col < nCols;
    }
}
