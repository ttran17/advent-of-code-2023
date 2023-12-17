package com.github.ttran17.day15;

import static com.github.ttran17.day15.CornedBeef.hash;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;
import com.github.ttran17.TestAdventUtils;

public class TestHash
{
    @Test
    public void testAscii( )
    {
        System.out.println( ( int ) 'H' );
        System.out.println( ( int ) 'A' );
        System.out.println( ( int ) 'S' );
    }

    @Test
    public void testOps( )
    {
        Assertions.assertEquals( 52, hash( "HASH" ) );
        Assertions.assertEquals( 0, hash( "rn" ) );
        Assertions.assertEquals( 0, hash( "cm" ) );
        Assertions.assertEquals( 1, hash( "qp" ) );
        Assertions.assertEquals( 3, hash( "pc" ) );
        Assertions.assertEquals( 3, hash( "ot" ) );
        Assertions.assertEquals( 3, hash( "ab" ) );
    }

    @Test
    public void testHash( )
    {
        TestAdventUtils.testSubmit( 15, ( lines ) -> CornedBeef.prepare( lines ), 1320 );
    }

    @Test
    public void submitOneStar( )
    {
        AdventUtils.submit( 15, ( lines ) -> CornedBeef.prepare( lines ) );
    }

    @Test
    public void testHashMap( )
    {
        TestAdventUtils.testSubmit( 15, ( lines ) -> CornedBeef.prepareLinkedHash( lines ), 145 );
    }

    @Test
    public void submitTwoStar( )
    {
        AdventUtils.submit( 15, ( lines ) -> CornedBeef.prepareLinkedHash( lines ) );
    }
}
