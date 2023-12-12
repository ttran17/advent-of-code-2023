package com.github.ttran17.day11;

import static com.github.ttran17.day11.Universe.Galaxy;

import java.util.List;

public class Sagan
{

    protected static int distance( Galaxy a, Galaxy b )
    {
        return Math.abs( a.getRow( ) - b.getRow( ) ) + Math.abs( a.getCol( ) - b.getCol( ) );
    }

    protected static long computePairWiseDistances( List<String> lines )
    {
        Universe universe = Universe.build( lines );
        universe.expand( );

        return computePairWiseDistances( universe );
    }

    protected static long computePairWiseDistancesOlderUniverse( List<String> lines, int expansionFactor )
    {
        Universe universe = Universe.build( lines );
        universe.expand( expansionFactor, ( a, b, c ) -> { } );

        return computePairWiseDistances( universe );
    }

    protected static long computePairWiseDistances( Universe universe )
    {
        long sum = 0;
        for ( int i = 0; i < universe.allGalaxies.size( ); i++ )
        {
            Galaxy a = universe.allGalaxies.get( i );
            for ( int j = i; j < universe.allGalaxies.size( ); j++ )
            {
                Galaxy b = universe.allGalaxies.get( j );
                sum += distance( a, b );
            }
        }

        return sum;
    }
}
