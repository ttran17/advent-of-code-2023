package com.github.ttran17.day25;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

public class WiringDiagram
{
    protected final Map<String, Node> nodeMap = new LinkedHashMap<>( );

    protected final Graph<Node, DefaultEdge> graph = new DefaultUndirectedGraph<>( DefaultEdge.class );

    protected WiringDiagram( List<String> lines )
    {
        for ( String line : lines )
        {
            int colonIndex = line.indexOf( ":" );
            String key = line.substring( 0, colonIndex );
            String[] values = line.substring( colonIndex + 1 ).trim( ).split( " " );

            Node source = nodeMap.computeIfAbsent( key, Node::new );
            graph.addVertex( source );
            for ( String value : values )
            {
                Node target = nodeMap.computeIfAbsent( value, Node::new );
                graph.addVertex( target );
                graph.addEdge( source, target );
            }
        }
    }

    protected void stats( )
    {
        Map<Integer, List<Node>> degreeMap = new TreeMap<>( );
        for ( Node v : graph.vertexSet( ) )
        {
            int degree = Graphs.neighborListOf( graph, v ).size( );
            degreeMap.computeIfAbsent( degree, ArrayList::new ).add( v );
        }
        {
            int sum = 0;
            for ( Map.Entry<Integer, List<Node>> entry : degreeMap.entrySet( ) )
            {
                sum += entry.getKey( ) * entry.getValue( ).size( );
                System.out.println( String.format( "Degree %d : %d vertices", entry.getKey( ), entry.getValue( ).size( ) ) );
            }
            System.out.println( String.format( "|E| = %d, sum(degree)/2 = %d", graph.edgeSet( ).size( ), sum / 2 ) );
        }

        Node degree10 = degreeMap.get( 10 ).get( 0 );
        DijkstraShortestPath<Node, DefaultEdge> dijkstra = new DijkstraShortestPath<>( graph );
        Map<DefaultEdge, Integer> edgeCount = new HashMap<>( );
        Map<Integer, Integer> pathLengthCount = new TreeMap<>( );
        for ( Node v : graph.vertexSet( ) )
        {
            if ( v == degree10 )
            {
                continue;
            }

            GraphPath<Node, DefaultEdge> path = dijkstra.getPath( degree10, v );
            for ( DefaultEdge edge : path.getEdgeList( ) )
            {
                int count = edgeCount.getOrDefault( edge, 0 ) + 1;
                edgeCount.put( edge, count );
            }
            {
                int count = pathLengthCount.getOrDefault( path.getLength( ), 0 ) + 1;
                pathLengthCount.put( path.getLength( ), count );
            }
        }
        {
            List<Map.Entry<DefaultEdge, Integer>> edgeCounts = new ArrayList<>( edgeCount.entrySet( ).stream( ).toList( ) );
            edgeCounts.sort( Comparator.comparingInt( Map.Entry::getValue ) );
            Collections.reverse( edgeCounts );
            for ( Map.Entry<DefaultEdge, Integer> entry : edgeCounts )
            {
                System.out.println( entry.getKey( ) + ": " + entry.getValue( ) );
            }
        }
        for ( Map.Entry<Integer, Integer> entry : pathLengthCount.entrySet( ) )
        {
            System.out.println( String.format( "Path length %d : %d instances", entry.getKey( ), entry.getValue( ) ) );
        }
    }

    protected record Node( String id ) { }
}
