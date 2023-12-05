package com.github.ttran17;

public class ElfUtils
{
    public static int getColonIndex( String line )
    {
        return line.indexOf( ':' );
    }

    public static int getPipeIndex( String line )
    {
        return line.indexOf( '|' );
    }
}
