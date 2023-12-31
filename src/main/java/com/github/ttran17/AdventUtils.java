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

    public static List<String> getLines( int day ) {
        return readLines( getInputFile( day ));
    }

    public static List<String> readLines( File inputFile )
    {
        List<String> lines = new ArrayList<>( );
        readLines( inputFile, ( line ) -> lines.add( line ) );
        return lines;
    }

    public static void readLines( File inputFile, Consumer<String> consumer )
    {
        try ( BufferedReader reader = new BufferedReader( new FileReader( inputFile ) ) )
        {
            int nLines = 0;

            String line = reader.readLine( );
            nLines++;
            while ( line != null )
            {
                consumer.accept( line );
                line = reader.readLine( );
                nLines++;
            }

            logger.info( String.format( "Read in %d lines", nLines - 1 ) );
        }
        catch ( IOException e )
        {
            logger.severe( ( ) -> "Encountered: " + e.getMessage( ) );
            throw new RuntimeException( e );
        }
    }

    public static void submit( int day, Submission submission )
    {
        File inputFile = AdventUtils.getInputFile( day );

        List<String> lines = AdventUtils.readLines( inputFile );

        logger.info( ( ) -> String.format( "submit: %d", submission.accept( lines ) ) );
    }

    public interface Submission
    {
        long accept( List<String> lines );
    }

    public static void main(String[] args )
    {
        String main = "src/main/java";
        String test = "src/test/java";
        String resources = "src/main/resources";

        String fqdn = "com/github/ttran17";

        for ( int day = 1; day < 26; day++ )
        {
            if ( !Path.of( main ).resolve( fqdn ).resolve( "day"+day ).toFile( ).exists( ) )
            {
                Path.of( main ).resolve( fqdn ).resolve( "day"+day ).toFile( ).mkdirs( );
            }

            if ( !Path.of( test ).resolve( fqdn ).resolve( "day"+day ).toFile( ).exists( ) )
            {
                Path.of( test ).resolve( fqdn ).resolve( "day"+day ).toFile( ).mkdirs( );
            }

            if ( !Path.of( resources ).resolve( "day" ).resolve( String.valueOf( day ) ).toFile( ).exists( ) )
            {
                Path.of( resources ).resolve( "day" ).resolve( String.valueOf( day ) ).toFile( ).mkdirs( );
            }
        }
    }
}
