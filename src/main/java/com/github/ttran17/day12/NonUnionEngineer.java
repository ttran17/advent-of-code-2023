package com.github.ttran17.day12;

import static com.github.ttran17.day12.SpringRow.SpringState;
import static com.github.ttran17.day12.SpringRow.SpringState.damaged;
import static com.github.ttran17.day12.SpringRow.SpringState.operational;
import static com.github.ttran17.day12.SpringRow.SpringState.unknown;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class NonUnionEngineer
{

    public enum DamageLaydown
    {
        NOT_STARTED,
        ACTIVE
    }

    protected static long reconstruct( List<String> lines, int unfoldCount )
    {
        long sum = 0;
        for ( String line : lines )
        {
            sum += reconstruct( line, unfoldCount );
        }
        return sum;
    }

    protected static long reconstruct( String line, int unfoldCount )
    {
        SpringRow springRow = SpringRow.unfold( line, unfoldCount );

        long total = 0;

        Map<Pointer, Pointer> currentCache = new LinkedHashMap<>( );
        for ( int k = unfoldCount; k > -1; k-- )
        {

            total = reconstruct( currentCache, springRow.states, springRow.rootIndex[k], springRow.damageCount, springRow.damageGroupIndex[k] );
        }

        return total;
    }

    protected static long reconstruct( Map<Pointer, Pointer> cache, SpringState[] states, int rootIndex, int[] damageCount, int damageGroupIndex )
    {
        long total = 0;

        Pointer rootPoint = new Pointer( rootIndex, DamageLaydown.NOT_STARTED, damageGroupIndex, -1, unknown );

        long cacheHits = 0;

        Queue<Pointer> queue = new ArrayDeque<>( );
        queue.add( rootPoint );
        while ( !queue.isEmpty( ) )
        {
            Pointer previous = queue.poll( );
            if ( previous.finished )
            {
                cacheHits++;
                while ( previous.parent != null )
                {
                    previous.parent.total += previous.total;
                    previous = previous.parent;
                }
                continue;
            }

            int currentCharIndex = previous.charIndex + 1;

            if ( currentCharIndex == states.length )
            {
                if ( previous.damageGroupIndex == damageCount.length || ( previous.damageGroupIndex == damageCount.length - 1 && previous.damageWithinGroupIndex + 1 == damageCount[previous.damageGroupIndex] ) )
                {
                    // found a valid arrangement
                    previous.total = 1;
                    //                    previous.parent.descendants.add( previous );
//                    previous.finished = true;

                    previous = previous.parent;
                    while ( previous != null )
                    {
                        previous.total++;
//                        previous.finished = true;
                        //                        previous.parent.descendants.add( previous );
                        //                        previous.parent.descendants.addAll( previous.descendants );
                        previous = previous.parent;
                    }
                    total++;
                }
                else
                {
                    previous.bad = true;
                }
                continue;
            }

            if ( currentCharIndex < states.length )
            {
                SpringState currentState = states[currentCharIndex];

                if ( currentState == operational )
                {
                    if ( previous.damageLaydown == DamageLaydown.ACTIVE )
                    {
                        if ( previous.damageWithinGroupIndex + 1 == damageCount[previous.damageGroupIndex] )
                        {
                            Pointer current = new Pointer( currentCharIndex, DamageLaydown.NOT_STARTED, previous.damageGroupIndex + 1, -1, operational );
                            current.parent = previous;
                            if ( !cache.containsKey( current ) )
                            {
                                cache.put( current, current );
                                previous.next.add( current );
                                queue.offer( current );
                                current.parent = previous;
                            }
                            else
                            {
                                current.finished = true;
                                queue.offer( cache.get( current ) );
                            }
                            continue;
                        }
                        // bad branch;
                        previous.bad = true;
                        continue;
                    }

                    Pointer current = new Pointer( currentCharIndex, DamageLaydown.NOT_STARTED, previous.damageGroupIndex, previous.damageWithinGroupIndex, operational );
                    current.parent = previous;
                    if ( !cache.containsKey( current ) )
                    {
                        cache.put( current, current );
                        previous.next.add( current );
                        queue.offer( current );
                        current.parent = previous;
                    }
                    else
                    {
                        current.finished = true;
                        queue.offer( cache.get( current ) );
                    }
                    continue;
                }

                if ( currentState == unknown )
                {
                    if ( previous.damageLaydown == DamageLaydown.NOT_STARTED )
                    {
                        if ( previous.damageGroupIndex < damageCount.length )
                        {
                            {
                                Pointer current = new Pointer( currentCharIndex, DamageLaydown.ACTIVE, previous.damageGroupIndex, previous.damageWithinGroupIndex + 1, damaged );
                                current.parent = previous;
                                if ( !cache.containsKey( current ) )
                                {
                                    cache.put( current, current );
                                    previous.next.add( current );
                                    queue.offer( current );
                                    current.parent = previous;
                                }
                                else
                                {
                                    current.finished = true;
                                    queue.offer( cache.get( current ) );
                                }
                            }

                            {
                                Pointer current = new Pointer( currentCharIndex, DamageLaydown.NOT_STARTED, previous.damageGroupIndex, previous.damageWithinGroupIndex, operational );
                                current.parent = previous;
                                if ( !cache.containsKey( current ) )
                                {
                                    cache.put( current, current );
                                    previous.next.add( current );
                                    queue.offer( current );
                                    current.parent = previous;
                                }
                                else
                                {
                                    current.finished = true;
                                    queue.offer( cache.get( current ) );
                                }
                            }
                            continue;
                        }

                        Pointer current = new Pointer( currentCharIndex, DamageLaydown.NOT_STARTED, previous.damageGroupIndex, previous.damageWithinGroupIndex, operational );
                        current.parent = previous;
                        if ( !cache.containsKey( current ) )
                        {
                            cache.put( current, current );
                            previous.next.add( current );
                            queue.offer( current );
                            current.parent = previous;
                        }
                        else
                        {
                            current.finished = true;
                            queue.offer( cache.get( current ) );
                        }
                        continue;
                    }

                    if ( previous.damageLaydown == DamageLaydown.ACTIVE )
                    {
                        if ( previous.damageWithinGroupIndex + 1 == damageCount[previous.damageGroupIndex] )
                        {
                            Pointer current = new Pointer( currentCharIndex, DamageLaydown.NOT_STARTED, previous.damageGroupIndex + 1, -1, operational );
                            current.parent = previous;
                            if ( !cache.containsKey( current ) )
                            {
                                cache.put( current, current );
                                previous.next.add( current );
                                queue.offer( current );
                                current.parent = previous;
                            }
                            else
                            {
                                current.finished = true;
                                queue.offer( cache.get( current ) );
                            }
                            continue;
                        }

                        Pointer current = new Pointer( currentCharIndex, DamageLaydown.ACTIVE, previous.damageGroupIndex, previous.damageWithinGroupIndex + 1, damaged );
                        current.parent = previous;
                        if ( !cache.containsKey( current ) )
                        {
                            cache.put( current, current );
                            previous.next.add( current );
                            queue.offer( current );
                            current.parent = previous;
                        }
                        else
                        {
                            current.finished = true;
                            queue.offer( cache.get( current ) );
                        }
                        continue;
                    }
                }

                if ( currentState == damaged )
                {
                    if ( previous.damageLaydown == DamageLaydown.ACTIVE )
                    {
                        if ( previous.damageWithinGroupIndex + 1 == damageCount[previous.damageGroupIndex] )
                        {
                            // bad
                            previous.bad = true;
                            continue;
                        }

                        Pointer current = new Pointer( currentCharIndex, DamageLaydown.ACTIVE, previous.damageGroupIndex, previous.damageWithinGroupIndex + 1, damaged );
                        current.parent = previous;
                        if ( !cache.containsKey( current ) )
                        {
                            cache.put( current, current );
                            previous.next.add( current );
                            queue.offer( current );
                            current.parent = previous;
                        }
                        else
                        {
                            current.finished = true;
                            queue.offer( cache.get( current ) );
                        }
                        continue;
                    }

                    if ( previous.damageGroupIndex < damageCount.length )
                    {
                        Pointer current = new Pointer( currentCharIndex, DamageLaydown.ACTIVE, previous.damageGroupIndex, previous.damageWithinGroupIndex + 1, damaged );
                        current.parent = previous;
                        if ( !cache.containsKey( current ) )
                        {
                            cache.put( current, current );
                            previous.next.add( current );
                            queue.offer( current );
                            current.parent = previous;
                        }
                        else
                        {
                            current.finished = true;
                            queue.offer( cache.get( current ) );
                        }
                        continue;
                    }

                    // bad branch if we get to here
                    previous.bad = true;
                }
            }
        }

        //         List<List<SpringState>> validArrangements = walk( rootPoint, states.length - ( rootIndex + 1), Arrays.stream( damageCount ).sum( ) );
        //         return validArrangements.size( );

        System.out.println("cache hits: " + cacheHits);
        long rootTotal = rootPoint.total;
        return total;
    }

    protected static void updateCacheAndQueue( Map<Pointer,Map<Pointer,Pointer>> cache, Pointer previous, Pointer current, Queue<Pointer> queue )
    {

        current.parent = previous;
        Pointer cachedPointer = cache.computeIfAbsent( previous, key -> new HashMap<>(  ) ).putIfAbsent( current, current );

        if ( cachedPointer == null )
        {
            queue.offer( current );
            return;
        }

        cachedPointer.finished = true;
        queue.offer( cachedPointer );
    }

    protected static List<List<SpringState>> walk( Pointer rootPointer, int requiredLength, int requiredDamage )
    {
        List<Pointer> leafNodes = new ArrayList<>( );

        ArrayDeque<Pointer> stack = new ArrayDeque<>( );
        stack.push( rootPointer );
        while ( !stack.isEmpty( ) )
        {
            Pointer current = stack.pop( );
            if ( current.next.isEmpty( ) )
            {
                leafNodes.add( current );
                continue;
            }
            for ( Pointer next : current.next )
            {
                next.parent = current;
                stack.push( next );
            }
        }

        List<List<SpringState>> validArrangements = new ArrayList<>( );
        for ( Pointer leaf : leafNodes )
        {
            List<SpringState> valid = new ArrayList<>( );
            valid.add( leaf.springState );
            while ( leaf.parent != rootPointer )
            {
                leaf = leaf.parent;
                valid.add( leaf.springState );
            }
            Collections.reverse( valid );
            if ( valid.size( ) == requiredLength && countDamage( valid ) == requiredDamage )
            {
                validArrangements.add( valid );
            }
        }

        return validArrangements;
    }

    protected static int countDamage( List<SpringState> states )
    {
        int sum = 0;
        for ( SpringState state : states )
        {
            if ( state == damaged )
            {
                sum++;
            }
        }
        return sum;
    }

    protected static class Pointer
    {
        private final int charIndex;

        private final DamageLaydown damageLaydown;

        private final int damageGroupIndex;
        private final int damageWithinGroupIndex;

        private final SpringRow.SpringState springState;

        private final List<Pointer> next = new ArrayList<>( );

        private final Set<Pointer> descendants = new HashSet<>( );

        private Pointer parent;

        private boolean bad;

        private boolean finished;

        private long total;

        public Pointer( int charIndex, DamageLaydown damageLaydown, int damageGroupIndex, int damageWithinGroupIndex, SpringRow.SpringState springState )
        {
            this.charIndex = charIndex;
            this.damageLaydown = damageLaydown;
            this.damageGroupIndex = damageGroupIndex;
            this.damageWithinGroupIndex = damageWithinGroupIndex;
            this.springState = springState;
        }

        private boolean equalState( Pointer a, Pointer b )
        {
            return a.charIndex == b.charIndex &&
                    a.damageLaydown == b.damageLaydown &&
                    a.damageGroupIndex == b.damageGroupIndex &&
                    a.damageWithinGroupIndex == b.damageWithinGroupIndex &&
                    a.springState == b.springState;
        }

        @Override
        public boolean equals( Object o )
        {
            if ( o == null )
            {
                return false;
            }

            if ( !this.getClass( ).equals( o.getClass( ) ) )
            {
                return false;
            }

            Pointer other = ( Pointer ) o;

            return equalState( this, other );
        }

        @Override
        public int hashCode( )
        {
            int prime = 31;
            int result = 1;

            result = prime * result + charIndex;
            result = prime * result + damageLaydown.ordinal( );
            result = prime * result + damageGroupIndex;
            result = prime * result + damageWithinGroupIndex;
            return prime * result + ( springState == null ? 10 : springState.ordinal( ) );
        }

        @Override
        public String toString( )
        {
            return charIndex + " " + springState.toString( ) + " : " + bad + " : " + total;
        }
    }
}
