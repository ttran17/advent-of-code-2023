package com.github.ttran17.day14;

import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;
import com.github.ttran17.TestAdventUtils;

public class TestPlatform
{
    @Test
    public void testSubmit()
    {
        TestAdventUtils.testSubmit( 14, (lines) -> Platform.computeLoad( lines ), 136 );
    }

    @Test
    public void submitOneStar()
    {
        AdventUtils.submit( 14, ( lines) -> Platform.computeLoad( lines ) );
    }

    @Test
    public void testSubmitWithCycles()
    {
        TestAdventUtils.testSubmit( 14, (lines) -> Platform.computeLoadWithCycles( lines ), 64 );
    }

    @Test
    public void submitTwoStar()
    {
        AdventUtils.submit( 14, ( lines) -> Platform.computeLoadWithCycles( lines ) );
    }
}
