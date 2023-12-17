package com.github.ttran17.day15;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CornedBeef
{
    protected interface HashOp
    {
        long apply( long value, char c );
    }

    protected static List<HashOp> ops = List.of(
            ( value, c ) -> value + ( int ) c,
            ( value, c ) -> value * 17,
            ( value, c ) -> value % 256
    );

    protected static long hash( String line )
    {
        long value = 0;
        for ( int i = 0; i < line.length( ); i++ )
        {
            for ( int k = 0; k < ops.size( ); k++ )
            {
                value = ops.get( k ).apply( value, line.charAt( i ) );
            }
        }
        return value;
    }

    protected static long prepare( List<String> lines )
    {
        String[] tokens = lines.get( 0 ).trim( ).split( "," );

        long sum = 0;
        for ( int j = 0; j < tokens.length; j++ )
        {
            String token = tokens[j].trim( );
            long value = hash( token );
            sum += value;
        }

        return sum;
    }

    protected static long prepareLinkedHash( List<String> lines )
    {
        Map<Long, Map<String, Long>> boxes = new HashMap<>( );

        String[] operations = lines.get( 0 ).trim( ).split( "," );

        for ( String operation : operations )
        {
            int opIndex = operation.indexOf( "-" );
            if ( opIndex > -1 )
            {
                String[] tokens = operation.split( "-" );

                String label = tokens[0];
                long boxIndex = hash( label );
                boxes.getOrDefault( boxIndex, new HashMap<>( ) ).remove( label );
            }
            else
            {
                String[] tokens = operation.split( "=" );

                String label = tokens[0];
                long boxIndex = hash( label );
                long focalLength = Long.parseLong( tokens[1] );

                boxes.computeIfAbsent( boxIndex, key -> new LinkedHashMap<>( ) ).put( label, focalLength );
            }
        }

        long sum = 0;
        for ( Map.Entry<Long, Map<String, Long>> outer : boxes.entrySet( ) )
        {
            long box = 1 + outer.getKey( );
            Map<String, Long> inner = outer.getValue( );

            int slot = 1;
            for ( long focalLength : inner.values( ) )
            {
                sum += box * ( slot++ ) * focalLength;
            }
        }
        return sum;
    }
}
