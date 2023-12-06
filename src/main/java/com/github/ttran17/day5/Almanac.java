package com.github.ttran17.day5;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Almanac
{
    protected final Map<GardenItem, List<SectionMapping>> sectionMappings;

    public Almanac( Map<GardenItem, List<SectionMapping>> sectionMappings )
    {
        this.sectionMappings = sectionMappings;
    }

    public record AlmanacSectionHeader(GardenItem source, GardenItem destination)
    {

    }

    protected static AlmanacSectionHeader parseSectionHeader( String line )
    {
        // e.g. seed-to-soil map:
        for ( GardenItem type : GardenItem.values( ) )
        {
            if ( line.startsWith( type.toString( ) ) )
            {
                String header = line.substring( 0, line.indexOf( ' ' ) );
                String[] tokens = header.split( "-" );
                GardenItem source = GardenItem.parse( tokens[0].trim( ) );
                GardenItem destination = GardenItem.parse( tokens[2].trim( ) );
                return new AlmanacSectionHeader( source, destination );
            }
        }

        return null;
    }

    protected static Almanac createAlmanac( List<String> lines )
    {
        Map<GardenItem, List<SectionMapping>> almanacSectionMappings = new LinkedHashMap<>( );

        GardenItem currentSourceType = null;
        GardenItem currentDestinationType = null;
        for ( int i = 1; i < lines.size( ); i++ )
        {
            String line = lines.get( i );

            if ( line.trim( ).isEmpty( ) )
            {
                continue;
            }

            AlmanacSectionHeader almanacSectionHeader = parseSectionHeader( line );
            if ( almanacSectionHeader != null )
            {
                currentSourceType = almanacSectionHeader.source( );
                currentDestinationType = almanacSectionHeader.destination( );
                continue;
            }

            updateAlmanacSectionMapping( line, currentSourceType, currentDestinationType, almanacSectionMappings );
        }

        long maxNumber = Long.MIN_VALUE;
        for ( List<SectionMapping> sectionMapping : almanacSectionMappings.values( ) )
        {
            sectionMapping.sort( Comparator.comparingLong( o -> o.destinationStart ) );
            maxNumber = max( maxNumber, sectionMapping );
        }

        Map<GardenItem, List<SectionMapping>> backfilledSectionMappings = new LinkedHashMap<>( );
        for ( Map.Entry<GardenItem, List<SectionMapping>> entry : almanacSectionMappings.entrySet( ) )
        {
            backfilledSectionMappings.put( entry.getKey( ), backfill( entry.getValue( ), maxNumber ) );
        }

        for ( List<SectionMapping> sectionMapping : backfilledSectionMappings.values( ) )
        {
            sectionMapping.sort( Comparator.comparingLong( o -> o.sourceStart ) );
            maxNumber = max( maxNumber, sectionMapping );
        }

        return new Almanac( backfilledSectionMappings );
    }

    protected static void updateAlmanacSectionMapping( String line, GardenItem source, GardenItem destination, Map<GardenItem, List<SectionMapping>> almanacSectionMappings )
    {
        String[] tokens = line.split( " " );

        long range = Long.parseLong( tokens[2].trim( ) );
        long sourceStart = Long.parseLong( tokens[1].trim( ) );
        long destinationStart = Long.parseLong( tokens[0].trim( ) );

        almanacSectionMappings.computeIfAbsent( source, s -> new ArrayList<>( ) )
                              .add( new SectionMapping( source, destination, sourceStart, destinationStart, range ) );

    }

    protected static long max( long maxNumber, List<SectionMapping> sectionMappings )
    {
        for ( SectionMapping sectionMapping : sectionMappings )
        {
            long maxSource = sectionMapping.sourceEnd;
            long maxDestination = sectionMapping.destinationEnd;

            maxNumber = Math.max( Math.max( maxNumber, maxDestination ), maxSource );
        }

        return maxNumber;
    }

    protected static List<SectionMapping> backfill( List<SectionMapping> sectionMappings, long maxDestination )
    {
        List<SectionMapping> backfilledList = new ArrayList<>( );

        Queue<SectionMapping> queue = new LinkedList<>( sectionMappings );

        long highestDestination = 0;
        while ( !queue.isEmpty( ) )
        {
            SectionMapping sectionMapping = queue.poll( );
            if ( highestDestination < sectionMapping.destinationStart )
            {
                // backfill missing section
                long range = sectionMapping.destinationStart - highestDestination;
                SectionMapping defaultMapping = new SectionMapping( sectionMapping.sourceType, sectionMapping.destinationType, highestDestination, highestDestination, range );
                backfilledList.add( defaultMapping );
            }
            backfilledList.add( sectionMapping );
            highestDestination = sectionMapping.destinationEnd + 1;
        }
        if ( highestDestination < maxDestination + 1 )
        {
            long range = maxDestination + 1 - highestDestination;
            SectionMapping defaultMapping = new SectionMapping( sectionMappings.get( 0 ).sourceType, sectionMappings.get( 0 ).destinationType, highestDestination, highestDestination, range );
            backfilledList.add( defaultMapping );
        }

        return backfilledList;
    }

    public static class SectionMapping
    {
        protected final GardenItem sourceType;
        protected final GardenItem destinationType;

        protected final long sourceStart;
        protected final long sourceEnd;
        protected final long destinationStart;
        protected final long destinationEnd;

        public SectionMapping( GardenItem sourceType, GardenItem destinationType, long sourceStart, long destinationStart, long range )
        {
            this.sourceType = sourceType;
            this.destinationType = destinationType;
            this.sourceStart = sourceStart;
            this.sourceEnd = sourceStart + range - 1;
            this.destinationStart = destinationStart;
            this.destinationEnd = destinationStart + range - 1;
        }

        @Override
        public String toString( )
        {
            return String.format( "[%s, %d, %d] --> [%s, %d, %d]", sourceType, sourceStart, sourceEnd, destinationType, destinationStart, destinationEnd );
        }
    }
}
