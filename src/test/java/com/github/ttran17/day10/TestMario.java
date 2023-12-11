package com.github.ttran17.day10;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;
import com.github.ttran17.TestAdventUtils;

public class TestMario
{
    @Test
    public void testTile( )
    {
        Assertions.assertEquals( Direction.NORTH, PipeFitting.NORTH_AND_EAST.getConnection( PipeFitting.SOUTH_AND_WEST, Direction.NORTH ) );
        Assertions.assertEquals( Direction.WEST, PipeFitting.SOUTH_AND_WEST.getConnection( PipeFitting.NORTH_AND_EAST, Direction.WEST ) );

        Assertions.assertEquals( Direction.WEST, PipeFitting.SOUTH_AND_WEST.getConnection( PipeFitting.HORIZONTAL, Direction.WEST ) );

        Assertions.assertEquals( null, PipeFitting.VERTICAL.getConnection( PipeFitting.NORTH_AND_WEST, Direction.NORTH ) );
        Assertions.assertEquals( null, PipeFitting.SOUTH_AND_EAST.getConnection( PipeFitting.VERTICAL, Direction.NORTH ) );
        Assertions.assertEquals( null, PipeFitting.SOUTH_AND_EAST.getConnection( PipeFitting.HORIZONTAL, Direction.NORTH ) );

        Assertions.assertEquals( null, PipeFitting.HORIZONTAL.getConnection( PipeFitting.NORTH_AND_WEST, Direction.WEST ) );
        Assertions.assertEquals( null, PipeFitting.HORIZONTAL.getConnection( PipeFitting.SOUTH_AND_WEST, Direction.WEST ) );

        Tile tile = new Tile( 0, 1, PipeFitting.SOUTH_AND_WEST );

        Tile otherTile0 = new Tile( 0, 0, PipeFitting.NORTH_AND_EAST );
        tile.checkNeighbor( otherTile0, Direction.WEST );
        Assertions.assertEquals( otherTile0, tile.getNeighbors( ).get( Direction.WEST.ordinal( ) ) );

        Tile otherTile1 = new Tile( -1, -1, PipeFitting.VERTICAL );
        tile.checkNeighbor( otherTile1, Direction.NORTH );
        Assertions.assertEquals( null, tile.getNeighbors( ).get( Direction.NORTH.ordinal( ) ) );
    }

    @Test
    public void testGrid( )
    {
        File testInputFile = TestAdventUtils.getTestInputFile( 10 );
        List<String> lines = AdventUtils.readLines( testInputFile );

        PlumbingDiagram diagram = PlumbingDiagram.buildDiagram( lines );
        Assertions.assertEquals( 3, diagram.bowserRow( ) );
        Assertions.assertEquals( 1, diagram.bowserCol( ) );
        Assertions.assertEquals( 6, diagram.grid( ).length );
        Assertions.assertEquals( 6, diagram.grid( )[0].length );

        Tile tile = diagram.grid( )[3][5];
        Assertions.assertEquals( PipeFitting.SOUTH_AND_WEST, tile.pipeFitting );
        Assertions.assertEquals( null, tile.getNeighbors( ).get( Direction.NORTH.ordinal( ) ) );
        Assertions.assertEquals( PipeFitting.NORTH_AND_EAST, tile.getNeighbors( ).get( Direction.WEST.ordinal( ) ).pipeFitting );
        Assertions.assertEquals( PipeFitting.NORTH_AND_WEST, tile.getNeighbors( ).get( Direction.SOUTH.ordinal( ) ).pipeFitting );

        Tile bowser = diagram.grid( )[3][1];
        Tile[][] grid = diagram.grid( );
        Assertions.assertEquals( PipeFitting.BOWSER, bowser.pipeFitting );
    }

    @Test
    public void testDiagram( )
    {
        File testInputFile = TestAdventUtils.getTestInputFile( 10 );
        List<String> lines = AdventUtils.readLines( testInputFile );

        PlumbingDiagram diagram = PlumbingDiagram.buildDiagram( lines );
        Mario.depthFirstWalk( diagram );

        System.out.println( diagram.printDepthFirstWalk( ( tile ) -> tile.pipeFitting.toString( ) ) );
    }

    @Test
    public void testWalk( )
    {
        File testInputFile = TestAdventUtils.getTestInputFile( 10 );
        List<String> lines = AdventUtils.readLines( testInputFile );

        PlumbingDiagram diagram = PlumbingDiagram.buildDiagram( lines );
        Mario.depthFirstWalk( diagram );

        System.out.println( diagram.printDepthFirstWalk( ( tile ) -> {
            int step = tile.getStep( );
            return step < 0 ? "." : String.valueOf( step );
        } ) );
    }

    @Test
    public void testLoop( )
    {
        File testInputFile = TestAdventUtils.getTestInputFile( 10, 6 );
        List<String> lines = AdventUtils.readLines( testInputFile );

        PlumbingDiagram diagram = PlumbingDiagram.buildDiagram( lines );
        Mario.depthFirstWalk( diagram );

        Tile peak = Mario.findPeak( diagram );

        Mario.markLoop( diagram, peak );

        System.out.println( diagram.printDepthFirstWalk( ( tile ) -> tile.isLoop( ) ? "*" : "." ) );
    }

    @Test
    public void testInside( )
    {
        File testInputFile = TestAdventUtils.getTestInputFile( 10, 2 );
        List<String> lines = AdventUtils.readLines( testInputFile );

        PlumbingDiagram diagram = PlumbingDiagram.buildDiagram( lines );
        Mario.depthFirstWalk( diagram );

        Tile peak = Mario.findPeak( diagram );

        Mario.markLoop( diagram, peak );

        Mario.walkJordan( diagram, peak );

        Mario.countInside( diagram );

        System.out.println( diagram.printDepthFirstWalk( ( tile ) -> tile.isInside( ) ? "I" : "." ) );
    }

    @Test
    public void testSubmit( )
    {
        TestAdventUtils.testSubmit( 10, ( lines ) -> Mario.walk( lines ), 8 );
    }

    @Test
    public void submitOneStar( )
    {
        AdventUtils.submit( 10, ( lines ) -> Mario.walk( lines ) );
    }

    @Test
    public void testSubmitTwoStar( )
    {
        TestAdventUtils.testSubmit( 10, 2, ( lines ) -> Mario.walkJordan( lines ), 4 );
        TestAdventUtils.testSubmit( 10, 3, ( lines ) -> Mario.walkJordan( lines ), 4 );
        TestAdventUtils.testSubmit( 10, 4, ( lines ) -> Mario.walkJordan( lines ), 4 );
        TestAdventUtils.testSubmit( 10, 5, ( lines ) -> Mario.walkJordan( lines ), 8 );
        TestAdventUtils.testSubmit( 10, 6, ( lines ) -> Mario.walkJordan( lines ), 10 );
    }

    @Test
    public void submitTwoStar( )
    {
        AdventUtils.submit( 10, ( lines ) -> Mario.walkJordan( lines ) );
    }
}
