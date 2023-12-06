package com.github.ttran17.day5;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;
import com.github.ttran17.TestAdventUtils;

public class TestGnome
{

    @Test
    public void testProgeny( )
    {
        File testInputFile = TestAdventUtils.getTestInputFile( 5 );
        List<String> lines = AdventUtils.readLines( testInputFile );
        Almanac almanac = Almanac.createAlmanac( lines );

        Chunk parent0 = new Chunk( null, GardenItem.SEED, 96, 99 );
        List<Chunk> children0 = Gnome.chunkFission( parent0, almanac );

        Assertions.assertEquals( 2, children0.size( ) );
        Chunk child0 = Gnome.chunkTransition( children0.get( 0 ), almanac );
        Assertions.assertEquals( 98, child0.start );
        Assertions.assertEquals( 99, child0.end );

        Chunk parent1 = new Chunk( null, GardenItem.SOIL, 40, 60 );
        List<Chunk> children1 = Gnome.chunkFission( parent1, almanac );

        Assertions.assertEquals( 3, children1.size( ) );

    }

    @Test
    public void testOneStar( )
    {
        File testInputFile = TestAdventUtils.getTestInputFile( 5 );
        List<String> lines = AdventUtils.readLines( testInputFile );
        Almanac almanac = Almanac.createAlmanac( lines );

        Chunk root = new Chunk( null, GardenItem.SEED, 79, 79 );

        Chunk minLocation = null;

        Queue<Chunk> queue = new LinkedList<>( );
        queue.add( root );
        while ( !queue.isEmpty( ) )
        {
            Chunk chunk = queue.poll( );
            List<Chunk> childrenOfFission = Gnome.chunkFission( chunk, almanac );
            for ( Chunk childOfFission : childrenOfFission )
            {
                Chunk childOfTransition = Gnome.chunkTransition( childOfFission, almanac );
                if ( childOfTransition.type == GardenItem.LOCATION )
                {
                    if ( minLocation == null || childOfTransition.start <= minLocation.start )
                    {
                        minLocation = childOfTransition;
                    }
                    continue;
                }
                queue.add( childOfTransition );
            }
        }

        Assertions.assertEquals( 82, minLocation.start );
    }

    @Test
    public void testSubmitOneStar( )
    {
        TestAdventUtils.testSubmit( 5, ( lines ) -> Gnome.consume( lines ), 35L );
    }

    @Test
    public void submitOneStar( )
    {
        AdventUtils.submit( 5, ( lines ) -> Gnome.consume( lines ) );
    }

    @Test
    public void testSubmitTwoStar( )
    {
        TestAdventUtils.testSubmit( 5, ( lines ) -> Gnome.consumeSeedsAsRanges( lines ), 46L );
    }

    @Test
    public void submitTwoStar( )
    {
        AdventUtils.submit( 5, ( lines ) -> Gnome.consumeSeedsAsRanges( lines ) );
    }
}
