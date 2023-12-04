package com.github.ttran17.day2;

public record BallGame(int numberOfBalls, BallColor ballColor)
{

    static BallGame parse( String ballGame )
    {
        // "3 blue"
        int spaceIndex = ballGame.indexOf( ' ' );
        int numberOfBalls = Integer.parseInt( ballGame.substring( 0, spaceIndex ) );
        BallColor ballColor = BallColor.parse( ballGame.substring( spaceIndex + 1 ) );

        return new BallGame( numberOfBalls, ballColor );
    }
}
