package com.github.ttran17.day12;

import java.util.Arrays;
import java.util.List;

public class ScabEngineer
{

    protected static long solve( char[] states, int damageGroup, int damageIndex )
    {
        if ( states[0] == '?' )
        {
            char[] branchA = Arrays.copyOf( states, states.length );
            branchA[0] = '.';
            char[] branchB = Arrays.copyOf( states, states.length );
            branchB[0] = '#';

            //return solve( branchA, damageGroups ) + solve( branchB, damageGroups ) ;
        }
        return 0;
    }

    public record OriginalRow( String rep, char[] states, int[] damageCount, char[][] damageGroups ) { }

    protected static OriginalRow unfold( String line, int unfoldCount )
    {
        String[] tokens = line.trim( ).split( " " );

        String unfoldedStateTokens = optimize( tokens[0].trim( ) );
        String unfoldedDamagedTokens = tokens[1];
        for ( int k = 0; k < unfoldCount; k++ )
        {
            unfoldedStateTokens = unfoldedStateTokens + "?" + tokens[0];
            unfoldedDamagedTokens = unfoldedDamagedTokens + "," + tokens[1];
        }

        char[] states = new char[unfoldedStateTokens.length( )];
        for ( int index = 0; index < unfoldedStateTokens.length( ); index++ )
        {
            states[index] = unfoldedStateTokens.charAt( index );
        }

        String[] damageTokens = unfoldedDamagedTokens.split( "," );
        int[] damageCount = new int[damageTokens.length];
        char[][] damageGroups = new char[damageTokens.length][];
        for ( int d = 0; d < damageTokens.length; d++ )
        {
            damageCount[d] = Integer.parseInt( damageTokens[d] );
            for ( int k = 0; k < damageCount[d]; k++ )
            {
                damageGroups[d][k] = '#';
            }
        }

        return new OriginalRow( unfoldedStateTokens, states, damageCount, damageGroups );
    }

    protected static String optimize( String line )
    {
        return line.replaceAll( "^\\.*|\\.*$", "" ).replaceAll( "\\.\\.+", "." );
    }
}
