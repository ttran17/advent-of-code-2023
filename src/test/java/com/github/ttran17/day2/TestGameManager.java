package com.github.ttran17.day2;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;

public class TestGameManager
{

    @Test
    public void testGameNumber( )
    {
        Assertions.assertEquals( 8, Elf.getGameNumber( "Game 8: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green" ) );
        Assertions.assertEquals( 87, Elf.getGameNumber( "Game 87: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green" ) );
        Assertions.assertEquals( 100, Elf.getGameNumber( "Game 100: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green" ) );
    }

    @Test
    public void testBlueBalls( )
    {
        BallGame ballGame = BallGame.parse( "2 blue" );
        Assertions.assertEquals( 2, ballGame.numberOfBalls( ) );
        Assertions.assertEquals( BallColor.BLUE, ballGame.ballColor( ) );
    }

    @Test
    public void testBallGameStrings( )
    {
        String line1 = "Game 8: 3 blue, 4 red; 1 red, 2 green, 2 blue; 2 green";
        List<BallGame> ballGames1 = Elf.getBallGames( line1 );
        Assertions.assertEquals( 6, ballGames1.size( ) );
        Assertions.assertEquals( 2, ballGames1.get( 4 ).numberOfBalls( ) );
        Assertions.assertEquals( BallColor.BLUE, ballGames1.get( 4 ).ballColor( ) );

        String line2 = "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 18 blue, 14 red";
        List<BallGame> ballGames2 = Elf.getBallGames( line2 );
        Assertions.assertEquals( 8, ballGames2.size( ) );
        Assertions.assertEquals( 18, ballGames2.get( 6 ).numberOfBalls( ) );
        Assertions.assertEquals( BallColor.BLUE, ballGames2.get( 6 ).ballColor( ) );
    }

    @Test
    public void testOneStar( ) throws IOException
    {
        File inputFile = AdventUtils.getInputFile( 2 );

        List<String> lines = AdventUtils.readLines( inputFile );

        List<CompleteGame> completeGames = GameManager.consume( lines );
        Assertions.assertEquals( 100, completeGames.size( ) );
    }

    @Test
    public void submitOneStar( )
    {
        BallGameBounds gameBounds = new BallGameBounds(
                new BallGame( 12, BallColor.RED ),
                new BallGame( 13, BallColor.GREEN ),
                new BallGame( 14, BallColor.BLUE )
        );

        AdventUtils.submit( 2, ( lines ) -> GameManager.consume( lines, gameBounds ) );
    }

    @Test
    public void submitTwoStar( )
    {
        AdventUtils.submit( 2, ( lines ) -> GameManager.computePower( lines ) );
    }

    @Test
    public void submitFancyTwoStar( )
    {
        AdventUtils.submit( 2, ( lines ) -> GameManager.computePowerFunctional( lines ) );
    }
}
