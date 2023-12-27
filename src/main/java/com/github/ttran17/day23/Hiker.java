package com.github.ttran17.day23;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import com.github.ttran17.util.Direction;

public class Hiker
{
    protected static long descend( List<String> lines )
    {
        Mountain mountain = new Mountain( lines );

        return ascend( mountain );
    }

    protected static long ascend( Mountain mountain )
    {
        long[][] bellman = new long[mountain.nRows][mountain.nCols];

        Location finalLocation = new Location( mountain.endRow, mountain.endCol, null );

        Queue<Location> queue = new ArrayDeque<>( );
        backtrack( mountain, finalLocation, queue );
        while ( !queue.isEmpty( ) )
        {
            Location location = queue.poll( );

            long currentReward = bellman[location.previous.row][location.previous.col] + 1;
            if ( currentReward > bellman[location.row][location.col] )
            {
                bellman[location.row][location.col] = currentReward;
                backtrack( mountain, location, queue );
            }
        }

        return bellman[mountain.startRow][mountain.startCol];
    }

    protected static void backtrack( Mountain mountain, Location currentLocation, Queue<Location> queue )
    {
        for ( Direction direction : Direction.values( ) )
        {
            int row = currentLocation.row + direction.dRow( );
            int col = currentLocation.col + direction.dCol( );

            if ( !mountain.isValidLocation( row, col ) )
            {
                continue;
            }

            if ( currentLocation.previous != null && row == currentLocation.previous.row && col == currentLocation.previous.col )
            {
                continue;
            }

            char previous = mountain.layout[row][col];

            switch ( direction )
            {
                case SOUTH ->
                {
                    if ( previous != 'v' )
                    {
                        queue.offer( new Location( row, col, currentLocation ) );
                    }
                }
                case EAST ->
                {
                    if ( previous != '>' )
                    {
                        queue.offer( new Location( row, col, currentLocation ) );
                    }
                }
                case NORTH ->
                {
                    if ( previous != '^' )
                    {
                        queue.offer( new Location( row, col, currentLocation ) );
                    }
                }
                case WEST ->
                {
                    if ( previous != '<' )
                    {
                        queue.offer( new Location( row, col, currentLocation ) );
                    }
                }
            }

        }
    }

    protected record Location( int row, int col, Location previous ) { }
}
