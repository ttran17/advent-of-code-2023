package com.github.ttran17.day4;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.github.ttran17.ElfUtils;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;

public class Elf
{

    protected static ScratchCard consume( String line )
    {
        int colonIndex = ElfUtils.getColonIndex( line );
        int pipeIndex = ElfUtils.getPipeIndex( line );

        int cardNumber = Integer.parseInt( line.substring( 4, colonIndex ).trim( ) );
        IntSet winningNumbers = buildNumberSet( line.substring( colonIndex + 1, pipeIndex ), new IntLinkedOpenHashSet( ) );
        IntList playedNumbers = buildNumberSet( line.substring( pipeIndex + 1 ), new IntArrayList( ) );
        return new ScratchCard( cardNumber, IntSets.unmodifiable( winningNumbers ), IntLists.unmodifiable( playedNumbers ) );
    }

    protected static int consumePartOne( List<String> lines )
    {
        int sum = 0;
        for ( String line : lines )
        {
            ScratchCard scratchCard = consume( line );
            int count = scratchCard.getNumberOfMatchedWinningNumbers( );
            sum += count == 0 ? 0 : ( int ) Math.pow( 2, count - 1 );
        }
        return sum;
    }

    protected static <T extends IntCollection> T buildNumberSet( String substring, T collection )
    {
        String[] tokens = substring.trim( ).split( "\s+" );
        for ( String token : tokens )
        {
            collection.add( Integer.parseInt( token ) );
        }

        return collection;
    }

    protected static int consumePartTwo( List<String> lines )
    {
        List<ScratchCard> originalCards = new LinkedList<>( );
        originalCards.add( new ScratchCard( 0, null, null ) ); // card numbers are indexed 1-based
        for ( String line : lines )
        {
            ScratchCard scratchCard = consume( line );
            originalCards.add( scratchCard );
        }

        Map<ScratchCard, List<ScratchCard>> cardInstances = new LinkedHashMap<>( );
        for ( int i = 1; i < originalCards.size( ); i++ )
        {
            Queue<ScratchCard> queue = new LinkedList<>( );
            queue.offer( originalCards.get( i ) );
            while ( !queue.isEmpty( ) )
            {
                ScratchCard scratchCard = queue.poll( );
                cardInstances.computeIfAbsent( scratchCard, s -> new ArrayList<>( ) ).add( scratchCard );
                int count = scratchCard.getNumberOfMatchedWinningNumbers( );
                int offset = scratchCard.getCardNumber( );
                for ( int k = 1; k <= count; k++ )
                {
                    queue.offer( originalCards.get( offset + k ) );
                }
            }
        }

        int sum = 0;
        for ( List<ScratchCard> instances : cardInstances.values( ) )
        {
            sum += instances.size( );
        }
        return sum;
    }
}
