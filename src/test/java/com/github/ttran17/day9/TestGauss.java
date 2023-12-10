package com.github.ttran17.day9;

import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;
import com.github.ttran17.TestAdventUtils;

public class TestGauss
{
    @Test
    public void testSubmit( )
    {
        TestAdventUtils.testSubmit( 9, ( lines ) -> Gauss.forecast( lines ), 114 );
    }

    @Test
    public void submitOneStar( )
    {
        AdventUtils.submit( 9, ( lines ) -> Gauss.forecast( lines ) );
    }

    @Test
    public void submitTwoStar( )
    {
        AdventUtils.submit( 9, ( lines ) -> Gauss.forecastTwoStar( lines ) );
    }
}
