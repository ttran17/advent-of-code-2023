package com.github.ttran17.util;

public enum Direction
{
    NORTH( -1, 0 ),
    WEST( 0, -1 ),
    SOUTH( 1, 0 ),

    EAST( 0, 1 );

    private final int dRow;
    private final int dCol;

    Direction( int dRow, int dCol )
    {
        this.dRow = dRow;
        this.dCol = dCol;
    }

    public int dRow( )
    {
        return dRow;
    }

    public int dCol( )
    {
        return dCol;
    }
}
