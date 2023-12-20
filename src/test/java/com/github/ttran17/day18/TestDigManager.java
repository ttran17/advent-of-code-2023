package com.github.ttran17.day18;

import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;
import com.github.ttran17.TestAdventUtils;

public class TestDigManager
{
    @Test
    public void testSubmit( )
    {
        TestAdventUtils.testSubmit( 18, ( lines ) -> DigManager.excavate( lines ), 62 );
    }

    @Test
    public void submitOneStar( )
    {
        AdventUtils.submit( 18, ( lines ) -> DigManager.excavate( lines ) );
    }

    @Test
    public void testSubmitTwoStar( )
    {
        TestAdventUtils.testSubmit( 18, ( lines ) -> DigManager.excavateActual( lines ), 952408144115L );
    }

    @Test
    public void submitTwoStar( )
    {
        AdventUtils.submit( 18, ( lines ) -> DigManager.excavateActual( lines ) );
    }
}
