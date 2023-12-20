package com.github.ttran17.day19;

import java.util.ArrayList;
import java.util.List;

import com.github.ttran17.RegexUtils;

public class Workflow
{
    protected final String name;

    protected final List<Operation> operations = new ArrayList<>( );

    protected final String defaultWorkflowName;

    protected final String rep;

    protected Workflow( String line )
    {
        // px{a<2006:qkq,m>2090:A,rfg}
        int braceIndex = line.indexOf( "{" );
        name = line.substring( 0, braceIndex );

        // a<2006:qkq,m>2090:A,rfg
        String[] tokens = RegexUtils.removeCurlyBraces( line.substring( braceIndex ) ).split( "," );
        defaultWorkflowName = tokens[tokens.length - 1];
        if ( tokens.length < 2 )
        {
            throw new IllegalStateException( "Tokens shortage ..." );
        }
        for ( int k = 0; k < tokens.length - 1; k++ )
        {
            // a<2006:qkq
            operations.add( new Operation( tokens[k] ) );
        }

        this.rep = line;
    }

    protected String apply( Rating rating )
    {
        for ( Operation operation : operations )
        {
            if ( operation.apply( rating ) )
            {
                return operation.workflowName;
            }
        }

        return defaultWorkflowName;
    }

    @Override
    public String toString( )
    {
        return rep;
    }
}
