package com.github.ttran17.day2;

import java.util.LinkedHashMap;
import java.util.Map;

public class BallGameBounds
{
    private final Map<BallColor, BallGame> bounds;

    protected BallGameBounds( BallGame redBounds, BallGame greenBounds, BallGame blueBounds )
    {
        this.bounds = new LinkedHashMap<>( );

        this.bounds.put( redBounds.ballColor( ), redBounds );
        this.bounds.put( greenBounds.ballColor( ), greenBounds );
        this.bounds.put( blueBounds.ballColor( ), blueBounds );
    }

    public boolean isLegalGame( BallGame ballGame )
    {
        return ballGame.numberOfBalls( ) <= bounds.get( ballGame.ballColor( ) ).numberOfBalls( );
    }
}
