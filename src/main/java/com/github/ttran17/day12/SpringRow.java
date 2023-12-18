package com.github.ttran17.day12;

import java.util.ArrayList;
import java.util.List;

public class SpringRow
{

    public enum SpringState
    {
        operational( '.' ),
        unknown( '?' ),
        damaged( '#' ),
        rumsfeld( '*' );

        private final char c;

        SpringState( char c )
        {
            this.c = c;
        }

        public static SpringState parse( char c )
        {
            for ( SpringState springState : SpringState.values( ) )
            {
                if ( springState.c == c )
                {
                    return springState;
                }
            }

            throw new IllegalArgumentException( "Unable to parse: " + c );
        }

        @Override
        public String toString( )
        {
            return Character.toString( c );
        }
    }

    protected final SpringState[] states;
    protected final int[] damageCount;

    protected final int[] rootIndex;
    protected final int[] damageGroupIndex;

    public SpringRow( SpringState[] states, int[] damageCount, int[] rootIndex,int[] damageGroupIndex )
    {
        this.states = states;
        this.damageCount = damageCount;
        this.rootIndex = rootIndex;
        this.damageGroupIndex = damageGroupIndex;
    }

    protected static List<SpringRow> parse( List<String> lines )
    {
        List<SpringRow> springRows = new ArrayList<>( );

        for ( int row = 0; row < lines.size( ); row++ )
        {
            springRows.add( parse( lines.get( row ) ) );
        }

        return springRows;
    }

    protected static SpringRow parse( String line )
    {
        String[] tokens = line.trim( ).split( " " );

        String stateTokens = tokens[0];
        SpringState[] states = new SpringState[stateTokens.length( )];
        for ( int index = 0; index < stateTokens.length( ); index++ )
        {
            states[index] = SpringState.parse( stateTokens.charAt( index ) );
        }

        String[] damageTokens = tokens[1].split( "," );
        int[] damageCount = new int[damageTokens.length];
        for ( int d = 0; d < damageTokens.length; d++ )
        {
            damageCount[d] = Integer.parseInt( damageTokens[d] );
        }

        return new SpringRow( states, damageCount, new int[0], new int[0] );
    }

    protected static List<SpringRow> unfold( List<String> lines, int unfoldCount )
    {
        List<SpringRow> springRows = new ArrayList<>( );

        for ( int row = 0; row < lines.size( ); row++ )
        {
            springRows.add( unfold( lines.get( row ), unfoldCount ) );
        }

        return springRows;
    }

    protected static SpringRow unfold( String line, int unfoldCount )
    {
        String[] tokens = line.trim( ).split( " " );

        String unfoldedStateTokens = tokens[0];
        String unfoldedDamagedTokens = tokens[1];
        for ( int k = 0; k < unfoldCount; k++ )
        {
            unfoldedStateTokens = unfoldedStateTokens + "?" + tokens[0];
            unfoldedDamagedTokens = unfoldedDamagedTokens + "," + tokens[1];
        }

        int[] rootIndex = new int[unfoldCount + 1];
        rootIndex[0] = -1;
        for ( int k = 1; k <= unfoldCount; k++ )
        {
            rootIndex[k] = (rootIndex[k-1] + 1) + tokens[0].length();
        }

        SpringState[] states = new SpringState[unfoldedStateTokens.length( )];
        for ( int index = 0; index < unfoldedStateTokens.length( ); index++ )
        {
            states[index] = SpringState.parse( unfoldedStateTokens.charAt( index ) );
        }

        String[] damageTokens = unfoldedDamagedTokens.split( "," );
        int[] damageCount = new int[damageTokens.length];
        for ( int d = 0; d < damageTokens.length; d++ )
        {
            damageCount[d] = Integer.parseInt( damageTokens[d] );
        }

        int[] damageIndex = new int[unfoldCount + 1];
        damageIndex[0] = 0;
        int nDamageTokens = tokens[1].split(",").length;
        for ( int k = 1; k <= unfoldCount; k++ )
        {
            damageIndex[k] = damageIndex[k-1] + nDamageTokens;
        }

        return new SpringRow( states, damageCount, rootIndex, damageIndex );
    }

}
