package com.github.ttran17.day21;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;
import com.github.ttran17.TestAdventUtils;

public class TestGarden
{
    @Test
    public void testSubmit( )
    {
        TestAdventUtils.testSubmit( 21, ( lines ) -> Garden.walk( lines, 6 ), 16 );
    }

    @Test
    public void submit( )
    {
        AdventUtils.submit( 21, ( lines ) -> Garden.walk( lines, 64 ) );
    }

    @Test
    public void testLayout( )
    {
        File inputFile = AdventUtils.getInputFile( 21 );
        List<String> lines = AdventUtils.readLines( inputFile );

        Garden garden = new Garden( lines, 1 );
        char[][] layout = garden.layout;
        for ( int row = 0; row < garden.nRows; row++ )
        {
            if ( layout[row][garden.walkStartCol] == '#' )
            {
                System.out.println( "Found a rock at row: " + row );
            }
        }
        for ( int col = 0; col < garden.nCols; col++ )
        {
            if ( layout[garden.walkStartRow][col] == '#' )
            {
                System.out.println( "Found a rock at col: " + col );
            }
        }
    }

    @Test
    public void testFiniteGarden( )
    {

        AdventUtils.submit( 21, ( lines ) -> FiniteGarden.walk( lines, 392, 392, 131, 65 ) );
        AdventUtils.submit( 21, ( lines ) -> InfiniteGarden.walk( lines, 392 ) );
    }

    @Test
    public void testFiniteGardenAgain( )
    {
        AdventUtils.submit( 21, ( lines ) -> FiniteGarden.walk( lines, 589, 589, 131, 65 ) );
        AdventUtils.submit( 21, ( lines ) -> InfiniteGarden.walk( lines, 589 ) );
    }

    @Test
    public void submitTwoStar( )
    {
        AdventUtils.submit( 21, ( lines ) -> InfiniteGarden.walk( lines, 26501365 ) );
    }
}
