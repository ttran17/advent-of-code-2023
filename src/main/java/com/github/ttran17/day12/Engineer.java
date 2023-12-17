package com.github.ttran17.day12;

import static com.github.ttran17.day12.SpringRow.SpringState;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Engineer
{

    protected static long reconstruct( List<String> lines )
    {
        List<SpringRow> springRows = SpringRow.parse( lines );

        long sum = 0;
        for ( SpringRow springRow : springRows )
        {
            sum += reconstruct( springRow );
        }

        return sum;
    }

    protected static long reconstructUnfolded( List<String> lines )
    {
        List<Long> folded = new ArrayList<>( );
        List<SpringRow> springRows = SpringRow.parse( lines );
        for ( SpringRow springRow : springRows )
        {
            folded.add( reconstruct( springRow ) );
        }

        List<Long> unfoldedOnce = new ArrayList<>( );
        List<SpringRow> unfoldedOnceSpringRows = SpringRow.unfold( lines, 1 );
        for ( SpringRow springRow : unfoldedOnceSpringRows )
        {
            unfoldedOnce.add( reconstruct( springRow ) );
        }

        List<Long> unfoldedTwice = new ArrayList<>( );
        List<SpringRow> unfoldedTwiceSpringRows = SpringRow.unfold( lines, 2 );
        for ( SpringRow springRow : unfoldedTwiceSpringRows )
        {
            unfoldedTwice.add( reconstruct( springRow ) );
        }

        long sum = 0;
        Iterator<Long> foldedIterator = folded.iterator( );
        Iterator<Long> unfoldedOnceIterator = unfoldedOnce.iterator( );
        Iterator<Long> unfoldedTwiceIterator = unfoldedTwice.iterator( );
        Iterator<SpringRow> rowIterator = springRows.iterator( );
        while ( foldedIterator.hasNext( ) )
        {
            long unfolded1 = unfoldedOnceIterator.next( );
            long base = foldedIterator.next( );
            long factor = unfolded1 / base;

            sum += ( base * factor * factor * factor * factor );

            long unfolded2 = unfoldedTwiceIterator.next( );
            SpringRow springRow = rowIterator.next( );
            if ( unfolded2 != base * factor * factor )
            {
                System.out.println( StringUtils.join( springRow.states, "" ) + ": " + base * factor * factor + " vs " + unfolded2 );
            }
        }

        return sum;
    }

    protected static long reconstruct( SpringRow springRow )
    {
        List<List<Range>> startLists = new ArrayList<>( );

        List<List<SpringState>> minimalGroups = getMinimalGroups( springRow );

        int start = 0;
        for ( int d = 0; d < minimalGroups.size( ); d++ )
        {
            List<SpringState> minimalGroup = minimalGroups.get( d );
            int maxStart = getMaxStart( springRow, minimalGroup, d );

            List<SpringState> exactGroup = minimalGroup.stream( ).filter( s -> s == SpringState.damaged ).collect( Collectors.toList( ) );

            List<Range> startList = new ArrayList<>( );
            while ( start <= maxStart )
            {
                boolean match = true;
                for ( int offset = 0; offset < minimalGroup.size( ); offset++ )
                {
                    match = springRow.states[start + offset] == SpringState.unknown || springRow.states[start + offset] == minimalGroup.get( offset );
                    if ( !match )
                    {
                        break;
                    }
                }
                if ( match )
                {
                    int offset = minimalGroup.get( 0 ) == SpringState.operational ? 1 : 0;
                    startList.add( new Range( exactGroup.size( ), start + offset, start + offset + exactGroup.size( ) - 1 ) );
                }
                start++;
            }

            if ( startList.isEmpty( ) )
            {
                throw new IllegalStateException( "No match found!" );
            }

            start = startList.get( 0 ).start + exactGroup.size( );

            startLists.add( startList );
        }

        List<List<Arrangement>> potentialArrangements = getPotentialArrangements( startLists );

        return countValidArrangements( springRow, potentialArrangements );
    }

    protected static List<List<SpringState>> getMinimalGroups( SpringRow springRow )
    {
        List<List<SpringState>> minimalGroups = new ArrayList<>( );

        for ( int d = 0; d < springRow.damageCount.length; d++ )
        {
            List<SpringState> minimalGroup = new ArrayList<>( );

            if ( d > 0 || ( d == 0 && springRow.states[0] == SpringState.operational ) )
            {
                minimalGroup.add( SpringState.operational );
            }

            for ( int k = 0; k < springRow.damageCount[d]; k++ )
            {
                minimalGroup.add( SpringState.damaged );
            }

            if ( d < springRow.damageCount.length - 1 || springRow.states[springRow.states.length - 1] == SpringState.operational )
            {
                minimalGroup.add( SpringState.operational );
            }

            minimalGroups.add( minimalGroup );
        }

        return minimalGroups;
    }

    protected static int getMaxStart( SpringRow springRow, List<SpringState> minimalGroup, int currentDamageGroupIndex )
    {
        int count = 0;
        for ( int d = currentDamageGroupIndex + 1; d < springRow.damageCount.length; d++ )
        {
            count += ( springRow.damageCount[d] + 1 );
        }

        for ( int i = springRow.states.length - 1; i > -1; i-- )
        {
            if ( springRow.states[i] == SpringState.operational )
            {
                count++;
            }
            else
            {
                break;
            }
        }

        int offset = minimalGroup.get( minimalGroup.size( ) - 1 ) == SpringState.operational ? 1 : 0;

        return springRow.states.length - count - ( minimalGroup.size( ) - offset );
    }

    protected static List<List<Arrangement>> getPotentialArrangements( List<List<Range>> startLists )
    {
        List<List<Arrangement>> potentialArrangements = new ArrayList<>( );

        for ( int id = 0; id < startLists.size( ); id++ )
        {
            List<Arrangement> current = new ArrayList<>( );
            for ( Range range : startLists.get( id ) )
            {
                current.add( new Arrangement( id, range.start, range.end ) );
            }

            if ( id == 0 )
            {
                for ( Arrangement a : current )
                {
                    potentialArrangements.add( List.of( a ) );
                }
                continue;
            }

            List<List<Arrangement>> previousValidArrangements = potentialArrangements;
            potentialArrangements = new ArrayList<>( );

            for ( List<Arrangement> previous : previousValidArrangements )
            {
                Arrangement a = previous.get( previous.size( ) - 1 );
                for ( Arrangement b : current )
                {
                    if ( a.end + 1 < b.start )
                    {
                        List<Arrangement> merged = new ArrayList<>( previous );
                        merged.add( b );
                        potentialArrangements.add( merged );
                    }
                }
            }
        }

        return potentialArrangements;
    }

    protected static long countValidArrangements( SpringRow springRow, List<List<Arrangement>> potentialArrangements )
    {
        Iterator<List<Arrangement>> iterator = potentialArrangements.iterator( );
        while ( iterator.hasNext( ) )
        {
            List<Arrangement> arrangement = iterator.next( );

            SpringState[] guess = new SpringState[springRow.states.length];
            for ( int i = 0; i < springRow.states.length; i++ )
            {
                guess[i] = SpringState.operational;
            }

            for ( Arrangement a : arrangement )
            {
                for ( int i = a.start; i <= a.end; i++ )
                {
                    guess[i] = SpringState.damaged;
                }
            }

            boolean valid = true;
            for ( int i = 0; i < springRow.states.length; i++ )
            {
                valid = springRow.states[i] == SpringState.unknown || springRow.states[i] == SpringState.operational && guess[i] == SpringState.operational || springRow.states[i] == SpringState.damaged && guess[i] == SpringState.damaged;
                if ( !valid )
                {
                    break;
                }
            }
            if ( !valid )
            {
                iterator.remove( );
            }
        }

        return potentialArrangements.size( );
    }

    public record Range( int span, int start, int end ) { }

    public record Arrangement( int id, int start, int end ) { }
}
