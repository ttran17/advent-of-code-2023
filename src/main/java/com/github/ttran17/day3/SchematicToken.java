package com.github.ttran17.day3;

public abstract class SchematicToken
{
    private final IndexRange indexRange;
    private final boolean isSymbol;

    protected SchematicToken( IndexRange indexRange, boolean isSymbol )
    {
        this.indexRange = indexRange;
        this.isSymbol = isSymbol;
    }

    protected IndexRange getIndexRange( )
    {
        return indexRange;
    }

    protected boolean isSymbol( )
    {
        return isSymbol;
    }

    protected static class Number extends SchematicToken
    {

        private final int value;

        public Number( IndexRange indexRange, int value )
        {
            super( indexRange, false );

            this.value = value;
        }

        public int getValue( )
        {
            return value;
        }
    }

    protected static class Symbol extends SchematicToken
    {

        private final char symbol;

        public Symbol( IndexRange indexRange, char symbol )
        {
            super( indexRange, true );

            this.symbol = symbol;
        }

        public char getSymbol( )
        {
            return symbol;
        }
    }
}
