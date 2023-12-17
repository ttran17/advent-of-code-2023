package com.github.ttran17.day16;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Grid
{
    protected final int nRows;
    protected final int nCols;

    protected final char[][] layout;

    protected final long[][] energize;

    protected final int[][] splitters;

    protected final List<BadRobot> active;

    protected final List<BadRobot> inactive;

    protected Grid( List<String> lines )
    {
        nRows = lines.size( );
        nCols = lines.get( 0 ).trim( ).length( );

        layout = new char[nRows][nCols];
        splitters = new int[nRows][nCols];

        for ( int row = 0; row < nRows; row++ )
        {
            for ( int col = 0; col < nCols; col++ )
            {
                char c = lines.get( row ).charAt( col );
                layout[row][col] = c;
                splitters[row][col] = 1;
            }
        }

        energize = new long[nRows][nCols];

        active = new LinkedList<>( );

        inactive = new ArrayList<>( );
    }

    protected void reset( )
    {
        for ( int row = 0; row < nRows; row++ )
        {
            for ( int col = 0; col < nCols; col++ )
            {
                splitters[row][col] = 1;
                energize[row][col] = 0;
                active.clear( );
                inactive.clear( );
            }
        }
    }

    protected void addBadRobot( int row, int col, Direction direction )
    {
        active.add( new BadRobot( this, row, col, direction ) );
    }

    protected void addBadRobot( BadRobot badRobot )
    {
        active.add( badRobot );
    }

    protected void moveBadRobots( )
    {
        List<BadRobot> current = new ArrayList<>( active );
        for ( BadRobot badRobot : current )
        {
            badRobot.step( );
        }
    }

    protected void winnowBadRobots( )
    {
        Iterator<BadRobot> iterator = active.iterator( );
        while ( iterator.hasNext( ) )
        {
            BadRobot badRobot = iterator.next( );
            if ( !badRobot.active )
            {
                iterator.remove( );
                inactive.add( badRobot );
            }
        }
    }

    protected long energize( )
    {
        for ( BadRobot badRobot : active )
        {
            if ( badRobot.row > -1 && badRobot.row < nRows && badRobot.col > -1 && badRobot.col < nCols - 1 )
            {
                energize[badRobot.row][badRobot.col] = 1;
            }
        }

        while ( !active.isEmpty( ) )
        {
            moveBadRobots( );

            winnowBadRobots( );
        }

        long sum = 0;
        for ( int row = 0; row < nRows; row++ )
        {
            for ( int col = 0; col < nCols; col++ )
            {
                sum += energize[row][col];
            }
        }
        return sum;
    }

    protected static long energize( List<String> lines )
    {
        Grid grid = new Grid( lines );
        grid.addBadRobot( 0, -1, Direction.EAST );
        return grid.energize( );
    }

    protected static long energizeTwoStar( List<String> lines )
    {
        Grid grid = new Grid( lines );
        long maxEnergized = 0;
        long energized;

        energized = maxEnergized( grid, new BadRobotBuilder[] {
                ( g ) -> new BadRobot( g, 0, 0, Direction.EAST ),
                ( g ) -> new BadRobot( g, 0, 0, Direction.SOUTH ),
        } );
        maxEnergized = Math.max( energized, maxEnergized );

        grid.reset( );
        energized = maxEnergized( grid, new BadRobotBuilder[] {
                ( g ) -> new BadRobot( g, grid.nRows - 1, 0, Direction.EAST ),
                ( g ) -> new BadRobot( g, grid.nRows - 1, 0, Direction.NORTH ),
        } );
        maxEnergized = Math.max( energized, maxEnergized );

        grid.reset( );
        energized = maxEnergized( grid, new BadRobotBuilder[] {
                ( g ) -> new BadRobot( g, 0, grid.nCols - 1, Direction.WEST ),
                ( g ) -> new BadRobot( g, 0, grid.nCols - 1, Direction.SOUTH ),
        } );
        maxEnergized = Math.max( energized, maxEnergized );

        grid.reset( );
        energized = maxEnergized( grid, new BadRobotBuilder[] {
                ( g ) -> new BadRobot( g, grid.nRows - 1, grid.nCols - 1, Direction.WEST ),
                ( g ) -> new BadRobot( g, grid.nRows - 1, grid.nCols - 1, Direction.NORTH ),
        } );
        maxEnergized = Math.max( energized, maxEnergized );

        for ( int col = 1; col < grid.nCols - 1; col++ )
        {
            grid.reset( );
            int fCol = col;
            energized = maxEnergized( grid, ( g ) -> new BadRobot( g, 0, fCol, Direction.SOUTH ) );
            maxEnergized = Math.max( energized, maxEnergized );
        }
        for ( int col = 1; col < grid.nCols - 1; col++ )
        {
            grid.reset( );
            int fCol = col;
            energized = maxEnergized( grid, ( g ) -> new BadRobot( g, grid.nRows - 1, fCol, Direction.NORTH ) );
            maxEnergized = Math.max( energized, maxEnergized );
        }
        for ( int row = 1; row < grid.nRows - 1; row++ )
        {
            grid.reset( );
            int fRow = row;
            energized = maxEnergized( grid, ( g ) -> new BadRobot( g, fRow, 0, Direction.EAST ) );
            maxEnergized = Math.max( energized, maxEnergized );
        }
        for ( int row = 1; row < grid.nRows - 1; row++ )
        {
            grid.reset( );
            int fRow = row;
            energized = maxEnergized( grid, ( g ) -> new BadRobot( g, fRow, grid.nCols - 1, Direction.WEST ) );
            maxEnergized = Math.max( energized, maxEnergized );
        }

        return maxEnergized;
    }

    protected static long maxEnergized( Grid grid, BadRobotBuilder... badRobotBuilders )
    {
        long maxEnergized = 0;
        for ( BadRobotBuilder badRobotBuilder : badRobotBuilders )
        {
            grid.addBadRobot( badRobotBuilder.accept( grid ) );
        }
        long energized = grid.energize( );
        if ( maxEnergized < energized ) ;
        {
            maxEnergized = energized;
        }
        return maxEnergized;
    }

    protected static long energizeTwoStar( List<String> lines, BadRobotBuilder... badRobotBuilders )
    {
        Grid grid = new Grid( lines );
        return maxEnergized( grid, badRobotBuilders );
    }

    protected void print( )
    {
        StringBuilder builder = new StringBuilder( );
        for ( int row = 0; row < nRows; row++ )
        {
            for ( int col = 0; col < nCols; col++ )
            {
                if ( energize[row][col] > 0 )
                {
                    builder.append( "#" );
                }
                else
                {
                    builder.append( "." );
                }
            }
            builder.append( System.lineSeparator( ) );
        }
        System.out.println( builder );
    }

    protected interface BadRobotBuilder
    {
        BadRobot accept( Grid grid );
    }
}
