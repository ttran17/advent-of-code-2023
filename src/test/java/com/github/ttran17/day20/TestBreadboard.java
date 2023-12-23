package com.github.ttran17.day20;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;
import com.github.ttran17.TestAdventUtils;

public class TestBreadboard
{

    @Test
    public void testBreadboard()
    {
        List<String> lines = TestAdventUtils.getTestLines( 20 );
        Faraday.analyze( lines );
    }

    @Test
    public void testBreadboard2()
    {
        List<String> lines = TestAdventUtils.getTestLines( 20, 2 );
        Faraday.analyze( lines );
    }

    @Test
    public void testSubmit()
    {
        TestAdventUtils.testSubmit( 20, (lines) -> Faraday.analyze( lines ), 32000000L );
    }

    @Test
    public void testSubmit2()
    {
        TestAdventUtils.testSubmit( 20, 2,(lines) -> Faraday.analyze( lines ), 11687500L );
    }

    @Test
    public void submit()
    {
        AdventUtils.submit( 20, ( lines) -> Faraday.analyze( lines ) );
    }

    @Test
    public void submitTwoStar()
    {
        AdventUtils.submit( 20, ( lines) -> Faraday.analyzeRx( lines ) );
    }
}
