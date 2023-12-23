package com.github.ttran17.day20;

import java.util.List;

public class Broadcaster extends Module
{
    protected Broadcaster( String id, char type, Breadboard breadboard )
    {
        super( id, type, breadboard );
    }

    @Override
    protected void process( Packet packet )
    {
        for ( Module destination : destinations )
        {
            breadboard.offer( new Breadboard.Event( new Packet( this, packet.signal( ) ), List.of( destination ) ) );
        }
    }
}
