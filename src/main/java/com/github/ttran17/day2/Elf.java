package com.github.ttran17.day2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.ttran17.ElfUtils;

public class Elf
{

    protected static int getGameNumber( String line )
    {
        int colonIndex = ElfUtils.getColonIndex( line );
        return Integer.parseInt( line.substring( 5, colonIndex ) );
    }

    protected static List<BallGame> getBallGames( String line )
    {
        List<BallGame> ballGames = new ArrayList<>( );

        // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        int colonIndex = ElfUtils.getColonIndex( line );
        String gamesString = line.substring( colonIndex + 1 );
        String[] games = gamesString.split( ";" );

        // [ " 3 blue, 4 red", "1 red, 2 green, 6 blue", "2 green" ]
        for ( String game : games )
        {
            String[] ballGameStrings = game.split( "," );

            // [ " 3 blue", " 4 red" ]
            for ( String ballGame : ballGameStrings )
            {
                // " 3 blue"
                ballGames.add( BallGame.parse( ballGame.trim( ) ) );
            }
        }

        return Collections.unmodifiableList( ballGames );
    }

    protected static CompleteGame getCompleteGame( String line )
    {
        int gameNumber = getGameNumber( line );
        List<BallGame> ballGames = getBallGames( line );
        return new CompleteGame( gameNumber, ballGames );
    }
}
