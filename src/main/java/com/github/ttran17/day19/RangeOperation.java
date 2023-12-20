package com.github.ttran17.day19;

import com.github.ttran17.ElfUtils;
import com.google.common.collect.Range;

import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;

public class RangeOperation
{
    protected static final char[] XMAS = new char[] { 'x', 'm', 'a', 's' };

    protected final char c;
    protected final char[] uninvolvedChars = new char[3];
    ;
    protected final char operation;

    protected final Range<Integer> passRangeBound;
    protected final Range<Integer> failRangeBound;

    protected final String workflowName;

    protected RangeOperation( String line )
    {
        c = line.charAt( 0 );
        int ci = 0;
        for ( char other : XMAS )
        {
            if ( c != other )
            {
                uninvolvedChars[ci++] = other;
            }
        }

        int opIndex = line.indexOf( '<' );
        if ( opIndex < 0 )
        {
            opIndex = line.indexOf( '>' );
        }
        operation = line.charAt( opIndex );

        int colonIndex = ElfUtils.getColonIndex( line );
        int bound = Integer.parseInt( line.substring( opIndex + 1, colonIndex ) );

        passRangeBound = operation == '<' ? Range.closedOpen( 1, bound ) : Range.openClosed( bound, 4000 );
        failRangeBound = operation == '<' ? Range.closed( bound, 4000 ) : Range.closed( 1, bound );

        workflowName = line.substring( colonIndex + 1 );
    }

    protected RangeOperationResult apply( RangeRating rating )
    {
        Char2ObjectMap<Range<Integer>> passed = new Char2ObjectArrayMap<>( );
        Char2ObjectMap<Range<Integer>> failed = new Char2ObjectArrayMap<>( );

        for ( char o : uninvolvedChars )
        {
            if ( rating.containsKey( o ) )
            {
                passed.put( o, rating.get( o ) );
                failed.put( o, rating.get( o ) );
            }
        }
        if ( rating.containsKey( c ) )
        {
            if ( passRangeBound.isConnected( rating.get( c ) ) )
            {
                Range<Integer> intersection = passRangeBound.intersection( rating.get( c ) );
                if ( !intersection.isEmpty( ) )
                {
                    passed.put( c, intersection );
                }
            }

            if ( failRangeBound.isConnected( rating.get( c ) ) )
            {
                Range<Integer> intersection = failRangeBound.intersection( rating.get( c ) );
                if ( !intersection.isEmpty( ) )
                {
                    failed.put( c, intersection );
                }
            }
        }

        return new RangeOperationResult( new RangeRating( passed ), new RangeRating( failed ) );
    }

}
