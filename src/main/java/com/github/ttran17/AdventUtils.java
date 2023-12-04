package com.github.ttran17;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class AdventUtils
{
    private static final Logger logger = Logger.getLogger( AdventUtils.class.getName( ) );

    public static Path RESOURCES = Path.of( "src/main/resources/day" );

    public static File getInputFile( int day )
    {
        return RESOURCES.resolve( String.valueOf( day ) ).resolve( "input.txt" ).toFile( );
    }

    public static List<String> readLines( File inputFile ) throws IOException
    {
        List<String> lines = new ArrayList<>( );
        readLines( inputFile, ( line ) -> lines.add( line ) );
        return lines;
    }

    public static void readLines( File inputFile, Consumer<String> consumer ) throws IOException
    {
        int nLines = 0;

        BufferedReader reader = new BufferedReader( new FileReader( inputFile ) );
        String line = reader.readLine( );
        nLines++;
        while ( line != null )
        {
            consumer.accept( line );
            line = reader.readLine( );
            nLines++;
        }
        reader.close( );

        logger.info( String.format( "Read in %d lines", nLines - 1 ) );
    }

    public static void submit( int day, Submission submission )
    {
        File inputFile = AdventUtils.getInputFile( day );

        try
        {
            List<String> lines = AdventUtils.readLines( inputFile );

            logger.info( ( ) -> String.format( "submit: %d", submission.accept( lines ) ) );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }

    public interface Submission
    {
        int accept( List<String> lines );
    }
}
