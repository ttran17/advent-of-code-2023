package com.github.ttran17.day1;

public class Elf
{
    private final char[] key;

    private final int value;

    private int index;

    protected Elf( char[] key, int value )
    {
        this.key = key;
        this.value = value;
    }

    protected boolean consume( char c )
    {
        int previousIndex = index;

        if ( c == key[index] )
        {
            index++;
        }

        if ( index == key.length )
        {
            // Matched everything
            return true;
        }

        if ( index > previousIndex )
        {
            // Matching but not complete
            return false;
        }

        if ( key.length > 1 && c == key[0] )
        {
            // Matching stopped on word but restarted on first key
            index = 1;
            return false;
        }

        // No matching
        index = 0;
        return false;
    }

    protected void reset( )
    {
        index = 0;
    }

    protected int getValue( )
    {
        return value;
    }
}
