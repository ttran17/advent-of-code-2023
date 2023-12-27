package com.github.ttran17.day24;

import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;
import com.github.ttran17.TestAdventUtils;

public class TestDescartes
{

    @Test
    public void testPermissibleCount( )
    {
        TestAdventUtils.testSubmit( 24, ( lines ) -> Descartes.intersect( lines, 7, 27 ), 2 );
    }

    @Test
    public void submit( )
    {
        AdventUtils.submit( 24, ( lines ) -> Descartes.intersect( lines, 200000000000000L, 400000000000000L ) );
    }

    // (dy2 - dy1) * a + (dx1 - dx2) * b + (y1 - y2) * d + (x2 - x1) * e = dx1 * y1 - dx2 * y2 + x2 * dy2 - x1 * dy1
}
