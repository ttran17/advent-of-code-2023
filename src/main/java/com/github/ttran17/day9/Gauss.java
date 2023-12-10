package com.github.ttran17.day9;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;

/**
 * No doubt the guy would be rolling over in his grave to have his name used here
 */
public class Gauss
{
    protected record Sequence(LongList x, boolean allSame)
    {
        public long getFirstValue( )
        {
            return x.get( 0 );
        }

        public long getLastValue( )
        {
            return x.get( x.size( ) - 1 );
        }
    }

    protected static Sequence diff( Sequence sequence )
    {
        LongList d = new LongArrayList( );

        boolean allSame = true;
        for ( int i = 1; i < sequence.x.size( ); i++ )
        {
            d.add( sequence.x.getLong( i ) - sequence.x.getLong( i - 1 ) );
            if ( i > 1 )
            {
                allSame = allSame && d.getLong( i - 1 ) == d.getLong( i - 2 );
            }
        }

        return new Sequence( d, allSame );
    }

    protected static Sequence consume( String line )
    {
        String[] tokens = line.split( " " );
        LongList x = new LongArrayList( );
        boolean allSame = true;
        for ( int i = 0; i < tokens.length; i++ )
        {
            x.add( Long.parseLong( tokens[i].trim( ) ) );
            if ( i > 0 )
            {
                allSame = allSame && x.getLong( i ) == x.getLong( i - 1 );
            }
        }
        return new Sequence( x, allSame );
    }

    protected static long forecast( List<String> lines )
    {
        return forecast( lines, ( sequence, forecastedValue ) -> sequence.getLastValue( ) + forecastedValue );
    }

    protected static long forecastTwoStar( List<String> lines )
    {
        return forecast( lines, ( sequence, forecastedValue ) -> sequence.getFirstValue( ) - forecastedValue );
    }

    protected static long forecast( List<String> lines, Extrapolator extrapolator )
    {
        List<Sequence> sequences = lines.stream( ).map( line -> consume( line ) ).collect( Collectors.toList( ) );

        long sum = 0;
        for ( Sequence sequence : sequences )
        {
            Deque<Sequence> stack = descend( sequence );
            sum += ascend( stack, extrapolator );
        }
        return sum;
    }

    protected static Deque<Sequence> descend( Sequence sequence )
    {
        Deque<Sequence> stack = new ArrayDeque<>( );

        stack.push( sequence );
        while ( !sequence.allSame )
        {
            sequence = diff( sequence );
            stack.push( sequence );
        }

        return stack;
    }

    protected interface Extrapolator
    {
        long accept( Sequence sequence, long forecastedValue );
    }

    protected static long ascend( Deque<Sequence> stack, Extrapolator extrapolator )
    {
        long forecastedValue = 0;
        while ( !stack.isEmpty( ) )
        {
            Sequence sequence = stack.pop( );
            forecastedValue = extrapolator.accept( sequence, forecastedValue );
        }

        return forecastedValue;
    }

}
