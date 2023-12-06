package com.github.ttran17.day5;

import java.util.ArrayList;
import java.util.List;

public class Chunk
{
    protected final Chunk parent;

    protected final List<Chunk> children;

    protected final GardenItem type;
    protected final long start;
    protected final long end;

    public Chunk( Chunk parent, GardenItem type, long start, long end )
    {
        this.parent = parent;
        this.children = new ArrayList<>( );
        this.type = type;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString( )
    {
        return String.format( "%s: [%d,%d]", type, start, end );
    }
}
