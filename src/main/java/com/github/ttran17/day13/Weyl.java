package com.github.ttran17.day13;

import java.util.ArrayList;
import java.util.List;

public class Weyl
{
    protected final List<char[][]> grids = new ArrayList<>( );

    protected static long countReflections( List<String> lines )
    {
        long sum = 0;

        Weyl weyl = consume( lines );
        List<Decimal> decimals = toDecimal( weyl );
        for ( Decimal decimal : decimals )
        {
            long rows = findReflection( decimal.rows );
            if ( rows > 0 )
            {
                sum += 100 * rows;
            }

            long cols = findReflection( decimal.cols );
            if ( cols > 0 )
            {
                sum += cols;
            }
        }

        return sum;
    }

    protected static Weyl consume( List<String> lines )
    {
        Weyl weyl = new Weyl( );

        List<char[]> charLine = new ArrayList<>( );
        for ( String line : lines )
        {
            if ( line.trim( ).isEmpty( ) )
            {
                char[][] grid = new char[charLine.size( )][charLine.get( 0 ).length];
                for ( int k = 0; k < charLine.size( ); k++ )
                {
                    grid[k] = charLine.get( k );
                }
                weyl.grids.add( grid );

                charLine = new ArrayList<>( );
                continue;
            }

            char[] chars = new char[line.length( )];
            for ( int i = 0; i < line.length( ); i++ )
            {
                chars[i] = line.charAt( i );
            }
            charLine.add( chars );
        }

        char[][] grid = new char[charLine.size( )][charLine.get( 0 ).length];
        for ( int k = 0; k < charLine.size( ); k++ )
        {
            grid[k] = charLine.get( k );
        }
        weyl.grids.add( grid );

        return weyl;
    }

    protected static List<Decimal> toDecimal( Weyl weyl )
    {
        List<Decimal> decimals = new ArrayList<>( );
        for ( char[][] grid : weyl.grids )
        {
            int nRows = grid.length;
            int nCols = grid[0].length;

            long[] rowDecimals = new long[nRows];
            for ( int row = 0; row < nRows; row++ )
            {
                StringBuilder builder = new StringBuilder( );
                for ( int col = 0; col < nCols; col++ )
                {
                    char c = grid[row][col];
                    String s = c == '#' ? "1" : "0";
                    builder.append( s );
                }
                rowDecimals[row] = Long.parseLong( builder.toString( ), 2 );
            }

            long[] colDecimals = new long[nCols];
            for ( int col = 0; col < nCols; col++ )
            {
                StringBuilder builder = new StringBuilder( );
                for ( int row = 0; row < nRows; row++ )
                {
                    char c = grid[row][col];
                    String s = c == '#' ? "1" : "0";
                    builder.append( s );
                }
                colDecimals[col] = Long.parseLong( builder.toString( ), 2 );
            }

            decimals.add( new Decimal( rowDecimals, colDecimals ) );
        }

        return decimals;
    }

    protected static long findReflection( long[] array )
    {

        int L = array.length;

        int maxReflection = -1;
        for ( int i = 0; i <= L - 2; i++ )
        {
            int offset = getOffset( i, L );
            boolean mirrored = true;
            for ( int k = 0; k <= offset; k++ )
            {
                long before = array[i - k];
                long after = array[i + 1 + k];
                if ( before != after )
                {
                    mirrored = false;
                    break;
                }
            }
            if ( mirrored )
            {
                if ( maxReflection < i + 1 )
                {
                    maxReflection = i + 1;
                }
            }
        }

        return maxReflection;
    }

    protected static Offset getOffsets( int i, int L )
    {
        int offset = getOffset( i, L );
        int left = i - offset;
        int right = i + 1 + offset;
        return new Offset( left, right );
    }

    protected static int getOffset( int i, int L )
    {
        int dLeft = i + 1;
        int dRight = L - ( i + 1 );
        int offset = Math.min( dLeft, dRight );
        return offset - 1;
    }

    protected record Decimal( long[] rows, long[] cols ) { }

    protected record Offset( int left, int right )
    {

    }
}
