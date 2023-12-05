package com.github.ttran17.day1;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;

public class TestWorkshop
{

    @Test
    public void charTest( )
    {
        String goal1 = "1";
        Assertions.assertEquals( '1', goal1.charAt( 0 ) );

        String goal2 = "one";
        Assertions.assertEquals( 'o', goal2.charAt( 0 ) );
    }

    @Test
    public void testOneStar( )
    {
        Assertions.assertEquals( 73, Workshop.consume( List.of( "oneonefour7193eight" ) ) );
        Assertions.assertEquals( 11, Workshop.consume( List.of( "1fourrj" ) ) );
        Assertions.assertEquals( 82, Workshop.consume( List.of( "1fourrj", "7ltsp1seventhreesix" ) ) );
    }

    @Test
    public void submitOneStar( )
    {
        AdventUtils.submit( 1, ( lines ) -> Workshop.consume( lines ) );
    }

    @Test
    public void testTwoStar( )
    {
        Assertions.assertEquals( 12, Workshop.consumeDigitsAndWords( List.of( "oonetwoo" ) ) );
        Assertions.assertEquals( 13, Workshop.consumeDigitsAndWords( List.of( "abcone2threexyz" ) ) );
        Assertions.assertEquals( 24, Workshop.consumeDigitsAndWords( List.of( "xtwone3four" ) ) );
        Assertions.assertEquals( 76, Workshop.consumeDigitsAndWords( List.of( "7pqrstsixteen" ) ) );
        Assertions.assertEquals( 97, Workshop.consumeDigitsAndWords( List.of( "zoneight234", "eightwothree" ) ) );

        Assertions.assertEquals( 281, Workshop.consumeDigitsAndWords( List.of(
                "two1nine",
                "eightwothree",
                "abcone2threexyz",
                "xtwone3four",
                "4nineeightseven2",
                "zoneight234",
                "7pqrstsixteen"
        ) ) );
    }

    @Test
    public void submitTwoStar( )
    {
        AdventUtils.submit( 1, ( lines ) -> Workshop.consumeDigitsAndWords( lines ) );
    }

    @Test
    public void submitOneStarNonUnionElf( )
    {
        List<NonUnionElf.NumberKey> keys = List.of(
                new NonUnionElf.NumberKey( "1", 1 ),
                new NonUnionElf.NumberKey( "2", 2 ),
                new NonUnionElf.NumberKey( "3", 3 ),
                new NonUnionElf.NumberKey( "4", 4 ),
                new NonUnionElf.NumberKey( "5", 5 ),
                new NonUnionElf.NumberKey( "6", 6 ),
                new NonUnionElf.NumberKey( "7", 7 ),
                new NonUnionElf.NumberKey( "8", 8 ),
                new NonUnionElf.NumberKey( "9", 9 )
        );

        AdventUtils.submit( 1, ( lines ) -> NonUnionElf.consume( lines, keys ) );
    }

    @Test
    public void submitTwoStarNonUnionElf( )
    {
        List<NonUnionElf.NumberKey> keys = List.of(
                new NonUnionElf.NumberKey( "1", 1 ),
                new NonUnionElf.NumberKey( "2", 2 ),
                new NonUnionElf.NumberKey( "3", 3 ),
                new NonUnionElf.NumberKey( "4", 4 ),
                new NonUnionElf.NumberKey( "5", 5 ),
                new NonUnionElf.NumberKey( "6", 6 ),
                new NonUnionElf.NumberKey( "7", 7 ),
                new NonUnionElf.NumberKey( "8", 8 ),
                new NonUnionElf.NumberKey( "9", 9 ),
                new NonUnionElf.NumberKey( "one", 1 ),
                new NonUnionElf.NumberKey( "two", 2 ),
                new NonUnionElf.NumberKey( "three", 3 ),
                new NonUnionElf.NumberKey( "four", 4 ),
                new NonUnionElf.NumberKey( "five", 5 ),
                new NonUnionElf.NumberKey( "six", 6 ),
                new NonUnionElf.NumberKey( "seven", 7 ),
                new NonUnionElf.NumberKey( "eight", 8 ),
                new NonUnionElf.NumberKey( "nine", 9 )
        );

        AdventUtils.submit( 1, ( lines ) -> NonUnionElf.consume( lines, keys ) );
    }
}
