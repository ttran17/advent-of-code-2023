package com.github.ttran17.day19;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.google.common.collect.Range;

import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;

public class RangeEscher
{

    protected static long distinct( List<String> lines )
    {
        Map<String, RangeWorkflow> workflowMap = new LinkedHashMap<>( );

        accumulate( workflowMap, lines );

        Char2ObjectMap<Range<Integer>> map = new Char2ObjectArrayMap<>( );
        map.put( 'x', Range.closed( 1, 4000 ) );
        map.put( 'm', Range.closed( 1, 4000 ) );
        map.put( 'a', Range.closed( 1, 4000 ) );
        map.put( 's', Range.closed( 1, 4000 ) );

        RangeRating rating = new RangeRating( map );

        return distinct( workflowMap, "in", rating );
    }

    protected static Queue<String> accumulate( Map<String, RangeWorkflow> workflowMap, List<String> lines )
    {
        Queue<String> queue = new ArrayDeque<>( lines );

        while ( !queue.isEmpty( ) )
        {
            String line = queue.poll( );

            if ( line.trim( ).isEmpty( ) )
            {
                break;
            }

            RangeWorkflow workflow = new RangeWorkflow( line );
            workflowMap.put( workflow.name, workflow );
        }

        return queue;
    }

    protected static long distinct( Map<String, RangeWorkflow> workflowMap, String initWorkflowName, RangeRating initRating )
    {
        ArrayDeque<RangeWorkflow.Input> deque = new ArrayDeque<>( );
        deque.push( new RangeWorkflow.Input( initRating, initWorkflowName ) );

        List<RangeRating> accepted = new ArrayList<>( );

        while ( !deque.isEmpty( ) )
        {
            RangeWorkflow.Input input = deque.pop( );

            RangeWorkflow workflow = workflowMap.get( input.workflowName( ) );
            RangeRating rating = input.rating( );

            List<RangeWorkflow.Input> nextInputs = workflow.apply( rating );
            for ( int i = nextInputs.size( ) - 1; i > -1; i-- )
            {
                String nextWorkflowName = nextInputs.get( i ).workflowName( );
                if ( nextWorkflowName.equals( "A" ) )
                {
                    accepted.add( nextInputs.get( i ).rating( ) );
                }
                else if ( !nextWorkflowName.equals( "R" ) )
                {
                    deque.push( nextInputs.get( i ) );
                }
            }
        }

        long sum = 0;
        for ( RangeRating rating : accepted )
        {
            sum += rating.distinct( );
        }
        return sum;
    }

}
