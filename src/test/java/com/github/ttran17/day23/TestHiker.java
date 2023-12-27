package com.github.ttran17.day23;

import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;
import com.github.ttran17.TestAdventUtils;

public class TestHiker
{
    @Test
    public void testSubmit()
    {
        TestAdventUtils.testSubmit( 23, (lines) -> Hiker.descend( lines ), 94 );
    }

    @Test
    public void submit()
    {
        AdventUtils.submit( 23, ( lines) -> Hiker.descend( lines ) );
    }
}
