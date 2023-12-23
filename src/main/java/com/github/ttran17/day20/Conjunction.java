package com.github.ttran17.day20;

import java.util.LinkedHashMap;
import java.util.Map;

import com.github.ttran17.day20.Breadboard.Signal;

public class Conjunction extends Module
{
    protected final Map<Module, Signal> memory = new LinkedHashMap<>( );

    protected Conjunction( String id, char type, Breadboard breadboard )
    {
        super( id, type, breadboard );
    }

    protected void addInput( Module module )
    {
        memory.put( module, Signal.LOW );
    }


    @Override
    protected void process( Packet packet )
    {
        // When a pulse is received, the conjunction module first updates its memory for that input.
        // Then, if it remembers high pulses for all inputs, it sends a low pulse;
        // otherwise, it sends a high pulse.

        Module sender = packet.sender( );
        Signal signal = packet.signal( );

        memory.put( sender, signal );

        for ( Signal recent : memory.values( ) )
        {
            if ( recent == Signal.LOW )
            {
                transmit( Signal.HIGH );
                return;
            }
        }

        transmit( Signal.LOW );
    }
}
