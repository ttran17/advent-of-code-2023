package com.github.ttran17.day19;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.ttran17.RegexUtils;

public class RangeWorkflow
{
    protected final String name;

    protected final List<RangeOperation> operations = new ArrayList<>( );

    protected final String defaultWorkflowName;

    protected final String rep;

    protected RangeWorkflow( String line )
    {
        // px{a<2006:qkq,m>2090:A,rfg}
        int braceIndex = line.indexOf( "{" );
        name = line.substring( 0, braceIndex );

        // a<2006:qkq,m>2090:A,rfg
        String[] tokens = RegexUtils.removeCurlyBraces( line.substring( braceIndex ) ).split( "," );
        defaultWorkflowName = tokens[tokens.length - 1];

        for ( int k = 0; k < tokens.length - 1; k++ )
        {
            // a<2006:qkq
            operations.add( new RangeOperation( tokens[k] ) );
        }

        this.rep = line;
    }

    protected List<Input> apply( RangeRating rating )
    {
        List<Input> nextInputs = new ArrayList<>( );

        Iterator<RangeOperation> iterator = operations.iterator( );

        while ( iterator.hasNext( ) )
        {
            RangeOperation operation = iterator.next( );
            RangeOperationResult result = operation.apply( rating );

            if ( !result.passed( ).isEmpty( ) )
            {
                nextInputs.add( new Input( result.passed( ), operation.workflowName ) );
            }

            rating = result.failed( );
        }

        if ( !rating.isEmpty( ) )
        {
            nextInputs.add( new Input( rating, defaultWorkflowName ) );
        }

        return nextInputs;
    }

    @Override
    public String toString( )
    {
        return rep;
    }

    protected record Input( RangeRating rating, String workflowName ) { }
}
