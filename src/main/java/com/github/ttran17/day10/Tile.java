package com.github.ttran17.day10;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Tile
{

    protected final int row;
    protected final int col;

    protected final PipeFitting pipeFitting;

    private final Tile[] neighbors;

    private int step;

    private boolean isLoop;

    private boolean isInside;

    private Tile next;

    protected Tile( int row, int col, PipeFitting pipeFitting )
    {
        this.row = row;
        this.col = col;
        this.pipeFitting = pipeFitting;
        this.neighbors = new Tile[4];

        this.step = -1;
    }

    public Tile getNext( )
    {
        return next;
    }

    public void setNext( Tile next )
    {
        this.next = next;
    }

    protected void checkNeighbor( Tile otherTile, Direction directionToOther )
    {
        Direction direction = this.pipeFitting.getConnection( otherTile.pipeFitting, directionToOther );
        if ( direction != null )
        {
            setNeighbor( direction, otherTile );
            otherTile.setNeighbor( direction.getPartner( ), this );
        }
    }

    protected Tile getNeighbor( Direction direction )
    {
        return neighbors[direction.ordinal( )];
    }

    protected List<Tile> getNeighbors( )
    {
        return Arrays.stream( neighbors ).collect( Collectors.toList( ) );
    }

    protected List<Tile> getValidNeighbors( )
    {
        return Arrays.stream( neighbors ).filter( Objects::nonNull ).collect( Collectors.toList( ) );
    }

    protected void setNeighbor( Direction direction, Tile neighbor )
    {
        neighbors[direction.ordinal( )] = neighbor;
    }

    public int getStep( )
    {
        return step;
    }

    /**
     * Only modifes this.step if it is greater than the given step (and step has not yet been set)
     */
    public void setStep( int step )
    {
        if ( this.step <= 0 )
        {
            this.step = step;
            return;
        }

        if ( this.step > 0 && step < this.step )
        {
            this.step = step;
        }
    }

    public boolean isLoop( )
    {
        return isLoop;
    }

    public void setLoop( boolean loop )
    {
        isLoop = loop;
    }

    public boolean isInside( )
    {
        return isInside;
    }

    public void setInside( boolean inside )
    {
        isInside = inside;
    }
}
