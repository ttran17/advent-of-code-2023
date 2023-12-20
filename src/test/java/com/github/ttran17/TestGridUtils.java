package com.github.ttran17;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestGridUtils
{
    protected static class TestGridCell
    {
        protected final int row;
        protected final int col;

        public TestGridCell( int row, int col )
        {
            this.row = row;
            this.col = col;
        }
    }

    protected GridUtils.CellCoordsAccessor<TestGridCell> accessor = new GridUtils.CellCoordsAccessor<>( )
    {
        @Override
        public long getRow( TestGridCell testGridCell )
        {
            return testGridCell.row;
        }

        @Override
        public long getCol( TestGridCell testGridCell )
        {
            return testGridCell.col;
        }
    };

    @Test
    public void testGreensTheorem( )
    {
        TestGridCell[] loop = new TestGridCell[] {
                new TestGridCell( 1, 2 ),
                new TestGridCell( 1, 3 ),
                new TestGridCell( 1, 5 ),
                new TestGridCell( 10, 5 ),
                new TestGridCell( 10, 4 ),
                new TestGridCell( 10, 2 ),
                new TestGridCell( 1, 2 ),
        };

        Assertions.assertEquals( 27, GridUtils.GreensTheorem( loop, accessor ) );
        Assertions.assertEquals( 40, GridUtils.Area( loop, accessor ) );
    }

    @Test
    public void testArea( )
    {
        TestGridCell[] loop = new TestGridCell[] {
                new TestGridCell( 1, 1 ),
                new TestGridCell( 1, 2 ),
                new TestGridCell( 2, 2 ),
                new TestGridCell( 3, 2 ),
                new TestGridCell( 3, 3 ),
                new TestGridCell( 4, 3 ),
                new TestGridCell( 5, 3 ),
                new TestGridCell( 5, 2 ),
                new TestGridCell( 5, 1 ),
                new TestGridCell( 5, 0 ),
                new TestGridCell( 4, 0 ),
                new TestGridCell( 3, 0 ),
                new TestGridCell( 3, 1 ),
                new TestGridCell( 2, 1 ),
                new TestGridCell( 1, 1 ),
        };
        Assertions.assertEquals( 16, GridUtils.Area( loop, accessor ) );

        TestGridCell[] minimalLoop = new TestGridCell[] {
                new TestGridCell( 1, 1 ),
                new TestGridCell( 1, 2 ),
                new TestGridCell( 3, 2 ),
                new TestGridCell( 3, 3 ),
                new TestGridCell( 5, 3 ),
                new TestGridCell( 5, 0 ),
                new TestGridCell( 3, 0 ),
                new TestGridCell( 3, 1 ),
                new TestGridCell( 1, 1 ),
        };
        Assertions.assertEquals( 16, GridUtils.Area( minimalLoop, accessor ) );
    }
}
