package com.github.ttran17;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestAdventUtils
{
    private static final Logger logger = Logger.getLogger( TestAdventUtils.class.getName( ) );

    public static File getTestInputFile( int day )
    {
        return getTestInputFile( day, 1 );
    }

    public static File getTestInputFile( int day, int version )
    {
        String testFilename = version > 1 ? String.format( "testInput-%d.txt", version ) : "testInput.txt";
        return AdventUtils.RESOURCES.resolve( String.valueOf( day ) ).resolve( testFilename ).toFile( );
    }

    public static List<String> getTestLines( int day )
    {
        return getTestLines( day, 1 );
    }

    public static List<String> getTestLines( int day, int version )
    {
        File testInputFile = getTestInputFile( day, version );
        return AdventUtils.readLines( testInputFile );
    }

    public static void testSubmit( int day, AdventUtils.Submission submission, long assertedValue )
    {
        testSubmit( day, 1, submission, assertedValue );
    }

    public static void testSubmit( int day, int version, AdventUtils.Submission submission, long assertedValue )
    {
        File testInputFile = TestAdventUtils.getTestInputFile( day, version );

        List<String> lines = AdventUtils.readLines( testInputFile );

        Assertions.assertEquals( assertedValue, submission.accept( lines ) );
    }

    @Test
    public void ready( ) throws IOException
    {
        File inputFile = AdventUtils.getInputFile( 1 );
        Assertions.assertEquals( "input.txt", inputFile.getName( ) );

        BufferedReader reader = new BufferedReader( new FileReader( inputFile ) );
        Assertions.assertTrue( reader.ready( ) );
        reader.close( );
        Assertions.assertThrows( IOException.class, reader::ready );
    }

    @Test
    public void logListOfLines( )
    {
        File inputFile = AdventUtils.getInputFile( 1 );
        Assertions.assertDoesNotThrow( ( ) -> AdventUtils.readLines( inputFile, ( line ) -> logger.info( ( ) -> String.format( "Line is: %s", line ) ) ) );
    }

    @Test
    public void buildListOfLines( )
    {
        List<String> lines = new ArrayList<>( );
        File inputFile = AdventUtils.getInputFile( 1 );
        AdventUtils.readLines( inputFile, ( line ) -> lines.add( line ) );
        Assertions.assertNotEquals( 0, lines.size( ) );
    }
}
