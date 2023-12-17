package com.github.ttran17.day14;

public enum RockType
{
    EMPTY( '.' ),
    ROUND( 'O' ),
    SQUARE( '#' );

    private final char c;

    RockType( char c )
    {
        this.c = c;
    }

    @Override
    public String toString( )
    {
        return Character.toString( c );
    }

    public static RockType parse( char c )
    {
        for ( RockType rockType : RockType.values( ) )
        {
            if ( rockType.c == c )
            {
                return rockType;
            }
        }

        throw new IllegalArgumentException( "Unknown character: " + c );
    }
}
