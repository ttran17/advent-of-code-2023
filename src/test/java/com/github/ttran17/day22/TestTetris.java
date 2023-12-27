package com.github.ttran17.day22;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;
import com.github.ttran17.TestAdventUtils;

public class TestTetris
{
    @Test
    public void testFalling( )
    {
        List<String> lines = TestAdventUtils.getTestLines( 22 );

        Tetris.falling( lines );
    }

    @Test
    public void testFallingRealInput( )
    {
        File inputFile = AdventUtils.getInputFile( 22 );
        List<String> lines = AdventUtils.readLines( inputFile );

        Tetris.falling( lines );
    }

    @Test
    public void testSubmit( )
    {
        TestAdventUtils.testSubmit( 22, ( lines ) -> Tetris.bobaFett( lines ), 5 );
    }

    @Test
    public void submit( )
    {
        AdventUtils.submit( 22, ( lines ) -> Tetris.bobaFett( lines ) );
    }

    @Test
    public void testSubmitTwoStar( )
    {
        TestAdventUtils.testSubmit( 22, ( lines ) -> Tetris.bobaFettLives( lines ), 7 );
    }

    @Test
    public void submitTwoStar( )
    {
        AdventUtils.submit( 22, ( lines ) -> Tetris.bobaFettLives( lines ) );
    }
}
