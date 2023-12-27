package com.github.ttran17.day23;

import java.util.List;

public class Mountain
{
    protected final int nRows;
    protected final int nCols;
    protected final char[][] layout;

    protected int startRow;
    protected int startCol;

    protected final int endRow;
    protected final int endCol;

    protected Mountain( List<String> lines )
    {
        nRows = lines.size( );
        nCols = lines.get( 0 ).trim( ).length( );

        layout = new char[nRows][nCols];

        for ( int row = 0; row < nRows; row++ )
        {
            String line = lines.get( row );
            for ( int col = 0; col < nCols; col++ )
            {
                layout[row][col] = line.charAt( col );
            }
        }

        startRow = 0;
        startCol = lines.get( startRow ).indexOf( '.' );

        endRow = nRows - 1;
        endCol = lines.get( endRow ).indexOf( '.' );
    }

    protected boolean isValidLocation( int row, int col )
    {
        return row > -1 && row < nRows && col > -1 && col < nCols && layout[row][col] != '#';
    }
}
