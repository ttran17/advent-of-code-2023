package com.github.ttran17.day4;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;
import com.github.ttran17.TestAdventUtils;

public class TestLottery
{
    @Test
    public void testScratchCard( )
    {
        String line = "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1";
        ScratchCard scratchCard = Elf.consume( line );

        Assertions.assertEquals( 3, scratchCard.getCardNumber() );
        Assertions.assertEquals( 2, scratchCard.getNumberOfMatchedWinningNumbers() );
        Assertions.assertTrue( scratchCard.getWinningNumbers().contains( 1 ));
        Assertions.assertTrue( scratchCard.getPlayedNumbers().contains( 72 ));
    }

    @Test
    public void testOneStar( )
    {
        TestAdventUtils.testSubmit( 4, ( lines ) -> Elf.consumePartOne( lines ), 13 );
    }

    @Test
    public void submitOneStar( )
    {
        AdventUtils.submit( 4, ( lines ) -> Elf.consumePartOne( lines ) );
    }

    @Test
    public void testTwoStar( )
    {
        TestAdventUtils.testSubmit( 4, ( lines ) -> Elf.consumePartTwo( lines ), 30 );
    }

    @Test
    public void submitTwoStar( )
    {
        AdventUtils.submit( 4, ( lines ) -> Elf.consumePartTwo( lines ) );
    }
}
