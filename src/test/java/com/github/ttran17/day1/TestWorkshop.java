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
}
