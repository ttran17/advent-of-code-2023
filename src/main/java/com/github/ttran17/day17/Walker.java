package com.github.ttran17.day17;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import com.github.ttran17.util.Direction;

public class Walker
{
    public enum Axis
    {
        HORIZONTAL,
        VERTICAL
    }

    protected static long walk( List<String> lines, int minSteps, int maxSteps )
    {
        City city = new City( lines );
        return walk( city, minSteps, maxSteps );
    }

    protected static long walk( City city, int minSteps, int maxSteps )
    {
        Queue<HeapNode> queue = new PriorityQueue<>( Comparator.comparingLong( o -> o.cost ) );
        queue.offer( new HeapNode( 0, 0, 0, Axis.HORIZONTAL ) );
        queue.offer( new HeapNode( 0, 0, 0, Axis.VERTICAL ) );

        Set<Node> visited = new HashSet<>( );
        while ( !queue.isEmpty( ) )
        {
            HeapNode heapNode = queue.poll( );

            if ( heapNode.node.row == city.nRows - 1 && heapNode.node.col == city.nCols - 1 )
            {
                return heapNode.cost;
            }

            if ( visited.add( heapNode.node ) )
            {
                processNeighbors( city, heapNode, queue, visited, minSteps, maxSteps );
            }
        }

        throw new IllegalStateException( "Something has gone very wrong!" );
    }

    /**
     * Like Day 16 but we use a priority queue instead of a regular queue. And 'walkers' instead of 'bad robot'. But it's the same idea.
     */
    protected static void processNeighbors( City city, HeapNode heapNode, Queue<HeapNode> queue, Set<Node> visited, int minSteps, int maxSteps )
    {
        List<Direction> directions = heapNode.node.axis == Axis.VERTICAL ? List.of( Direction.EAST, Direction.WEST ) : List.of( Direction.SOUTH, Direction.NORTH );
        Axis axis = heapNode.node.axis == Axis.VERTICAL ? Axis.HORIZONTAL : Axis.VERTICAL;

        for ( Direction direction : directions )
        {
            int cost = heapNode.cost;
            int row = heapNode.node.row;
            int col = heapNode.node.col;
            for ( int step = 0; step < maxSteps; step++ )
            {
                row = row + direction.dRow( );
                col = col + direction.dCol( );

                if ( !city.isValidLocation( row, col ) )
                {
                    break;
                }

                cost = cost + city.layout[row][col];

                if ( visited.contains( new Node( row, col, axis ) ) )
                {
                    continue;
                }

                if ( step >= minSteps - 1 )
                {
                    queue.offer( new HeapNode( cost, row, col, axis ) );
                }
            }
        }
    }

    protected record Node( int row, int col, Axis axis ) { }

    protected static class HeapNode
    {
        protected int cost;
        protected final Node node;

        public HeapNode( int cost, int row, int col, Axis axis )
        {
            this.cost = cost;
            this.node = new Node( row, col, axis );
        }

        @Override
        public boolean equals( Object o )
        {
            if ( o == null )
            {
                return false;
            }

            if ( !this.getClass( ).equals( o.getClass( ) ) )
            {
                return false;
            }

            HeapNode other = ( HeapNode ) o;
            return cost == other.cost && node.equals( other.node );
        }

        @Override
        public int hashCode( )
        {
            return 31 * node.hashCode( ) + cost;
        }
    }
}
