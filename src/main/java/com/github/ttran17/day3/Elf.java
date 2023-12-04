package com.github.ttran17.day3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Elf
{
    // By visual inspection of input.txt for this day
    protected static int maxTokenLength = 10;

    protected static List<SchematicToken> consume( String line )
    {
        List<SchematicToken> schematicTokens = new ArrayList<>( );

        SavedToken savedToken = new SavedToken( );
        for ( int i = 0; i < line.length( ); i++ )
        {
            char c = line.charAt( i );

            if ( !isDigit( c ) )
            {
                // Save current char if warranted
                if ( c != '.' )
                {
                    IndexRange indexRange = new IndexRange( i, i );
                    schematicTokens.add( new SchematicToken.Symbol( indexRange, c ) );
                }

                // Save savedToken if warranted
                if ( !savedToken.isEmpty( ) )
                {
                    // Process the schematic number saved so far
                    schematicTokens.add( savedToken.toSchematicNumber( i - 1 ) );
                }

                // Reset
                savedToken = new SavedToken( );
            }
            else
            {
                // Got a digit so tack it onto savedToken
                savedToken.addChar( c, i );
            }
        }

        // Process any schematic numbers at the end of a line
        if ( !savedToken.isEmpty( ) )
        {
            schematicTokens.add( savedToken.toSchematicNumber( line.length( ) - 1 ) );
        }

        return schematicTokens;
    }

    protected static boolean isDigit( char c )
    {
        return c >= '0' && c <= '9';
    }

    protected static class SavedToken
    {
        private final char[] savedToken = new char[maxTokenLength];
        private int nextSavedTokenIndex = 0;
        private int savedTokenStartIndex = -1;

        protected boolean isEmpty( )
        {
            return nextSavedTokenIndex == 0;
        }

        protected void addChar( char c, int currentIndex )
        {
            if ( nextSavedTokenIndex == 0 )
            {
                savedTokenStartIndex = currentIndex;
            }
            savedToken[nextSavedTokenIndex++] = c;
        }

        protected SchematicToken.Number toSchematicNumber( int endIndex )
        {
            IndexRange indexRange = new IndexRange( savedTokenStartIndex, endIndex );
            char[] actual = Arrays.copyOf( savedToken, nextSavedTokenIndex );
            int value = Integer.parseInt( String.valueOf( actual ) );
            return new SchematicToken.Number( indexRange, value );
        }
    }
}
