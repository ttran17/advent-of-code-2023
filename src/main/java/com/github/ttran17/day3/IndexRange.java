package com.github.ttran17.day3;

public class IndexRange
{
    private final int start;
    private final int end;

    public IndexRange( int start, int end )
    {
        this.start = start;
        this.end = end;
    }

    public boolean isAdjacent( IndexRange other )
    {
        return other.start <= end + 1 && start - 1 <= other.end;
    }
}
