package com.github.ttran17.day10;

public enum Direction
{
    NORTH( 2 ),
    EAST( 3 ),
    SOUTH( 0 ),
    WEST( 1 );

    private final int partnerOrdinal;

    Direction( int partnerOrdinal )
    {
        this.partnerOrdinal = partnerOrdinal;
    }

    public Direction getPartner( )
    {
        return Direction.values( )[this.partnerOrdinal];
    }
}
