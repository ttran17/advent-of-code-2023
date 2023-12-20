package com.github.ttran17.day19;

import com.github.ttran17.RegexUtils;

import it.unimi.dsi.fastutil.chars.Char2IntArrayMap;
import it.unimi.dsi.fastutil.chars.Char2IntMap;

public class Rating
{
    protected final Char2IntMap map = new Char2IntArrayMap( );

    private long totalRatings;

    private final String rep;

    protected Rating( String line )
    {
        // {x=787,m=2655,a=1222,s=2876}
        String[] tokens = RegexUtils.removeCurlyBraces( line ).split( "," );

        long totalRatings = 0;
        for ( String token : tokens )
        {
            char c = token.charAt( 0 );
            int rating = Integer.parseInt( token.substring( 2 ) );
            map.put( c, rating );

            totalRatings += rating;
        }
        this.totalRatings = totalRatings;

        this.rep = line;
    }

    protected boolean contains( char c )
    {
        return map.containsKey( c );
    }

    protected int get( char c )
    {
        return map.getOrDefault( c, -1 );
    }

    protected long totalRatings( )
    {
        return totalRatings;
    }

    @Override
    public String toString( )
    {
        return rep;
    }
}
