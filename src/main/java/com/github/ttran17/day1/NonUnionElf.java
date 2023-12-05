package com.github.ttran17.day1;

import java.util.List;

public class NonUnionElf
{
    protected record NumberKey(String number, int value)
    {
    }

    protected static int consumeForwards( String line, List<NumberKey> keys )
    {
        for ( int i = 0; i < line.length( ); i++ )
        {
            for ( NumberKey key : keys )
            {

                if ( i + key.number.length( ) <= line.length( ) && line.substring( i, i + key.number.length( ) ).equalsIgnoreCase( key.number ) )
                {
                    return key.value;
                }
            }
        }

        throw new IllegalStateException( "Could not find a number key in " + line );
    }

    protected static int consumeBackwards( String line, List<NumberKey> keys )
    {

        for ( int i = line.length( ); i >= 1; i-- )
        {
            for ( NumberKey key : keys )
            {
                if ( i - key.number.length( ) >= 0 && line.substring( i - key.number.length( ), i ).equalsIgnoreCase( key.number ) )
                {
                    return key.value;
                }
            }
        }

        throw new IllegalStateException( "Could not find a number key in " + line );
    }

    protected static int consume( List<String> lines, List<NumberKey> keys )
    {
        int sum = 0;
        for ( String line : lines )
        {
            int forward = consumeForwards( line, keys );
            int backward = consumeBackwards( line, keys );

            sum += Integer.parseInt( String.valueOf( forward ) + String.valueOf( backward ) );
        }
        return sum;
    }
}
