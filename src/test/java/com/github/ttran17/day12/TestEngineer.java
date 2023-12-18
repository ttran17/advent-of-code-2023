package com.github.ttran17.day12;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;
import com.github.ttran17.TestAdventUtils;

public class TestEngineer
{

    @Test
    public void testReconstruct( )
    {
        SpringRow springRow0 = SpringRow.parse( "???.### 1,1,3" );
        Assertions.assertEquals( 1, Engineer.reconstruct( springRow0 ) );

        SpringRow springRow1 = SpringRow.parse( ".??..??...?##. 1,1,3" );
        Assertions.assertEquals( 4, Engineer.reconstruct( springRow1 ) );

        SpringRow springRow2 = SpringRow.parse( "?#?#?#?#?#?#?#? 1,3,1,6" );
        Assertions.assertEquals( 1, Engineer.reconstruct( springRow2 ) );

        SpringRow springRow3 = SpringRow.parse( "????.#...#... 4,1,1" );
        Assertions.assertEquals( 1, Engineer.reconstruct( springRow3 ) );

        SpringRow springRow4 = SpringRow.parse( "????.######..#####. 1,6,5" );
        Assertions.assertEquals( 4, Engineer.reconstruct( springRow4 ) );

        SpringRow springRow5 = SpringRow.parse( "?###???????? 3,2,1" );
        Assertions.assertEquals( 10, Engineer.reconstruct( springRow5 ) );
    }

    @Test
    public void testTrouble( )
    {
        SpringRow springRow = SpringRow.parse( ".#??.??.????###????? 1,1,2,8,3" );
        Assertions.assertEquals( 1, Engineer.reconstruct( springRow ) );

        SpringRow case0 = SpringRow.parse( "????## 3" );
        Assertions.assertEquals( 1, Engineer.reconstruct( case0 ) );

        SpringRow case1 = SpringRow.parse( "?###??? 3" );
        Assertions.assertEquals( 1, Engineer.reconstruct( case1 ) );

        SpringRow case2 = SpringRow.parse( "??#?? 1,1" );
        Assertions.assertEquals( 2, Engineer.reconstruct( case2 ) );
    }

    @Test
    public void testSubmit( )
    {
        TestAdventUtils.testSubmit( 12, ( lines ) -> Engineer.reconstruct( lines ), 21 );
    }

    @Test
    public void submitOneStar( )
    {
        AdventUtils.submit( 12, ( lines ) -> Engineer.reconstruct( lines ) );
    }

    @Test
    public void testTwoStar( )
    {
        TestAdventUtils.testSubmit( 12, 2, ( lines ) -> Engineer.reconstruct( lines ), 16384 );

        TestAdventUtils.testSubmit( 12, 3, ( lines ) -> Engineer.reconstruct( lines ), 3 );
    }

    @Test
    public void testProbe( )
    {
        System.out.println( Engineer.reconstruct( List.of( "?##.?.?? 3,1" ) ) );
        System.out.println( Engineer.reconstruct( List.of( "?##.? 3" ) ) );
        System.out.println( Engineer.reconstruct( List.of( "?.?? 1" ) ) );

        System.out.println( Engineer.reconstruct( List.of( ".??..??...?##...??..??...?##. 1,1,3,1,1,3" ) ) );

        System.out.println( Engineer.reconstruct( List.of( ".??..??...?##. 1,1,3" ) ) );
        System.out.println( Engineer.reconstruct( List.of( ".??..??...?##.?.??..??...?##. 1,1,3,1,1,3" ) ) );
        System.out.println( Engineer.reconstruct( List.of( ".??..??...?##.?.??..??...?##.?.??..??...?##. 1,1,3,1,1,3,1,1,3" ) ) );
        System.out.println( Engineer.reconstruct( List.of( ".??..??...?##.?.??..??...?##.?.??..??...?##.?.??..??...?##. 1,1,3,1,1,3,1,1,3,1,1,3" ) ) );
        System.out.println( Engineer.reconstruct( List.of( ".??..??...?##.?.??..??...?##.?.??..??...?##.?.??..??...?##.?.??..??...?##. 1,1,3,1,1,3,1,1,3,1,1,3,1,1,3" ) ) );

        System.out.println( Engineer.reconstruct( List.of( "?###???????? 3,2,1" ) ) );
        System.out.println( Engineer.reconstruct( List.of( "?###??????????###???????? 3,2,1,3,2,1" ) ) );
        System.out.println( Engineer.reconstruct( List.of( "?###??????????###??????????###???????? 3,2,1,3,2,1,3,2,1" ) ) );
        System.out.println( Engineer.reconstruct( List.of( "?###??????????###??????????###??????????###???????? 3,2,1,3,2,1,3,2,1,3,2,1" ) ) );

        // Computational burden is immense:
        // System.out.println( Engineer.reconstruct( List.of( "?###??????????###??????????###??????????###??????????###???????? 3,2,1,3,2,1,3,2,1,3,2,1,3,2,1" ) ));

        System.out.println( Engineer.reconstruct( List.of( "??#?? 1,1" ) ) );
        System.out.println( Engineer.reconstruct( List.of( "??#?????#?? 1,1,1,1" ) ) );
        System.out.println( Engineer.reconstruct( List.of( "??#?????#?????#?? 1,1,1,1,1,1" ) ) );
        System.out.println( Engineer.reconstruct( List.of( "??#?????#?????#?????#?? 1,1,1,1,1,1,1,1" ) ) );
        System.out.println( Engineer.reconstruct( List.of( "??#?????#?????#?????#?????#?? 1,1,1,1,1,1,1,1,1,1" ) ) );

        System.out.println( Engineer.reconstruct( List.of( "????? 1" ) ) );
    }

