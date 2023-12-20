package com.github.ttran17.day18;

import java.util.ArrayList;
import java.util.List;

public class BadRobot
{

    protected final List<Position> visited = new ArrayList<>( );

    protected long row;
    protected long col;

    public BadRobot( long row, long col )
    {
        this.row = row;
        this.col = col;

        this.visited.add( new Position( row, col ) );
    }

    protected void step( String line )
    {
        line = line.trim( );

        int p1 = line.indexOf( '(' );

        String[] tokens = line.substring( 0, p1 ).trim( ).split( " " );
        char move = tokens[0].charAt( 0 );
        long steps = Integer.parseInt( tokens[1] );

        switch ( move )
        {
            case 'R' -> row += steps;
            case 'D' -> col += steps;
            case 'L' -> row -= steps;
            case 'U' -> col -= steps;
        }

        visited.add( new Position( row, col ) );
    }

    protected void stepActual( String line )
    {
        line = line.trim( );

        int p1 = line.indexOf( '(' );
        int p2 = line.indexOf( ')' );

        String instruction = line.substring( p1 + 1, p2 );

        long steps = Long.decode( instruction.substring( 0, 6 ) );
        char move = String.valueOf( Long.parseLong( instruction.substring( 6 ), 16 ) ).charAt( 0 );

        switch ( move )
        {
            case '0' -> row += steps;
            case '1' -> col += steps;
            case '2' -> row -= steps;
            case '3' -> col -= steps;
        }

        visited.add( new Position( row, col ) );
    }

    protected Position[] getVisitedArray( )
    {
        return visited.toArray( new Position[0] );
    }

    protected record Position( long row, long col ) { }
}
