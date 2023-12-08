package com.github.ttran17.day8;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.primes.Primes;

public class Network
{
    protected final Map<Node, Node.Child> network = new LinkedHashMap<>( );

    protected final String hop;
    protected final int hopAround;

    protected Network( List<String> lines )
    {
        hop = lines.get( 0 );
        hopAround = hop.length( );

        for ( int i = 2; i < lines.size( ); i++ )
        {
            Node.Layout layout = Node.parse( lines.get( i ) );
            network.put( layout.parent( ), layout.child( ) );
        }
    }

    protected interface StopCondition
    {
        boolean accept( Node node );
    }

    protected long traverse( Node start, StopCondition stopCondition )
    {
        int hopIndex = 0;
        int masterHopIndex = hopIndex + 1;

        Node current = start;
        char currentHop = hop.charAt( hopIndex );

        Node location = network.get( current ).select( currentHop );
        while ( !stopCondition.accept( location ) )
        {
            hopIndex = hopIndex + 1 < hopAround ? hopIndex + 1 : 0;
            masterHopIndex++;

            current = location;
            currentHop = hop.charAt( hopIndex );

            location = network.get( current ).select( currentHop );
        }

        return masterHopIndex;
    }

    protected long traverseAsGhost( char startChar, char endChar )
    {
        List<Node> startingNodes = network.keySet( ).stream( ).filter( k -> k.endsWith( startChar ) ).collect( Collectors.toList( ) );

        long[] masterHops = new long[startingNodes.size( )];
        for ( int i = 0; i < startingNodes.size( ); i++ )
        {
            Node start = startingNodes.get( i );
            masterHops[i] = traverse( start, ( location ) -> location.endsWith( endChar ) );
        }

        Set<Integer> commonPrimes = new LinkedHashSet<>( );
        for ( long masterHop : masterHops )
        {
            commonPrimes.addAll( Primes.primeFactors( ( int ) masterHop ) );
        }

        long product = 1L;
        for ( int prime : commonPrimes )
        {
            product *= prime;
        }
        return product;
    }

    protected static long traverse( List<String> lines )
    {
        Network network = new Network( lines );
        Node start = Node.parseLocation( "AAA" );
        Node end = Node.parseLocation( "ZZZ" );
        return network.traverse( start, ( location ) -> location == end );
    }

    protected static long traverseAsGhost( List<String> lines )
    {
        Network network = new Network( lines );
        return network.traverseAsGhost( 'A', 'Z' );
    }
}
