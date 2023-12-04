package com.github.ttran17.day1;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Workshop
{

    protected static List<Elf> createWorkers( List<String> goals )
    {
        List<Elf> workers = new ArrayList<>( );
        for ( String goal : goals )
        {
            workers.add( createWorker( goal, Integer.parseInt( goal ) ) );
        }
        return workers;
    }

    protected static Elf createWorker( String goal, int value )
    {
        char[] key = new char[goal.length( )];
        for ( int i = 0; i < goal.length( ); i++ )
        {
            key[i] = goal.charAt( i );
        }
        return new Elf( key, value );
    }

    protected static int consume( List<String> lines )
    {
        List<String> goals = IntStream.rangeClosed( 1, 9 )
                                      .mapToObj( i -> String.valueOf( i ) )
                                      .collect( Collectors.toList( ) );

        List<Elf> workers = Workshop.createWorkers( goals );

        return consume( lines, workers, workers );
    }

    private static int consume( List<String> lines, List<Elf> forwardWorkers, List<Elf> backwardWorkers )
    {
        int sum = 0;
        for ( String line : lines )
        {
            int[] forwardIndices = IntStream.rangeClosed( 0, line.length( ) - 1 ).toArray( );
            int forward = Workshop.consume( line, forwardIndices, forwardWorkers );

            int[] backwardIndices = IntStream.iterate( line.length( ) - 1, x -> x >= 0, x -> x - 1 ).toArray( );
            int backward = Workshop.consume( line, backwardIndices, backwardWorkers );

            sum += Integer.parseInt( String.format( "%d%d", forward, backward ) );
        }
        return sum;
    }

    protected static int consume( String line, int[] indices, List<Elf> elves )
    {
        for ( Elf elf : elves )
        {
            elf.reset( );
        }

        for ( int i : indices )
        {
            for ( Elf elf : elves )
            {
                char c = line.charAt( i );
                if ( elf.consume( c ) )
                {
                    return elf.getValue( );
                }
            }
        }

        throw new IllegalStateException( "Did not find a value!" );
    }

    protected static List<Elf> createWorkers( List<String> goals, int[] values )
    {
        if ( goals.size( ) != values.length )
        {
            throw new IllegalArgumentException( String.format( "Goals size %d != values.length %d", goals.size( ), values.length ) );
        }

        List<Elf> workers = new ArrayList<>( );
        for ( int k = 0; k < goals.size( ); k++ )
        {
            workers.add( createWorker( goals.get( k ), values[k] ) );
        }
        return workers;
    }

    protected static int consumeDigitsAndWords( List<String> lines )
    {
        List<String> digits = IntStream.rangeClosed( 1, 9 )
                                       .mapToObj( i -> String.valueOf( i ) )
                                       .collect( Collectors.toList( ) );

        List<String> words = List.of( "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" );
        List<String> wordsReversed = words.stream( )
                                          .map( w ->
                                                {
                                                    StringBuilder builder = new StringBuilder( w );
                                                    builder.reverse( );
                                                    return builder.toString( );
                                                } )
                                          .toList( );

        List<Elf> digitWorkers = Workshop.createWorkers( digits );
        int[] values = IntStream.rangeClosed( 1, 9 ).toArray( );

        List<Elf> forwardWorkers = new ArrayList<>( digitWorkers );
        forwardWorkers.addAll( Workshop.createWorkers( words, values ) );

        List<Elf> backwardWorkers = new ArrayList<>( digitWorkers );
        backwardWorkers.addAll( createWorkers( wordsReversed, values ) );

        return consume( lines, forwardWorkers, backwardWorkers );
    }
}
