package com.github.ttran17.day20;

import static com.github.ttran17.day20.Module.Packet;

import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Breadboard
{
    public enum Signal
    {
        HIGH,
        LOW
    }

    protected final Map<String, Module> modules = new LinkedHashMap<>( );

    protected final Queue<Event> eventQueue = new ArrayDeque<>( );

    protected long low = 0;
    protected long high = 0;

    protected void process( )
    {
        while ( !eventQueue.isEmpty( ) )
        {
            Event event = eventQueue.poll( );
            for ( Module module : event.destinations )
            {
                module.process( event.packet );
            }
        }
    }

    protected static String[] crucial = {"ln", "db", "vq", "tf"};
    protected static Map<String,Long> crucialMap = new LinkedHashMap<>(  );

    protected void process( long nButtonPresses )
    {
        while ( !eventQueue.isEmpty( ) )
        {
            Event event = eventQueue.poll( );
            for ( String key : crucial )
            {
                if ( event.packet.sender().id.equals( key ) && event.packet.signal() == Signal.HIGH )
                {
                    crucialMap.putIfAbsent( key, nButtonPresses );
                }
            }
            if ( crucialMap.size() == 4)
            {
                eventQueue.clear();
            }

            for ( Module module : event.destinations )
            {
                module.process( event.packet );
            }
        }
    }

    protected void offer( Event event )
    {

        if ( event.packet.signal( ) == Signal.LOW )
        {
            low += event.destinations.size( );
        }
        else
        {
            high += event.destinations.size( );
        }

        eventQueue.offer( event );
    }

    protected record Event( Packet packet, List<Module> destinations ) { }
}
