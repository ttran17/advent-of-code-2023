package com.github.ttran17.day3;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;
import com.github.ttran17.TestAdventUtils;

public class TestPartNumberManager
{

    @Test
    public void testIndexRange( )
    {
        IndexRange range0 = new IndexRange( 1, 3 );

        Assertions.assertTrue( range0.isAdjacent( new IndexRange( 0, 0 ) ) );
        Assertions.assertTrue( range0.isAdjacent( new IndexRange( 0, 1 ) ) );
        Assertions.assertTrue( range0.isAdjacent( new IndexRange( 0, 2 ) ) );
        Assertions.assertTrue( range0.isAdjacent( new IndexRange( 0, 3 ) ) );
        Assertions.assertTrue( range0.isAdjacent( new IndexRange( 0, 4 ) ) );
        Assertions.assertTrue( range0.isAdjacent( new IndexRange( 3, 5 ) ) );
        Assertions.assertTrue( range0.isAdjacent( new IndexRange( 4, 5 ) ) );
        Assertions.assertFalse( range0.isAdjacent( new IndexRange( 5, 6 ) ) );

        IndexRange range1 = new IndexRange( 5, 5 );
        Assertions.assertTrue( range1.isAdjacent( new IndexRange( 4, 4 ) ) );
        Assertions.assertTrue( range1.isAdjacent( new IndexRange( 6, 6 ) ) );
        Assertions.assertFalse( range1.isAdjacent( new IndexRange( 0, 3 ) ) );
        Assertions.assertFalse( range1.isAdjacent( new IndexRange( 7, 10 ) ) );
    }

    @Test
    public void testChar( )
    {
        char zero = '0';
        char nine = '9';

        Assertions.assertEquals( zero, '0' );
        Assertions.assertEquals( nine, '9' );
        Assertions.assertEquals( nine - zero, 9 );
        Assertions.assertTrue( zero <= '4' && '4' <= nine );

        for ( int i = 0; i < 10; i++ )
        {
            char c = String.valueOf( i ).charAt( 0 );
            Assertions.assertTrue( Elf.isDigit( c ) );
        }
    }

    @Test
    public void testSchematicToken( )
    {
        String line = "..@$........962............*.428........**.........752*6................$.380........4*687../.........963*649";

        List<SchematicToken> tokens = Elf.consume( line );
        Assertions.assertEquals( 19, tokens.size( ) );

        List<SchematicToken.Symbol> symbols = PartNumberManager.parse( tokens, PartNumberManager.symbolPredicate, PartNumberManager.symbolMapper );
        Assertions.assertEquals( 10, symbols.size( ) );
        Assertions.assertEquals( '@', symbols.get( 0 ).getSymbol( ) );
        Assertions.assertEquals( '$', symbols.get( 6 ).getSymbol( ) );

        List<SchematicToken.Number> numbers = PartNumberManager.parse( tokens, PartNumberManager.numberPredicate, PartNumberManager.numberMapper );
        Assertions.assertEquals( 9, numbers.size( ) );
        Assertions.assertEquals( 962, numbers.get( 0 ).getValue( ) );
        Assertions.assertEquals( 6, numbers.get( 3 ).getValue( ) );
        Assertions.assertEquals( 4, numbers.get( 5 ).getValue( ) );
        Assertions.assertEquals( 649, numbers.get( 8 ).getValue( ) );
    }

    @Test
    public void testOneStar( )
    {
        File testInputFile = TestAdventUtils.getTestInputFile( 3 );

        List<String> lines = AdventUtils.readLines( testInputFile );

        Assertions.assertEquals( 4361, PartNumberManager.consume( lines ) );
    }

    @Test
    public void submitOneStar( )
    {
        AdventUtils.submit( 3, ( lines ) -> PartNumberManager.consume( lines ) );
    }

    @Test
    public void submitTwoStar( )
    {
        AdventUtils.submit( 3, ( lines ) -> PartNumberManager.consumeGearRatio( lines ) );
    }
}
