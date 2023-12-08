package com.github.ttran17.day7;

public enum Card
{
    // A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, or 2
    A( 'A' ),
    K( 'K' ),
    Q( 'Q' ),
    J( 'J' ),
    T( 'T' ),
    _9( '9' ),
    _8( '8' ),
    _7( '7' ),
    _6( '6' ),
    _5( '5' ),
    _4( '4' ),
    _3( '3' ),
    _2( '2' );

    private final char c;

    Card( char c )
    {
        this.c = c;
    }

    public static Card parse( char c )
    {
        for ( Card card : Card.values( ) )
        {
            if ( card.c == c )
            {
                return card;
            }
        }

        throw new IllegalArgumentException( "Unknown card type: " + c );
    }
}
