package com.github.ttran17.day13;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;
import com.github.ttran17.TestAdventUtils;
import com.github.ttran17.day13.Weyl.Offset;

public class TestWeyl
{
    @Test
    public void checkInputs( )
    {
        List<String> lines = AdventUtils.getLines( 13 );
        Weyl weyl = Weyl.consume( lines );

        int maxRows = -1;
        int maxCols = -1;
        for ( char[][] grid : weyl.grids )
        {
            maxRows = Math.max( maxRows, grid.length );
            maxCols = Math.max( maxCols, grid[0].length );
        }

        System.out.println( String.format( "Max rows: %d, Max cols %d", maxRows, maxCols ) );
    }

    @Test
    public void testOffsets( )
    {
        Assertions.assertEquals( new Offset( 0, 1 ), Weyl.getOffsets( 0, 9 ) );
        Assertions.assertEquals( new Offset( 0, 3 ), Weyl.getOffsets( 1, 9 ) );
        Assertions.assertEquals( new Offset( 0, 5 ), Weyl.getOffsets( 2, 9 ) );
        Assertions.assertEquals( new Offset( 0, 7 ), Weyl.getOffsets( 3, 9 ) );
        Assertions.assertEquals( new Offset( 1, 8 ), Weyl.getOffsets( 4, 9 ) );
        Assertions.assertEquals( new Offset( 3, 8 ), Weyl.getOffsets( 5, 9 ) );
        Assertions.assertEquals( new Offset( 5, 8 ), Weyl.getOffsets( 6, 9 ) );
        Assertions.assertEquals( new Offset( 7, 8 ), Weyl.getOffsets( 7, 9 ) );

        Assertions.assertEquals( new Offset( 0, 1 ), Weyl.getOffsets( 0, 8 ) );
        Assertions.assertEquals( new Offset( 0, 3 ), Weyl.getOffsets( 1, 8 ) );
        Assertions.assertEquals( new Offset( 0, 5 ), Weyl.getOffsets( 2, 8 ) );
        Assertions.assertEquals( new Offset( 0, 7 ), Weyl.getOffsets( 3, 8 ) );
        Assertions.assertEquals( new Offset( 2, 7 ), Weyl.getOffsets( 4, 8 ) );
        Assertions.assertEquals( new Offset( 4, 7 ), Weyl.getOffsets( 5, 8 ) );
        Assertions.assertEquals( new Offset( 6, 7 ), Weyl.getOffsets( 6, 8 ) );
        Assertions.assertEquals( new Offset( 8, 7 ), Weyl.getOffsets( 7, 8 ) );
    }

    @Test
    public void testSubmit( )
    {
        TestAdventUtils.testSubmit( 13, ( lines ) -> Weyl.countReflections( lines ), 405 );
    }

    @Test
    public void testSubmitAgain( )
    {
        TestAdventUtils.testSubmit( 13, 2, ( lines ) -> Weyl.countReflections( lines ), 8 );
    }

    @Test
    public void submit( )
    {
        // Too low
        AdventUtils.submit( 13, ( lines ) -> Weyl.countReflections( lines ) );
    }
}
