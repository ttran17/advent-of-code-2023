package com.github.ttran17.day17;

import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;
import com.github.ttran17.TestAdventUtils;

public class TestWalker
{
    @Test
    public void testSubmit( )
    {
        TestAdventUtils.testSubmit( 17, ( lines ) -> Walker.walk( lines, 1, 3 ), 102 );
    }

    @Test
    public void submit( )
    {
        AdventUtils.submit( 17, ( lines ) -> Walker.walk( lines, 1, 3 ) );
    }

    @Test
    public void testSubmitTwoStar( )
    {
        TestAdventUtils.testSubmit( 17, ( lines ) -> Walker.walk( lines, 4, 10 ), 94 );
    }

    @Test
    public void submitTwoStar( )
    {
        AdventUtils.submit( 17, ( lines ) -> Walker.walk( lines, 4, 10 ) );
    }
}
