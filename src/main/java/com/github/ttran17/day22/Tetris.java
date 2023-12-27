package com.github.ttran17.day22;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Tetris
{

    protected final Map<Integer, Brick> brickMap = new LinkedHashMap<>( );
    protected final List<int[][]> tower = new LinkedList<>( );

    protected static long bobaFett( List<String> lines )
    {

        Tetris tetris = falling( lines );
        return tetris.brickMap.size( ) - cannotDisintegrate( tetris.tower, tetris.brickMap ).size( );
    }

    protected static long bobaFettLives( List<String> lines )
    {

        Tetris tetris = falling( lines );

        List<int[][]> towerOG = new ArrayList<>( );
        for ( int[][] level : tetris.tower )
        {
            int[][] levelOG = new int[level.length][level[0].length];
            for ( int k = 0; k < level.length; k++ )
            {
                System.arraycopy( level[k], 0, levelOG[k], 0, level[0].length );
            }
            towerOG.add( levelOG );
        }

        long sum = 0;
        Set<Brick> cannotDisintegrate = cannotDisintegrate( tetris.tower, tetris.brickMap );
        for ( Brick brick : cannotDisintegrate )
        {
            for ( int z = brick.startZ; z <= brick.endZ; z++ )
            {
                int[][] level = tetris.tower.get( z );
                for ( int x = brick.startX; x <= brick.endX; x++ )
                {
                    for ( int y = brick.startY; y <= brick.endY; y++ )
                    {
                        level[x][y] = 0;
                    }
                }
            }

            boolean falling = true;
            while ( falling )
            {
                falling = applyGravity( tetris.tower, tetris.brickMap );
            }

            Set<Brick> ignore = new HashSet<>( );
            Set<Brick> fallen = new HashSet<>( );
            for ( int k = 1; k < tetris.tower.size( ); k++ )
            {
                for ( Brick levelBrick : getBricks( tetris.tower.get( k ), tetris.brickMap ) )
                {
                    if ( levelBrick.startZ != k && !ignore.contains( levelBrick ) )
                    {
                        fallen.add( levelBrick );
                    }
                    ignore.add( levelBrick );
                }
            }
            sum += fallen.size( );

            tetris.tower.clear( );
            for ( int[][] levelOG : towerOG )
            {
                int[][] level = new int[levelOG.length][levelOG[0].length];
                for ( int k = 0; k < levelOG.length; k++ )
                {
                    System.arraycopy( levelOG[k], 0, level[k], 0, levelOG[0].length );
                }
                tetris.tower.add( level );
            }
            adjustBrickZ( tetris.tower, tetris.brickMap );
        }

        return sum;
    }

    protected static int[] toInt( String[] tokens )
    {
        int[] array = new int[tokens.length];
        for ( int k = 0; k < tokens.length; k++ )
        {
            array[k] = Integer.parseInt( tokens[k].trim( ) );
        }
        return array;
    }

    protected static boolean isEmpty( int[][] level )
    {
        for ( int x = 0; x < 10; x++ )
        {
            for ( int y = 0; y < 10; y++ )
            {
                if ( level[x][y] > 0 )
                {
                    return false;
                }
            }
        }
        return true;
    }

    protected static Tetris falling( List<String> lines )
    {
        Tetris tetris = new Tetris( );

        Map<Integer, int[][]> towerMap = new TreeMap<>( );

        int id = 100;
        int maxZ = -1;
        for ( String line : lines )
        {
            String[] parts = line.split( "~" );
            String[] startTokens = parts[0].trim( ).split( "," );
            String[] endTokens = parts[1].trim( ).split( "," );

            int[] start = toInt( startTokens );
            int[] end = toInt( endTokens );
            Brick brick = new Brick( id++, start[0], end[0], start[1], end[1], start[2], end[2] );

            tetris.brickMap.put( brick.id, brick );

            if ( maxZ < brick.endZ )
            {
                maxZ = brick.endZ;
            }

            for ( int z = brick.startZ; z <= brick.endZ; z++ )
            {
                int[][] level = towerMap.computeIfAbsent( z, k -> new int[10][10] );
                for ( int x = brick.startX; x <= brick.endX; x++ )
                {
                    for ( int y = brick.startY; y <= brick.endY; y++ )
                    {
                        level[x][y] = brick.id;
                    }
                }
            }
        }

        int[][] ground = new int[10][10];
        for ( int x = 0; x < 10; x++ )
        {
            for ( int y = 0; y < 10; y++ )
            {
                ground[x][y] = 1;
            }
        }
        tetris.tower.add( ground );
        for ( int i = 1; i <= maxZ; i++ )
        {
            tetris.tower.add( towerMap.computeIfAbsent( i, k -> new int[10][10] ) );
        }

        boolean falling = true;
        while ( falling )
        {
            falling = applyGravity( tetris.tower, tetris.brickMap );
        }
        adjustBrickZ( tetris.tower, tetris.brickMap );

        return tetris;
    }

    protected static boolean applyGravity( List<int[][]> tower, Map<Integer, Brick> brickMap )
    {
        boolean falling = false;

        Iterator<int[][]> iterator = tower.iterator( );
        int[][] previousLevel = iterator.next( ); // always the ground level
        while ( iterator.hasNext( ) )
        {
            int[][] level = iterator.next( );
            if ( isEmpty( level ) )
            {
                iterator.remove( );
                continue;
            }

            Set<Brick> levelBricks = getBricks( level, brickMap );

            for ( Brick brick : levelBricks )
            {
                boolean canFall = true;
                for ( int x = brick.startX; x <= brick.endX; x++ )
                {
                    for ( int y = brick.startY; y <= brick.endY; y++ )
                    {
                        if ( previousLevel[x][y] != 0 )
                        {
                            canFall = false;
                            break;
                        }
                    }
                }
                if ( canFall )
                {
                    falling = true;
                    for ( int x = brick.startX; x <= brick.endX; x++ )
                    {
                        for ( int y = brick.startY; y <= brick.endY; y++ )
                        {
                            previousLevel[x][y] = brick.id;
                            level[x][y] = 0;
                        }
                    }
                }
            }

            previousLevel = level;
        }

        return falling;
    }

    protected static void adjustBrickZ( List<int[][]> tower, Map<Integer, Brick> brickMap )
    {
        Set<Brick> adjusted = new HashSet<>( );

        for ( int k = 1; k < tower.size( ); k++ )
        {
            int[][] level = tower.get( k );

            Set<Brick> levelBricks = new LinkedHashSet<>( );
            for ( int x = 0; x < 10; x++ )
            {
                for ( int y = 0; y < 10; y++ )
                {
                    if ( level[x][y] > 0 )
                    {
                        Brick brick = brickMap.get( level[x][y] );
                        if ( !adjusted.contains( brick ) )
                        {
                            levelBricks.add( brick );
                        }
                    }
                }
            }
            for ( Brick brick : levelBricks )
            {
                brick.endZ = k + brick.endZ - brick.startZ;
                brick.startZ = k;

                adjusted.add( brick );
            }
        }
    }

    protected static Set<Brick> getBricks( int[][] level, Map<Integer, Brick> brickMap )
    {
        Set<Brick> levelBricks = new LinkedHashSet<>( );
        for ( int x = 0; x < 10; x++ )
        {
            for ( int y = 0; y < 10; y++ )
            {
                if ( level[x][y] > 0 )
                {
                    levelBricks.add( brickMap.get( level[x][y] ) );
                }
            }
        }
        return levelBricks;
    }

    protected static Set<Brick> cannotDisintegrate( List<int[][]> tower, Map<Integer, Brick> brickMap )
    {
        Set<Brick> cannotDisintegrate = new LinkedHashSet<>( );
        for ( int k = 2; k < tower.size( ); k++ )
        {
            int[][] previousLevel = tower.get( k - 1 );

            for ( Brick brick : getBricks( tower.get( k ), brickMap ) )
            {
                Set<Brick> supportingBricks = new LinkedHashSet<>( );
                for ( int x = brick.startX; x <= brick.endX; x++ )
                {
                    for ( int y = brick.startY; y <= brick.endY; y++ )
                    {
                        if ( previousLevel[x][y] > 0 )
                        {
                            Brick supportingBrick = brickMap.get( previousLevel[x][y] );
                            if ( supportingBrick != brick )
                            {
                                supportingBricks.add( supportingBrick );
                            }
                        }
                    }
                }
                if ( supportingBricks.size( ) == 1 )
                {
                    cannotDisintegrate.addAll( supportingBricks );
                }
            }
        }

        return cannotDisintegrate;
    }
}
