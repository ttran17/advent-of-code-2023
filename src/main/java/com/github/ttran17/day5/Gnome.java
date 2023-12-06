package com.github.ttran17.day5;

import static com.github.ttran17.day5.Almanac.SectionMapping;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.github.ttran17.ElfUtils;

public class Gnome
{

    protected static List<Chunk> chunkFission( Chunk parent, Almanac almanac )
    {
        List<Chunk> children = new ArrayList<>( );

        List<SectionMapping> sectionMappings = almanac.sectionMappings.get( parent.type );
        for ( SectionMapping sectionMapping : sectionMappings )
        {
            if ( sectionMapping.sourceStart <= parent.end && parent.start <= sectionMapping.sourceEnd )
            {
                long childStart = Math.max( sectionMapping.sourceStart, parent.start );
                long childEnd = Math.min( sectionMapping.sourceEnd, parent.end );
                children.add( new Chunk( parent, parent.type, childStart, childEnd ) );
            }
        }

        return children;
    }

    protected static Chunk chunkTransition( Chunk parent, Almanac almanac )
    {
        List<SectionMapping> sectionMappings = almanac.sectionMappings.get( parent.type );

        for ( SectionMapping sectionMapping : sectionMappings )
        {
            if ( sectionMapping.sourceStart <= parent.start && parent.start <= sectionMapping.sourceEnd )
            {
                long childStart = sectionMapping.destinationStart + parent.start - sectionMapping.sourceStart;
                long childEnd = sectionMapping.destinationStart + Math.min( parent.end, sectionMapping.sourceEnd ) - sectionMapping.sourceStart;

                return new Chunk( parent, sectionMapping.destinationType, childStart, childEnd );
            }
        }

        throw new IllegalStateException( String.format( "Chunk %s could not transition", parent ) );
    }

    protected static long consume( List<String> lines )
    {
        List<Chunk> seedRoots = getSeedRoots( lines.get( 0 ) );
        Almanac almanac = Almanac.createAlmanac( lines );

        return computeMinLocation( seedRoots, almanac );
    }

    protected static long consumeSeedsAsRanges( List<String> lines )
    {
        List<Chunk> seedRoots = getSeedRootsAsRanges( lines.get( 0 ) );
        Almanac almanac = Almanac.createAlmanac( lines );

        return computeMinLocation( seedRoots, almanac );
    }

    protected static List<Chunk> getSeedRoots( String line )
    {
        List<Chunk> seedRoots = new ArrayList<>( );

        String seedsString = line.substring( ElfUtils.getColonIndex( line ) + 1 ).trim( );
        String[] seedTokens = seedsString.split( " " );
        for ( String seedToken : seedTokens )
        {
            long value = Long.parseLong( seedToken.trim( ) );
            seedRoots.add( new Chunk( null, GardenItem.SEED, value, value ) );
        }

        return seedRoots;
    }

    protected static List<Chunk> getSeedRootsAsRanges( String line )
    {
        List<Chunk> seedRoots = new ArrayList<>( );

        String seedsString = line.substring( ElfUtils.getColonIndex( line ) + 1 ).trim( );
        String[] seedTokens = seedsString.split( " " );
        for ( int i = 0; i < seedTokens.length; i = i + 2 )
        {
            long start = Long.parseLong( seedTokens[i].trim( ) );
            long range = Long.parseLong( seedTokens[i + 1].trim( ) );
            long end = start + range - 1;
            seedRoots.add( new Chunk( null, GardenItem.SEED, start, end ) );
        }

        return seedRoots;
    }

    protected static long computeMinLocation( List<Chunk> seedRoots, Almanac almanac )
    {
        long minLocation = Long.MAX_VALUE;

        for ( Chunk root : seedRoots )
        {
            minLocation = computeMinLocation( root, almanac, minLocation );
        }

        return minLocation;
    }

    protected static long computeMinLocation( Chunk root, Almanac almanac, long minLocation )
    {
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
                    if ( childOfTransition.start <= minLocation )
                    {
                        minLocation = childOfTransition.start;
                    }
                    continue;
                }
                queue.add( childOfTransition );
            }
        }

        return minLocation;
    }
}
