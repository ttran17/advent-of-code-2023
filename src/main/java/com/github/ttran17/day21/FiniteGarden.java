package com.github.ttran17.day21;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.github.ttran17.util.Direction;

public class FiniteGarden
{
    protected final int nRows;
    protected final int nCols;

    protected final char[][] layout;

    protected final boolean[][] visited;

    protected final int[][] stepCount;

    protected final List<Gnome> exactVisits = new ArrayList<>( );

    private final int minSideCopies;

    protected final int walkStartRow;
    protected final int walkStartCol;

    protected final int maxSteps;

    protected final int side;

    protected final int center;

    protected FiniteGarden( List<String> lines, int maxSteps, int side, int center )
    {
        this.side = side;
        this.center = center;

        minSideCopies = Math.max( ( maxSteps - center ) / side + 1, 0 );

        nRows = ( 1 + 2 * minSideCopies ) * lines.size( );
        nCols = ( 1 + 2 * minSideCopies ) * lines.get( 0 ).length( );

        layout = new char[nRows][nCols];
        visited = new boolean[nRows][nCols];
        stepCount = new int[nRows][nCols];

        for ( int row = 0; row < nRows; row++ )
        {
            String line = lines.get( row % side ).trim( );
            for ( int col = 0; col < nCols; col++ )
            {
                char tile = line.charAt( col % side );
                layout[row][col] = tile;
                stepCount[row][col] = maxSteps;
            }
        }
        this.walkStartRow = side * minSideCopies + center;
        this.walkStartCol = side * minSideCopies + center;

        this.maxSteps = maxSteps;
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

    protected long exactVisits( int hopR, int hopC )
    {
        int offsetR = side * hopR;
        int offsetC = side * hopC;

        int[][] stepsRemaining = new int[side][side];
        long sum = 0;
        for ( int row = offsetR; row < offsetR + side; row++ )
        {
            for ( int col = offsetC; col < offsetC + side; col++ )
            {
                stepsRemaining[row - offsetR][col - offsetC] = maxSteps - stepCount[row][col];
                if ( visited[row][col] && ( ( maxSteps - stepCount[row][col] ) % 2 == 0 ) )
                {
                    sum += 1;
                }
            }
        }
        return sum;
    }

    protected record Gnome( int row, int col, int stepsRemaining ) { }

    protected static long walk( List<String> lines, int maxSteps, int totalSteps, int side, int center )
    {
        FiniteGarden garden = new FiniteGarden( lines, maxSteps, side, center );

        long exactVisits = garden.walk( totalSteps );

        long finiteCross = 0;
        if ( false )
        {
            long sum = 0;
            int maxHop = 1 + 2 * garden.minSideCopies;
            int o = garden.minSideCopies;
            for ( int hopR = 0; hopR < maxHop; hopR++ )
            {
                sum += garden.exactVisits( hopR, o );
            }
            for ( int hopC = 0; hopC < maxHop; hopC++ )
            {
                sum += garden.exactVisits( o, hopC );
            }
            sum -= ( 2 * garden.exactVisits( o, o ) );
            finiteCross = sum;
            System.out.println( "center: " + garden.exactVisits( o, o ) );
            System.out.println( "finite cross: " + finiteCross );
        }

        if ( false )
        {
            long sum = 0;
            int maxHop = 1 + 2 * garden.minSideCopies;
            int o = garden.minSideCopies;
            for ( int hopR = 0; hopR < maxHop; hopR++ )
            {
                for ( int hopC = 0; hopC < maxHop; hopC++ )
                {
                    sum += garden.exactVisits( hopR, hopC );
                }
            }
            sum -= ( 1 * garden.exactVisits( o, o ) );
            sum = sum - finiteCross;
            System.out.println( "quadrant: " + sum );
        }

        return exactVisits;
    }
}
