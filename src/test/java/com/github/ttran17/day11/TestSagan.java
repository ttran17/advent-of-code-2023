package com.github.ttran17.day11;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;
import com.github.ttran17.TestAdventUtils;

public class TestSagan
{
    @Test
    public void testBuild( )
    {
        File testInputFile = TestAdventUtils.getTestInputFile( 11 );
        List<String> lines = AdventUtils.readLines( testInputFile );

        Universe universe = Universe.build( lines );
        Assertions.assertEquals( 10, universe.rows.size( ) );
        Assertions.assertEquals( 10, universe.cols.size( ) );

        universe.expand( );
        Assertions.assertEquals( 12, universe.rows.size( ) );
        Assertions.assertEquals( 13, universe.cols.size( ) );
    }

    @Test
    public void testSubmit( )
    {
        TestAdventUtils.testSubmit( 11, ( lines ) -> Sagan.computePairWiseDistances( lines ), 374 );
        TestAdventUtils.testSubmit( 11, ( lines ) -> Sagan.computePairWiseDistancesOlderUniverse( lines, 10 ), 1030 );
        TestAdventUtils.testSubmit( 11, ( lines ) -> Sagan.computePairWiseDistancesOlderUniverse( lines, 100 ), 8410 );
    }

    @Test
    public void submitOneStar( )
    {
        AdventUtils.submit( 11, ( lines ) -> Sagan.computePairWiseDistances( lines ) );
    }

    @Test
    public void submitTwoStar( )
    {
        AdventUtils.submit( 11, ( lines ) -> Sagan.computePairWiseDistancesOlderUniverse( lines, 1_000_000 ) );
    }
}
