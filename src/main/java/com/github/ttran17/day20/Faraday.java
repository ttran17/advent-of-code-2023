package com.github.ttran17.day20;

import static com.github.ttran17.day20.Breadboard.Signal;
import static com.github.ttran17.day20.Breadboard.crucialMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Faraday
{

    protected static long analyze( List<String> lines )
    {
        Breadboard breadboard = new Breadboard( );

        preload( lines, breadboard );

        load( lines, breadboard );

        Module button = breadboard.modules.get( "button" );

        for ( int k = 0; k < 1000; k++ )
        {
            button.transmit( Signal.LOW );
            breadboard.process( );
        }

        return breadboard.low * breadboard.high;
    }

    protected static long analyzeRx( List<String> lines )
    {
        Breadboard breadboard = new Breadboard( );

        preload( lines, breadboard );

        load( lines, breadboard );

        Module button = breadboard.modules.get( "button" );

        long k = 1;
        while ( true )
        {
            button.transmit( Signal.LOW );
            breadboard.process( k++ );
            if (crucialMap.size() == 4)
            {
                break;
            }
        }

        // In general, should find the least common multiple but all the numbers are relatively prime so lcm is the product in this case
        long product = 1;
        for ( Map.Entry<String,Long> entry : crucialMap.entrySet( ))
        {
            System.out.println(String.format("%s: %d", entry.getKey(), entry.getValue()));
            product *= entry.getValue();
        }

        return product;
    }

    protected static Map<String, Module> preload( List<String> lines, Breadboard breadboard )
    {

        Map<String, Module> modules = breadboard.modules;

        for ( String line : lines )
        {
            char prefix = line.charAt( 0 );

            if ( prefix == Module.FLIPFLOP || prefix == Module.CONJUNCTION )
            {
                String id = line.substring( 1, line.indexOf( " " ) );

                if ( prefix == Module.FLIPFLOP )
                {
                    modules.computeIfAbsent( id, k -> new FlipFlop( id, Module.FLIPFLOP, breadboard ) );
                }
                else
                {
                    modules.computeIfAbsent( id, k -> new Conjunction( id, Module.CONJUNCTION, breadboard ) );
                }
            }
            else
            {
                String id = line.substring( 0, line.indexOf( " " ) );
                modules.computeIfAbsent( id, k -> new Broadcaster( id, Module.BROADCAST, breadboard ) );
            }
        }

        String button = "button";
        modules.computeIfAbsent( button, k -> new Module( button, Module.BUTTON, breadboard ) );

        String output = "output";
        modules.computeIfAbsent( output, k -> new Module( output, Module.OUTPUT, breadboard ) );

        return modules;
    }

    protected static void load( List<String> lines, Breadboard breadboard )
    {
        Map<String, Module> modules = breadboard.modules;

        for ( String line : lines )
        {
            char prefix = line.charAt( 0 );
            int idStart = prefix == Module.FLIPFLOP || prefix == Module.CONJUNCTION ? 1 : 0;
            String id = line.substring( idStart, line.indexOf( " " ) );
            Module module = modules.get( id );

            int arrowIndex = line.indexOf( '>' );
            String[] destinationIds = line.substring( arrowIndex + 1 ).trim( ).split( ", " );
            Arrays.stream( destinationIds )
                  .filter( key -> !modules.containsKey( key ) )
                  .forEach( key -> modules.put( key, new Module( key, Module.OUTPUT, breadboard ) ) );
            List<Module> destinations = Arrays.stream( destinationIds ).map( key -> modules.get( key ) ).collect( Collectors.toList( ) );
            destinations.forEach( module::addDestination );
            destinations.stream( )
                        .filter( d -> d instanceof Conjunction )
                        .map( d -> ( Conjunction ) d )
                        .forEach( c -> c.addInput( module ) );
        }

        modules.get( "button" ).addDestination( modules.get( "broadcaster" ) );
    }

}
