package com.github.ttran17.day19;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;
import com.github.ttran17.RegexUtils;
import com.github.ttran17.TestAdventUtils;
import com.google.common.collect.Range;

public class TestEscher
{
    @Test
    public void testRegex( )
    {
        Assertions.assertEquals( "x=787,m=2655,a=1222,s=2876", RegexUtils.removeCurlyBraces( "{x=787,m=2655,a=1222,s=2876}" ) );
        Assertions.assertEquals( 3, "qqz{s>2770:qs,m<1801:hdj,R}".indexOf( "{" ) );
    }

    @Test
    public void testGuava( )
    {
        Range<Integer> range = Range.closed( 1, 4000 );
        Range<Integer> range2 = Range.openClosed( 4000, 5000 );

        Range<Integer> result = range.intersection( range2 );
        Assertions.assertEquals( true, result.isEmpty( ) );

        Assertions.assertEquals( Range.openClosed( 1531, 4000 ), range.intersection( Range.open( 1531, 10000 ) ) );

        Range<Integer> range3 = Range.openClosed( 3500, 6000 );
        Assertions.assertThrows( IllegalArgumentException.class, ( ) -> range.gap( range3 ) );

        Assertions.assertEquals( 4, RangeRating.length( Range.closed( 4, 7 ) ) );
        Assertions.assertEquals( 3, RangeRating.length( Range.closedOpen( 4, 7 ) ) );
        Assertions.assertEquals( 3, RangeRating.length( Range.openClosed( 4, 7 ) ) );
        Assertions.assertEquals( 2, RangeRating.length( Range.open( 4, 7 ) ) );
    }

    @Test
    public void testSubmit( )
    {
        TestAdventUtils.testSubmit( 19, ( lines ) -> Escher.accumulate( lines ), 19114 );
    }

    @Test
    public void submit( )
    {
        AdventUtils.submit( 19, ( lines ) -> Escher.accumulate( lines ) );
    }

    @Test
    public void testTwoStar( )
    {
        TestAdventUtils.testSubmit( 19, ( lines ) -> RangeEscher.distinct( lines ), 167409079868000L );
    }

    @Test
    public void testRange( )
    {
        List<String> lines = TestAdventUtils.getTestLines( 19 );
        Map<String, Workflow> workflowMap = new LinkedHashMap<>( );

        Escher.accumulate( workflowMap, lines );

        System.out.println( Escher.accumulate( workflowMap, "in", List.of( new Rating( "{x=787,m=2655,a=1222,s=2876}" ) ) ) );
        System.out.println( Escher.accumulate( workflowMap, "in", List.of( new Rating( "{x=2036,m=264,a=79,s=2244}" ) ) ) );
        System.out.println( Escher.accumulate( workflowMap, "in", List.of( new Rating( "{x=2127,m=1623,a=2188,s=1013}" ) ) ) );

        System.out.println( Escher.accumulate( workflowMap, "in", List.of( new Rating( "{x=1417,m=1623,a=2188,s=2166}" ) ) ) );
    }

    @Test
    public void submitTwoStar( )
    {
        AdventUtils.submit( 19, ( lines ) -> RangeEscher.distinct( lines ) );
    }
}
