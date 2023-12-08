package com.github.ttran17.day8;

import java.util.LinkedHashMap;
import java.util.Map;

public class Node
{
    private static final Map<String, Node> singletons = new LinkedHashMap<>( );
    private final String rep;

    private Node( String rep )
    {
        this.rep = rep;
    }

    protected boolean endsWith( char c )
    {
        return rep.charAt( 2 ) == c;
    }

    @Override
    public String toString( )
    {
        return rep;
    }

    protected static Node parseLocation( String location )
    {
        return singletons.computeIfAbsent( location, Node::new );
    }

    protected static Layout parse( String layout )
    {
        String parentString = layout.trim( ).substring( 0, 3 );
        String childString = layout.split( "=" )[1].trim( );
        String leftString = childString.substring( 1, 4 );
        String rightString = childString.substring( 6, 9 );

        Node parent = singletons.computeIfAbsent( parentString, Node::new );
        Node left = singletons.computeIfAbsent( leftString, Node::new );
        Node right = singletons.computeIfAbsent( rightString, Node::new );

        return new Layout( parent, new Child( left, right ) );
    }

    protected record Child(Node left, Node right)
    {
        Node select( char hop )
        {
            return hop == 'L' ? left : right;
        }
    }

    protected record Layout(Node parent, Child child)
    {
    }

}