    @Test
    public void testTwoStarTrouble( )
    {
        // ..?.????#?????????? 1,1,1,1,1,4
        System.out.println( Engineer.reconstruct( List.of( "..?.????#?????????? 1,1,1,1,1,4" ) ) );
        System.out.println( Engineer.reconstruct( List.of( "..?.????#???????????..?.????#?????????? 1,1,1,1,1,4,1,1,1,1,1,4" ) ) );

        System.out.println( Engineer.reconstruct( List.of( "..?.????#??????????? 1,1,1,1,1,4" ) ) );
        System.out.println( Engineer.reconstruct( List.of( "..?.????#??????????? 1,1,1,1,1,4,1" ) ) );
        System.out.println( Engineer.reconstruct( List.of( "..?.????#???????????..? 1,1,1,1,1,4,1" ) ) );
        System.out.println( Engineer.reconstruct( List.of( "..?.????#???????????..? 1,1,1,1,1,4,1,1" ) ) );

        System.out.println( Engineer.reconstruct( List.of( "..?.????#???????????..?.????# 1,1,1,1,1,4,1,1,1,1" ) ) );

        System.out.println( Engineer.reconstruct( List.of( "??#?? 1,1" ) ) );
        System.out.println( Engineer.reconstruct( List.of( "??#?????#?? 1,1,1,1" ) ) );
        System.out.println( Engineer.reconstruct( List.of( "??#?????#?????#?? 1,1,1,1,1,1" ) ) );
        System.out.println( Engineer.reconstruct( List.of( "??#?????#?????#?????#?? 1,1,1,1,1,1,1,1" ) ) );
        System.out.println( Engineer.reconstruct( List.of( "??#?????#?????#?????#?????#?? 1,1,1,1,1,1,1,1,1,1" ) ) );

        System.out.println( Engineer.reconstruct( List.of( "??#?? 1,1" ) ) );
        System.out.println( Engineer.reconstruct( List.of( "?.#.???.#.? 1,1,1,1" ) ) );
    }

    @Test
    public void testYetAgain( )
    {
        System.out.println( Engineer.reconstruct( List.of( "??#?? 1,1" ) ) );
        System.out.println( Engineer.reconstruct( List.of( "??#?????#?? 1,1,1,1" ) ) );

        System.out.println( Engineer.reconstruct( List.of( "?????#?? 1,1,1,1" ) ) );
    }

    @Test
    public void testNonUnion( )
    {
//        System.out.println( NonUnionEngineer.reconstruct( "??#?? 1,1", 1 ) );
//        System.out.println( NonUnionEngineer.reconstruct( "??#?????#?? 1,1,1,1", 0 ) );

        //        System.out.println( NonUnionEngineer.reconstruct( ".??...?.... 1", 0 ) );
//                System.out.println( NonUnionEngineer.reconstruct( ".??..??...?##. 1,1,3", 0 ) );
        //        System.out.println( NonUnionEngineer.reconstruct( "?###???????? 3,2,1", 0 ) );

                System.out.println( NonUnionEngineer.reconstruct( ".??..??...?##. 1,1,3", 4 ) );
//                System.out.println( NonUnionEngineer.reconstruct( ".??..??...?##.?.??..??...?##. 1,1,3,1,1,3", 0 ) );

//        System.out.println( NonUnionEngineer.reconstruct( ".??..??...?##.?.??..??...?##.?.??..??...?##.?.??..??...?##.?.??..??...?##. 1,1,3,1,1,3,1,1,3,1,1,3,1,1,3", 0 ) );

//                        System.out.println( NonUnionEngineer.reconstruct( "????.#...#... 4,1,1", 4 ) );
//                System.out.println( NonUnionEngineer.reconstruct( "????.######..#####. 1,6,5", 4 ) );
//                System.out.println( NonUnionEngineer.reconstruct( "?###???????? 3,2,1", 4 ) );
    }

    @Test
    public void testSubmitNonUnion( )
    {
        TestAdventUtils.testSubmit( 12, ( lines ) -> NonUnionEngineer.reconstruct( lines, 0 ), 21 );
    }

    @Test
    public void submitOneStarNonUnion( )
    {
        AdventUtils.submit( 12, ( lines ) -> NonUnionEngineer.reconstruct( lines, 0 ) );
    }

    @Test
    public void testSubmitTwoStar( )
    {
        TestAdventUtils.testSubmit( 12, ( lines ) -> NonUnionEngineer.reconstruct( lines, 4 ), 525152 );
    }

    //    @Test1
    //    public void submitTwoStar( )
    //    {
    //        AdventUtils.submit( 12, ( lines ) -> Engineer.reconstructUnfolded( lines ) );
    //    }

}
