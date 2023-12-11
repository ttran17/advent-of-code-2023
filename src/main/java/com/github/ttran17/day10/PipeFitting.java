package com.github.ttran17.day10;

import static com.github.ttran17.day10.Direction.EAST;
import static com.github.ttran17.day10.Direction.NORTH;
import static com.github.ttran17.day10.Direction.SOUTH;
import static com.github.ttran17.day10.Direction.WEST;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public enum PipeFitting
{
    // | is a vertical pipe connecting north and south.
    // - is a horizontal pipe connecting east and west.
    // L is a 90-degree bend connecting north and east.
    // J is a 90-degree bend connecting north and west.
    // 7 is a 90-degree bend connecting south and west.
    // F is a 90-degree bend connecting south and east.
    // . is ground; there is no pipe in this tile.
    // S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.

    VERTICAL( '|', List.of( NORTH, SOUTH ), true ),

    HORIZONTAL( '-', List.of( EAST, WEST ), false ),

    NORTH_AND_EAST( 'L', List.of( NORTH, EAST ), true ),

    NORTH_AND_WEST( 'J', List.of( NORTH, WEST ), true ),

    SOUTH_AND_WEST( '7', List.of( SOUTH, WEST ), true ),

    SOUTH_AND_EAST( 'F', List.of( SOUTH, EAST ), true ),

    GROUND( '.', List.of( ), false ),

    BOWSER( 'S', List.of( NORTH, EAST, SOUTH, WEST ), true );

    private final char c;

    private final List<Direction> validDirections;

    private final boolean verticalComponent;

    PipeFitting( char c, List<Direction> validDirections, boolean verticalComponent )
    {
        this.c = c;
        this.validDirections = validDirections;
        this.verticalComponent = verticalComponent;
    }

    public boolean hasVerticalComponent( )
    {
        return verticalComponent;
    }

    /**
     * <strong>CAUTION</strong>>: Only checks to see if this PipeFitting can connect to its NORTH or WEST neighbor!
     */
    public Direction getConnection( PipeFitting other, Direction directionToOther )
    {
        for ( Direction direction : validDirections )
        {
            for ( Direction otherDirection : other.validDirections )
            {
                if ( directionToOther == NORTH && direction == NORTH && otherDirection == SOUTH )
                {
                    return direction;
                }

                if ( directionToOther == WEST && direction == WEST && otherDirection == EAST )
                {
                    return direction;
                }
            }
        }

        return null;
    }

    public static PipeFitting resolve( List<Direction> directions )
    {
        for ( PipeFitting pipeFitting : PipeFitting.values( ) )
        {
            if ( pipeFitting.validDirections.size( ) == directions.size( ) && pipeFitting.validDirections.containsAll( directions ) )
            {
                return pipeFitting;
            }
        }

        throw new IllegalArgumentException( "Unable to resolve directions: " + StringUtils.join( directions, ", " ) );
    }

    public static PipeFitting parse( char c )
    {
        for ( PipeFitting fitting : PipeFitting.values( ) )
        {
            if ( fitting.c == c )
            {
                return fitting;
            }
        }

        throw new IllegalArgumentException( "Unknown character: " + c );
    }

    @Override
    public String toString( )
    {
        return Character.toString( c );
    }
}
