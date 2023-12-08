package com.github.ttran17.day8;

import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;
import com.github.ttran17.TestAdventUtils;

public class TestNetwork
{
    @Test
    public void testSubmit( )
    {
        TestAdventUtils.testSubmit( 8, ( lines ) -> Network.traverse( lines ), 2 );
        TestAdventUtils.testSubmit( 8, 2, ( lines ) -> Network.traverse( lines ), 6 );
        TestAdventUtils.testSubmit( 8, 3, ( lines ) -> Network.traverseAsGhost( lines ), 6 );
    }

    @Test
    public void submitOneStar( )
    {
        AdventUtils.submit( 8, ( lines ) -> Network.traverse( lines ) );
    }

    @Test
    public void submitTwoStar( )
    {
        AdventUtils.submit( 8, ( lines ) -> Network.traverseAsGhost( lines ) );
    }
}
