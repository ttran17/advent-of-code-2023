package com.github.ttran17.day7;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Gambler
{

    /**
     * Returns the hand in ranked order from highest to lowest. So the hand at index 0 is the highest.
     */
    protected static List<Hand> rankHands( LinkedList<Hand> hands, TieBreaker tieBreaker )
    {
        List<Hand> sortedHands = new ArrayList<>( );

        // First just sort by type from highest to lowest
        hands.sort( Comparator.comparingInt( o -> o.type.ordinal( ) ) );

        // Now tie-breakers
        while ( !hands.isEmpty( ) )
        {
            Hand hand = hands.poll( );
            Hand nextHand = hands.peek( );

            if ( nextHand == null || hand.type.ordinal( ) < nextHand.type.ordinal( ) )
            {
                sortedHands.add( hand );
                continue;
            }

            List<Hand> ties = new ArrayList<>( );
            ties.add( hand );
            while ( nextHand != null && hand.type.ordinal( ) == nextHand.type.ordinal( ) )
            {
                ties.add( hands.poll( ) );
                nextHand = hands.peek( );
            }
            tieBreaker.accept( ties );
            sortedHands.addAll( ties );
        }

        return sortedHands;
    }

    public interface TieBreaker
    {
        void accept( List<Hand> ties );
    }

    protected static void tieBreaker( List<Hand> ties )
    {
        ties.sort( ( o1, o2 ) -> {
            for ( int i = 0; i < o1.cards.length; i++ )
            {
                if ( o1.cards[i].ordinal( ) < o2.cards[i].ordinal( ) )
                {
                    return -1;
                }
                if ( o1.cards[i].ordinal( ) > o2.cards[i].ordinal( ) )
                {
                    return 1;
                }
            }
            return 0;
        } );
    }

    protected static void tieBreakerWithJoker( List<Hand> ties )
    {
        ties.sort( ( o1, o2 ) -> {
            for ( int i = 0; i < o1.cards.length; i++ )
            {
                // Special handling of joker card
                if ( o1.cards[i] == Card.J && o2.cards[i] == Card.J )
                {
                    continue;
                }

                if ( o1.cards[i] == Card.J )
                {
                    return 1;
                }
                if ( o2.cards[i] == Card.J )
                {
                    return -1;
                }

                // Otherwise proceed as normal
                if ( o1.cards[i].ordinal( ) < o2.cards[i].ordinal( ) )
                {
                    return -1;
                }
                if ( o1.cards[i].ordinal( ) > o2.cards[i].ordinal( ) )
                {
                    return 1;
                }
            }
            return 0;
        } );
    }

    protected static LinkedList<Hand> buildHands( List<String> lines, Hand.HandTypeResolver handTypeResolver )
    {
        LinkedList<Hand> hands = new LinkedList<>( );
        for ( String line : lines )
        {
            hands.offer( new Hand( line, handTypeResolver ) );
        }
        return hands;
    }

    protected static long score( List<String> lines )
    {
        return score( lines, cards -> Hand.resolveHandType( cards ), ( ties ) -> tieBreaker( ties ) );
    }

    protected static long scoreWithJoker( List<String> lines )
    {
        return score( lines, cards -> Hand.resolveHandTypeWithJoker( cards ), ( ties ) -> tieBreakerWithJoker( ties ) );
    }

    protected static long score( List<String> lines, Hand.HandTypeResolver handTypeResolver, TieBreaker tieBreaker )
    {
        LinkedList<Hand> hands = buildHands( lines, handTypeResolver );

        List<Hand> rankedHands = rankHands( hands, tieBreaker );
        Collections.reverse( rankedHands );

        int i = 1;
        long sum = 0;
        for ( Hand hand : rankedHands )
        {
            sum += ( i++ ) * ( long ) hand.bid;
        }
        return sum;
    }
}
