package com.github.ttran17.day20;

public class FlipFlop extends Module
{
    protected State state;

    protected FlipFlop( String id, char type, Breadboard breadboard )
    {
        super( id, type, breadboard );

        state = State.OFF;

    }

    @Override
    protected void process( Packet packet )
    {
        Breadboard.Signal signal = packet.signal( );

        if ( signal == Breadboard.Signal.HIGH )
        {
            return;
        }

        // signal == Signal.LOW

        if ( state == State.OFF )
        {
            transmit( Breadboard.Signal.HIGH );
            state = State.ON;
            return;
        }

        if ( state == State.ON )
        {
            transmit( Breadboard.Signal.LOW );
            state = State.OFF;
        }
    }
}
