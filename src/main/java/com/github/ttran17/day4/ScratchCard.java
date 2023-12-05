package com.github.ttran17.day4;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

public class ScratchCard
{
    private final int cardNumber;

    private final IntSet winningNumbers;

    private final IntList playedNumbers;

    public ScratchCard( int cardNumber, IntSet winningNumbers, IntList playedNumbers )
    {
        this.cardNumber = cardNumber;
        this.winningNumbers = winningNumbers;
        this.playedNumbers = playedNumbers;
    }

    public int getCardNumber( )
    {
        return cardNumber;
    }

    public IntSet getWinningNumbers( )
    {
        return winningNumbers;
    }

    public IntList getPlayedNumbers( )
    {
        return playedNumbers;
    }

    public int getNumberOfMatchedWinningNumbers( )
    {
        int count = 0;

        for ( int playedNumber : playedNumbers )
        {
            if ( winningNumbers.contains( playedNumber ) )
            {
                count++;
            }
        }

        return count;
    }
}
