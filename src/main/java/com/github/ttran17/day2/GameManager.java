package com.github.ttran17.day2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GameManager
{

    protected static List<CompleteGame> consume( List<String> lines )
    {
        List<CompleteGame> completeGames = new ArrayList<>( );
        for ( String line : lines )
        {
            completeGames.add( Elf.getCompleteGame( line ) );
        }
        return completeGames;
    }

    protected static boolean consume( CompleteGame completeGame, BallGameBounds gameBounds )
    {
        for ( BallGame game : completeGame.ballGames( ) )
        {
            if ( !gameBounds.isLegalGame( game ) )
            {
                return false;
            }
        }
        return true;
    }

    protected static int consume( List<String> lines, BallGameBounds gameBounds )
    {
        List<CompleteGame> completeGames = consume( lines );

        int sum = 0;
        for ( CompleteGame completeGame : completeGames )
        {
            if ( consume( completeGame, gameBounds ) )
            {
                sum += completeGame.gameNumber( );
            }
        }
        return sum;
    }

    protected static int computePower( List<String> lines )
    {
        List<CompleteGame> completeGames = consume( lines );

        int sum = 0;
        for ( CompleteGame completeGame : completeGames )
        {
            sum += computePower( completeGame );
        }
        return sum;
    }

    protected static int computePower( CompleteGame completeGame )
    {
        Map<BallColor, BallGame> maxBallGameMap = new LinkedHashMap<>( );

        for ( BallGame game : completeGame.ballGames( ) )
        {
            BallGame maxBallGame = maxBallGameMap.get( game.ballColor( ) );
            if ( maxBallGame == null )
            {
                maxBallGameMap.put( game.ballColor( ), game );
            }
            else
            {
                if ( maxBallGame.numberOfBalls( ) < game.numberOfBalls( ) )
                {
                    maxBallGameMap.put( game.ballColor( ), game );
                }
            }
        }

        int product = 1;
        for ( BallGame maxBallGame : maxBallGameMap.values( ) )
        {
            product *= maxBallGame.numberOfBalls( );
        }
        return product;
    }

    protected static int computePowerFunctional( List<String> lines )
    {
        List<CompleteGame> completeGames = consume( lines );

        int sum = 0;
        for ( CompleteGame completeGame : completeGames )
        {
            sum += computePowerFunctional( completeGame );
        }
        return sum;
    }

    protected static int computePowerFunctional( CompleteGame completeGame )
    {
        return computePowerFunctional( completeGame, BallColor.RED ) *
                computePowerFunctional( completeGame, BallColor.GREEN ) *
                computePowerFunctional( completeGame, BallColor.BLUE );
    }

    protected static int computePowerFunctional( CompleteGame completeGame, BallColor ballColor )
    {
        Optional<Integer> ballMax = completeGame.ballGames( )
                                                .stream( )
                                                .filter( ballGame -> ballGame.ballColor( ) == ballColor )
                                                .map( ballGame -> ballGame.numberOfBalls( ) )
                                                .max( Comparator.naturalOrder( ) );

        return ballMax.orElseThrow( ( ) -> new IllegalStateException( String.format( "Did not find color: %s", ballColor.toString( ) ) ) );
    }
}
