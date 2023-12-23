package com.github.ttran17.day21;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.github.ttran17.util.Direction;

public class Garden
{
    protected final int nRows;
    protected final int nCols;

    protected final char[][] layout;

    protected final boolean[][] visited;

    protected final int[][] stepCount;

    protected final List<Gnome> exactVisits = new ArrayList<>( );

    protected final int walkStartRow;
    protected final int walkStartCol;

    protected final int maxSteps;

    protected Garden( List<String> lines, int maxSteps )
    {
        nRows = lines.size( );
        nCols = lines.get( 0 ).trim( ).length( );

        layout = new char[nRows][nCols];
        visited = new boolean[nRows][nCols];
        stepCount = new int[nRows][nCols];

        int walkStartRow = -1;
        int walkStartCol = -1;
        for ( int row = 0; row < nRows; row++ )
        {
            String line = lines.get( row ).trim( );
            for ( int col = 0; col < nCols; col++ )
            {
                char tile = line.charAt( col );
                layout[row][col] = tile;
                if ( tile == 'S' )
                {
                    walkStartRow = row;
                    walkStartCol = col;
                }

                stepCount[row][col] = maxSteps;
            }
        }
        this.walkStartRow = walkStartRow;
        this.walkStartCol = walkStartCol;

        this.maxSteps = maxSteps;
    }

    protected Garden( Garden origin, int walkStartRow, int walkStartCol, int maxSteps )
    {
        nRows = origin.nRows;
        nCols = origin.nCols;

        layout = new char[nRows][nCols];
        visited = new boolean[nRows][nCols];
        stepCount = new int[nRows][nCols];

        for ( int row = 0; row < nRows; row++ )
        {
            for ( int col = 0; col < nCols; col++ )
            {
                layout[row][col] = origin.layout[row][col];
                stepCount[row][col] = maxSteps;
            }
        }
        this.walkStartRow = walkStartRow;
        this.walkStartCol = walkStartCol;

        this.maxSteps = maxSteps;
    }

    protected long walk( )
    {
        return walk( maxSteps );
    }

    protected long walk( int totalSteps )
    {
        Queue<Gnome> queue = new ArrayDeque<>( );
        queue.add( createGnome( walkStartRow, walkStartCol, totalSteps ) );

        while ( !queue.isEmpty( ) )
        {
            Gnome gnome = queue.poll( );

            if ( gnome.stepsRemaining % 2 == 0 )
            {
                exactVisits.add( gnome );
            }

            if ( gnome.stepsRemaining > 0 )
            {
                offerNextGnomes( gnome, queue );
            }
        }

        return exactVisits.size( );
    }

    protected void offerNextGnomes( Gnome gnome, Queue<Gnome> queue )
    {
        for ( Direction direction : Direction.values( ) )
        {
            int nextRow = gnome.row + direction.dRow( );
            int nextCol = gnome.col + direction.dCol( );

            if ( canStep( nextRow, nextCol ) )
            {
                queue.offer( createGnome( nextRow, nextCol, gnome.stepsRemaining - 1 ) );
            }
        }
    }

    protected Gnome createGnome( int row, int col, int stepsRemaining )
    {
        Gnome gnome = new Gnome( row, col, stepsRemaining );
        this.visited[gnome.row][gnome.col] = true;
        this.stepCount[row][col] = maxSteps - stepsRemaining;

        return gnome;
    }

    protected boolean canStep( int row, int col )
    {
        if ( row < 0 || row > nRows - 1 || col < 0 || col > nCols - 1 )
        {
            return false;
        }

        return !visited[row][col] && layout[row][col] != '#';
    }

    protected int maxStepCount( )
    {
        int maxStepCount = -1;
        for ( int row = 0; row < nRows; row++ )
        {
            for ( int col = 0; col < nCols; col++ )
            {
                if ( visited[row][col] )
                {
                    if ( maxStepCount < stepCount[row][col] )
                    {
                        maxStepCount = stepCount[row][col];
                    }
                }
            }
        }
        return maxStepCount;
    }

    protected record Gnome( int row, int col, int stepsRemaining ) { }

    protected static long walk( List<String> lines, int totalSteps )
    {
        Garden garden = new Garden( lines, totalSteps );

        return garden.walk( );
    }
}
