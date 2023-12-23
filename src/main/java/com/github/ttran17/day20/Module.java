package com.github.ttran17.day20;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.github.ttran17.day20.Breadboard.Event;
import com.github.ttran17.day20.Breadboard.Signal;

public class Module
{
    protected enum State
    {
        OFF,
        ON
    }

    protected static final char FLIPFLOP = '%';
    protected static final char CONJUNCTION = '&';

    protected static final char BUTTON = '!';
    protected static final char BROADCAST = '<';
    protected static final char OUTPUT = '*';

    protected final String id;

    protected final char type;

    protected final Set<Module> destinations = new LinkedHashSet<>( );

    protected final Breadboard breadboard;

    protected Module( String id, char type, Breadboard breadboard )
    {
        this.id = id;
        this.type = type;
        this.breadboard = breadboard;
    }

    protected boolean addDestination( Module destination )
    {
        return destinations.add( destination );
    }

    protected void transmit( Signal signal )
    {
        breadboard.offer( new Event( new Packet( this, signal ), new ArrayList<>( destinations ) ) );
    }

    protected void process( Packet packet )
    {

    }

    @Override
    public String toString( )
    {
        String prefix = type == BROADCAST || type == OUTPUT ? "" : Character.toString( type );
        List<String> destinationIds = destinations.stream( ).map( d -> d.id ).collect( Collectors.toList( ) );
        return String.format( "%s%s -> %s", prefix, id, StringUtils.join( destinationIds, ", " ) );
    }

    protected record Packet( Module sender, Signal signal ) { }
}
