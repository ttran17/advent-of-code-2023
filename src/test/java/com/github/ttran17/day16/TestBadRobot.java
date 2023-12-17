package com.github.ttran17.day16;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;
import com.github.ttran17.TestAdventUtils;

public class TestBadRobot
{

    @Test
    public void test( )
    {
        List<String> lines = TestAdventUtils.getTestLines( 16 );
        Grid grid = new Grid( lines );
        grid.addBadRobot( 0, 0, Direction.EAST );
        grid.energize( );
        grid.print( );
    }

    @Test
    public void testRule( )
    {
        List<String> lines = TestAdventUtils.getTestLines( 16 );
        Grid grid = new Grid( lines );
        grid.addBadRobot( 0, -1, Direction.EAST ); // cf. grid.addBadRobot( 0, 0, Direction.EAST )
        grid.energize( );
        grid.print( );
    }

    @Test
    public void testSubmit( )
    {
        TestAdventUtils.testSubmit( 16, ( lines ) -> Grid.energize( lines ), 46 );
    }

    @Test
    public void submit( )
    {
        AdventUtils.submit( 16, ( lines ) -> Grid.energize( lines ) );
    }

    @Test
    public void testTwoStar( )
    {
        Grid.BadRobotBuilder[] badRobotBuilders = new Grid.BadRobotBuilder[] {
                ( grid ) -> new BadRobot( grid, 0, 3, Direction.SOUTH )
        };
        TestAdventUtils.testSubmit( 16, ( lines ) -> Grid.energizeTwoStar( lines, badRobotBuilders ), 51 );
    }

    @Test
    public void testSubmitTwoStar( )
    {
        TestAdventUtils.testSubmit( 16, ( lines ) -> Grid.energizeTwoStar( lines ), 46 );
    }

    @Test
    public void submitTwoStar( )
    {
        AdventUtils.submit( 16, ( lines ) -> Grid.energizeTwoStar( lines ) );
    }
}
