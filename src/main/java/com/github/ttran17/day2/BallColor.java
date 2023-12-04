package com.github.ttran17.day2;

public enum BallColor
{
    RED( "red" ),
    GREEN( "green" ),
    BLUE( "blue" );

    private final String color;

    BallColor( String color )
    {
        this.color = color;
    }

    public static BallColor parse( String s )
    {
        for ( BallColor ballColor : BallColor.values( ) )
        {
            if ( ballColor.color.equalsIgnoreCase( s ) )
            {
                return ballColor;
            }
        }

        throw new IllegalArgumentException( String.format( "Got an unknown color %s", s ) );
    }
}
