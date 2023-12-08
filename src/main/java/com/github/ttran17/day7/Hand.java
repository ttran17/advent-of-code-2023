package com.github.ttran17.day7;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class Hand
{

    public enum Type
    {
        FIVE_OF_A_KIND,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD;
    }

    protected final Card[] cards;

    protected final Type type;

    protected final int bid;

    private final String hand;

    public Hand( String line, HandTypeResolver handTypeResolver )
    {
        String[] tokens = line.split( " " );
        String hand = tokens[0].trim( );

        Card[] cards = new Card[5];
        for ( int i = 0; i < hand.length( ); i++ )
        {
            cards[i] = Card.parse( hand.charAt( i ) );
        }
        this.cards = cards;

        this.type = handTypeResolver.accept( cards );

        this.bid = Integer.parseInt( tokens[1].trim( ) );

        this.hand = hand;
    }

    @Override
    public String toString( )
    {
        return hand + ": " + type;
    }

    public interface HandTypeResolver
    {
        Type accept( Card[] cards );
    }

    protected static Type resolveHandType( Card[] cards )
    {
        Object2IntMap<Card> map = new Object2IntOpenHashMap<>( );

        for ( Card card : cards )
        {
            int count = map.putIfAbsent( card, 0 );
            map.put( card, count + 1 );
        }

        int[] counts = map.values( ).intStream( ).sorted( ).toArray( );
        int k = counts.length - 1;
        int count = counts[k];
        if ( count == 5 )
        {
            return Type.FIVE_OF_A_KIND;
        }

        if ( count == 4 )
        {
            return Type.FOUR_OF_A_KIND;
        }

        if ( count == 3 )
        {
            if ( k > 0 && counts[k - 1] == 2 )
            {
                return Type.FULL_HOUSE;
            }

            return Type.THREE_OF_A_KIND;
        }

        if ( count == 2 )
        {
            if ( k > 0 && counts[k - 1] == 2 )
            {
                return Type.TWO_PAIR;
            }

            return Type.ONE_PAIR;
        }

        return Type.HIGH_CARD;
    }

    protected static Type resolveHandTypeWithJoker( Card[] cards )
    {
        Object2IntMap<Card> map = new Object2IntOpenHashMap<>( );

        int nJokers = 0;
        for ( Card card : cards )
        {
            if ( card == Card.J )
            {
                nJokers++;
                continue;
            }
            int count = map.putIfAbsent( card, 0 );
            map.put( card, count + 1 );
        }

        // 5 jokers?!
        if ( nJokers == 5 )
        {
            return Type.FIVE_OF_A_KIND;
        }

        int[] counts = map.values( ).intStream( ).sorted( ).toArray( );
        int k = counts.length - 1;
        int count = counts[k];

        if ( count == 5 || count == 4 && nJokers == 1 || count == 3 && nJokers == 2 || count == 2 && nJokers == 3 || nJokers == 4 )
        {
            return Type.FIVE_OF_A_KIND;
        }

        if ( count == 4 || count == 3 && nJokers == 1 || count == 2 && nJokers == 2 || nJokers == 3 )
        {
            return Type.FOUR_OF_A_KIND;
        }

        if ( count == 3 && k > 0 && counts[k - 1] == 2 )
        {
            return Type.FULL_HOUSE;
        }

        if ( count == 2 && k > 0 && counts[k - 1] == 2 && nJokers == 1 )
        {
            return Type.FULL_HOUSE;
        }

        if ( count == 3 || count == 2 && nJokers == 1 || nJokers == 2 )
        {
            return Type.THREE_OF_A_KIND;
        }

        if ( count == 2 )
        {
            if ( k > 0 && counts[k - 1] == 2 )
            {
                return Type.TWO_PAIR;
            }

            return Type.ONE_PAIR;
        }

        if ( nJokers == 1 )
        {
            return Type.ONE_PAIR;
        }

        return Type.HIGH_CARD;
    }
}
