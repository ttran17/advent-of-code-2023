package com.github.ttran17.day19;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Escher
{

    protected static long accumulate( List<String> lines )
    {
        Map<String, Workflow> workflowMap = new LinkedHashMap<>( );

        Queue<String> queue = accumulate( workflowMap, lines );

        List<Rating> ratings = new ArrayList<>( );
        while ( !queue.isEmpty( ) )
        {
            String line = queue.poll( );
            ratings.add( new Rating( line ) );
        }

        return accumulate( workflowMap, "in", ratings );
    }

    protected static Queue<String> accumulate( Map<String, Workflow> workflowMap, List<String> lines )
    {
        Queue<String> queue = new ArrayDeque<>( lines );

        while ( !queue.isEmpty( ) )
        {
            String line = queue.poll( );

            if ( line.trim( ).isEmpty( ) )
            {
                break;
            }

            Workflow workflow = new Workflow( line );
            workflowMap.put( workflow.name, workflow );
        }

        return queue;
    }

    protected static long accumulate( Map<String, Workflow> workflowMap, String workflowName, List<Rating> ratings )
    {

        long sum = 0;

        for ( Rating rating : ratings )
        {
            if ( accept( workflowMap, workflowName, rating ) )
            {
                sum += rating.totalRatings( );
            }
        }

        return sum;
    }

    protected static boolean accept( Map<String, Workflow> workflowMap, String workflowName, Rating rating )
    {
        boolean accept = false;

        while ( true )
        {
            Workflow workflow = workflowMap.get( workflowName );
            workflowName = workflow.apply( rating );
            if ( workflowName.equals( "A" ) )
            {
                accept = true;
                break;
            }
            if ( workflowName.equals( "R" ) )
            {
                break;
            }
        }

        return accept;
    }

}
