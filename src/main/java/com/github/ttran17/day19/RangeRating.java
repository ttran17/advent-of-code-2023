package com.github.ttran17.day19;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;

public class RangeRating
{
    protected final Char2ObjectMap<Range<Integer>> map;

    public RangeRating( Char2ObjectMap<Range<Integer>> map )
    {
        this.map = map;
    }

    protected boolean containsKey( char c )
    {
        return map.containsKey( c );
    }

    protected Range<Integer> get( char c )
    {
        return map.get( c );
    }

    protected boolean isEmpty( )
    {
        for ( Range<Integer> range : map.values( ) )
        {
            if ( !range.isEmpty( ) )
            {
                return false;
            }
        }
        return true;
    }

    protected long distinct( )
    {
        long product = 1;
        for ( char c : RangeOperation.XMAS )
        {
            if ( map.containsKey( c ) )
            {
                Range<Integer> range = map.get( c );
                product *= length( range );
            }
        }
        return product;
    }

    protected static int length( Range<Integer> range )
    {
        if ( range.lowerBoundType( ) == BoundType.CLOSED && range.upperBoundType( ) == BoundType.CLOSED )
        {
            return range.upperEndpoint( ) - range.lowerEndpoint( ) + 1;
        }

        if ( range.lowerBoundType( ) == BoundType.CLOSED && range.upperBoundType( ) == BoundType.OPEN )
        {
            return range.upperEndpoint( ) - range.lowerEndpoint( );
        }

        if ( range.lowerBoundType( ) == BoundType.OPEN && range.upperBoundType( ) == BoundType.CLOSED )
        {
            return range.upperEndpoint( ) - range.lowerEndpoint( );
        }

        if ( range.lowerBoundType( ) == BoundType.OPEN && range.upperBoundType( ) == BoundType.OPEN )
        {
            return range.upperEndpoint( ) - range.lowerEndpoint( ) - 1;
        }

        throw new IllegalStateException( "Range<Integer> does not conform to expectations!" );
    }

    @Override
    public String toString( )
    {
        List<String> stringList = new ArrayList<>( );
        for ( char c : RangeOperation.XMAS )
        {
            if ( containsKey( c ) )
            {
                stringList.add( String.format( "%c=%s", c, get( c ).toString( ) ) );
            }
        }

        StringBuilder builder = new StringBuilder( );
        builder.append( StringUtils.join( stringList, "," ) );
        return builder.toString( );
    }
}
