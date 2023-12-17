package com.github.ttran17.day12;

import java.util.ArrayList;
import java.util.Arrays;
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

    protected final int row;
    protected final SpringState[] states;
    protected final int[] damageCount;
    protected final int totalDamaged;

    public SpringRow( int row, SpringState[] states, int[] damageCount )
    {
        this.row = row;
        this.states = states;
        this.damageCount = damageCount;
        this.totalDamaged = Arrays.stream( damageCount ).sum( );
    }

    protected static List<SpringRow> parse( List<String> lines )
    {
        List<SpringRow> springRows = new ArrayList<>( );

        for ( int row = 0; row < lines.size( ); row++ )
        {
            springRows.add( parse( row, lines.get( row ) ) );
        }

        return springRows;
    }

    protected static SpringRow parse( int row, String line )
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

        return new SpringRow( row, states, damageCount );
    }

    protected static List<SpringRow> unfold( List<String> lines, int unfoldCount )
    {
        List<SpringRow> springRows = new ArrayList<>( );

        for ( int row = 0; row < lines.size( ); row++ )
        {
            springRows.add( unfold( row, lines.get( row ), unfoldCount ) );
        }

        return springRows;
    }

    protected static SpringRow unfold( int row, String line, int unfoldCount )
    {
        String[] tokens = line.trim( ).split( " " );

        String unfoldedStateTokens = tokens[0];
        String unfoldedDamagedTokens = tokens[1];
        for (int k = 0; k < unfoldCount; k++)
        {
            unfoldedStateTokens = unfoldedStateTokens + "?" + tokens[0];
            unfoldedDamagedTokens = unfoldedDamagedTokens + "," + tokens[1];
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

        return new SpringRow( row, states, damageCount );
    }
}
