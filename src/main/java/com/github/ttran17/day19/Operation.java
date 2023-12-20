package com.github.ttran17.day19;

import com.github.ttran17.ElfUtils;

public class Operation
{

    protected final char c;

    protected final char operation;

    protected final int bound;

    protected final String workflowName;

    protected Operation( String line )
    {
        // a<2006:qkq
        c = line.charAt( 0 );
        int opIndex = line.indexOf( '<' );
        if ( opIndex < 0 )
        {
            opIndex = line.indexOf( '>' );
        }
        operation = line.charAt( opIndex );

        int colonIndex = ElfUtils.getColonIndex( line );
        bound = Integer.parseInt( line.substring( opIndex + 1, colonIndex ) );
        workflowName = line.substring( colonIndex + 1 );
    }

    protected boolean apply( Rating rating )
    {
        if ( rating.contains( c ) )
        {
            if ( operation == '<' )
            {
                return rating.get( c ) < bound;
            }

            return rating.get( c ) > bound;
        }

        return false;
    }
}
