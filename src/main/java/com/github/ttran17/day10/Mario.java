package com.github.ttran17.day10;

import static com.github.ttran17.day10.PlumbingDiagram.buildDiagram;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Mario
{

    protected static long walk( List<String> lines )
    {
        PlumbingDiagram diagram = buildDiagram( lines );

        depthFirstWalk( diagram );

        return findPeak( diagram ).getStep( );
    }

    protected static long walkJordan( List<String> lines )
    {
        PlumbingDiagram diagram = buildDiagram( lines );

        depthFirstWalk( diagram );

        Tile peak = findPeak( diagram );

        markLoop( diagram, peak );

        walkJordan( diagram, peak );

        return countInside( diagram );
    }

    protected interface Visitor
    {
        void visit( Tile tile, Tile neighbor );
    }

    protected static void walk( Tile start, Tile end, Visitor visitor )
    {
        Set<Tile> visited = new LinkedHashSet<>( );
        visited.add( end );

        Deque<Tile> neighbors = new ArrayDeque<>( );
        neighbors.push( start );
        while ( !neighbors.isEmpty( ) )
        {
            Tile tile = neighbors.pop( );
            visited.add( tile );
            for ( Tile neighbor : tile.getValidNeighbors( ) )
            {
                if ( !visited.contains( neighbor ) )
                {
                    visitor.visit( tile, neighbor );
                    neighbors.push( neighbor );
                }
            }
        }
    }

    protected static void depthFirstWalk( PlumbingDiagram diagram )
    {
        Tile bowser = diagram.getBowser( );
        bowser.setStep( 0 );

        Queue<Tile> bowserNeighbors = new ArrayDeque<>( );
        for ( Tile neighbor : bowser.getValidNeighbors( ) )
        {
            bowserNeighbors.offer( neighbor );
        }

        while ( !bowserNeighbors.isEmpty( ) )
        {
            Tile bowserNeighbor = bowserNeighbors.poll( );
            bowserNeighbor.setStep( 1 );

            walk( bowserNeighbor, bowser, ( tile, neighbor ) -> {
                int step = tile.getStep( ) + 1;
                neighbor.setStep( step );
            } );
        }
    }

    protected static Tile findPeak( PlumbingDiagram diagram )
    {
        Tile[][] grid = diagram.grid( );

        int nRows = grid.length;
        int nCols = grid[0].length;

        for ( int row = 1; row < nRows; row++ )
        {
            for ( int col = 1; col < nCols; col++ )
            {
                Tile tile = grid[row][col];
                int step = tile.getStep( );
                if ( step > 0 && isPeak( tile, step ) )
                {
                    return tile;
                }
            }
        }

        throw new IllegalStateException( "Could not find a peak step in the grid!" );
    }

    protected static boolean isPeak( Tile tile, int peak )
    {
        List<Tile> neighbors = tile.getValidNeighbors( );
        boolean isPeak = true;
        for ( Tile neighbor : neighbors )
        {
            isPeak = isPeak && ( neighbor.getStep( ) < peak );
        }
        return isPeak;
    }

    protected static void markLoop( PlumbingDiagram diagram, Tile peak )
    {
        Tile bowser = diagram.grid( )[diagram.bowserRow( )][diagram.bowserCol( )];

        bowser.setLoop( true );
        peak.setLoop( true );

        walk( peak, bowser, ( tile, neighbor ) -> {
            neighbor.setLoop( true );
        } );
    }

    /**
     * Compute the walk so that the loop is walked with the inside on the left
     */
    protected static void walkJordan( PlumbingDiagram diagram, Tile peak )
    {
        diagram.resolveBowserPipeFitting( );

        Tile[][] grid = diagram.grid( );

        Tile start = findFirstIntersection( grid[peak.row], grid[0].length );

        // Need to get the initial direction to walk
        if ( start.pipeFitting == PipeFitting.VERTICAL )
        {
            start.setNext( start.getNeighbor( Direction.SOUTH ) );
            start.getNeighbor( Direction.NORTH ).setNext( start );
        }
        else if ( start.pipeFitting == PipeFitting.SOUTH_AND_EAST )
        {
            start.setNext( start.getNeighbor( Direction.SOUTH ) );
            start.getNeighbor( Direction.EAST ).setNext( start );
        }
        else if ( start.pipeFitting == PipeFitting.NORTH_AND_EAST )
        {
            start.setNext( start.getNeighbor( Direction.EAST ) );
            start.getNeighbor( Direction.NORTH ).setNext( start );
        }
        else
        {
            throw new IllegalStateException( "Unexpected PipeFitting: " + start.pipeFitting );
        }

        walk( start.getNext( ), start, ( tile, neighbor ) -> tile.setNext( neighbor ) );
    }

    protected static Tile findFirstIntersection( Tile[] row, int nCols )
    {
        for ( int col = 1; col < nCols; col++ )
        {
            Tile tile = row[col];
            if ( tile.isLoop( ) )
            {
                return tile;
            }
        }

        throw new IllegalStateException( "Could not find an intersection!" );
    }

    protected static boolean isInside( Tile[][] grid, Tile start, int nCols )
    {
        int parity = 0;

        for ( int col = start.col + 1; col < nCols; col++ )
        {
            Tile next = grid[start.row][col];
            if ( next.isLoop( ) && next.pipeFitting.hasVerticalComponent( ) )
            {
                if ( next.pipeFitting == PipeFitting.VERTICAL )
                {
                    if ( next.getNext( ) == next.getNeighbor( Direction.NORTH ) )
                    {
                        parity = parity + 2;
                    }
                    else
                    {
                        parity = parity - 2;
                    }
                    continue;
                }

                if ( next.pipeFitting == PipeFitting.NORTH_AND_EAST )
                {
                    if ( next.getNext( ) == next.getNeighbor( Direction.NORTH ) )
                    {
                        parity = parity + 1;
                    }
                    else
                    {
                        parity = parity - 1;
                    }
                }

                if ( next.pipeFitting == PipeFitting.NORTH_AND_WEST )
                {
                    if ( next.getNext( ) == next.getNeighbor( Direction.NORTH ) )
                    {
                        parity = parity + 1;
                    }
                    else
                    {
                        parity = parity - 1;
                    }
                }

                if ( next.pipeFitting == PipeFitting.SOUTH_AND_EAST )
                {
                    if ( next.getNext( ) == next.getNeighbor( Direction.EAST ) )
                    {
                        parity = parity + 1;
                    }
                    else
                    {
                        parity = parity - 1;
                    }
                }

                if ( next.pipeFitting == PipeFitting.SOUTH_AND_WEST )
                {
                    if ( next.getNext( ) == next.getNeighbor( Direction.WEST ) )
                    {
                        parity = parity + 1;
                    }
                    else
                    {
                        parity = parity - 1;
                    }
                }
            }
        }
        start.setInside( parity > 0 );
        return start.isInside( );
    }

    protected static long countInside( PlumbingDiagram diagram )
    {
        Tile[][] grid = diagram.grid( );

        int nRows = grid.length;
        int nCols = grid[0].length;

        int sum = 0;
        for ( int row = 1; row < nRows; row++ )
        {
            for ( int col = 1; col < nCols; col++ )
            {
                Tile tile = grid[row][col];
                if ( !tile.isLoop( ) && isInside( grid, tile, nCols ) )
                {
                    sum++;
                }
            }
        }

        return sum;
    }
}
