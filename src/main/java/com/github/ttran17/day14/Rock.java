package com.github.ttran17.day14;

public class Rock
{
    private int row;
    private int col;

    private RockType type;

    public Rock( int row, int col, RockType type )
    {
        this.row = row;
        this.col = col;
        this.type = type;
    }

    protected int getRow( )
    {
        return row;
    }

    protected void setRow( int row )
    {
        this.row = row;
    }

    protected int getCol( )
    {
        return col;
    }

    protected void setCol( int col )
    {
        this.col = col;
    }

    protected RockType getType( )
    {
        return type;
    }

    @Override
    public String toString( )
    {
        return type.toString( );
    }
}
