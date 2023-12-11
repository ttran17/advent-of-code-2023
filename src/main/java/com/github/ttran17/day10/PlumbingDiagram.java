package com.github.ttran17.day10;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

public record PlumbingDiagram(Tile[][] grid, int bowserRow, int bowserCol)
{
    public Tile getBowser( )
    {
        return grid[bowserRow][bowserCol];
    }

    public void resolveBowserPipeFitting( )
    {
        Tile bowser = getBowser( );

        List<Direction> resolvedDirections = new ArrayList<>( );
        for ( Tile neighbor : bowser.getValidNeighbors( ) )
        {
            if ( neighbor.isLoop( ) )
            {
                int directionOrdinal = neighbor.getNeighbors( ).indexOf( bowser );
                resolvedDirections.add( Direction.values( )[directionOrdinal].getPartner( ) );
            }
        }
        PipeFitting resolvedPipeFitting = PipeFitting.resolve( resolvedDirections );

        Tile resolvedBowser = new Tile( bowserRow, bowserCol, resolvedPipeFitting );
        resolvedBowser.setStep( 0 );
        resolvedBowser.setLoop( true );

        List<Tile> neighbors = bowser.getNeighbors( );
        for ( Direction direction : Direction.values( ) )
        {
            Tile tile = neighbors.get( direction.ordinal( ) );
            if ( tile != null )
            {
                resolvedBowser.setNeighbor( direction, tile );
                tile.setNeighbor( direction.getPartner( ), resolvedBowser );
            }
        }

        grid[bowserRow][bowserCol] = resolvedBowser;
    }

    public String printDepthFirstWalk( Function<Tile, String> printer )
    {
        int nRows = grid.length;
        int nCols = grid[0].length;

        StringBuilder builder = new StringBuilder( System.lineSeparator( ) );

        for ( int row = 1; row < nRows; row++ )
        {
            List<String> steps = new ArrayList<>( );
            for ( int col = 1; col < nCols; col++ )
            {
                Tile tile = grid[row][col];

                steps.add( printer.apply( tile ) );
            }
            builder.append( StringUtils.join( steps, "" ) ).append( System.lineSeparator( ) );
        }

        return builder.toString( );
    }

    public static PlumbingDiagram buildDiagram( List<String> lines )
    {
        int nRows = lines.size( ) + 1;
        int nCols = lines.get( 0 ).length( ) + 1;

        Tile[][] grid = new Tile[nRows][nCols];

        // Pad the list of lines with GROUND at the top
        for ( int col = 0; col < nCols; col++ )
        {
            grid[0][col] = new Tile( 0, col, PipeFitting.GROUND );
        }

        int bowserRow = 0;
        int bowserCol = 0;
        for ( int lineIndex = 0; lineIndex < lines.size( ); lineIndex++ )
        {
            int row = lineIndex + 1;

            // Pad the line with GROUND at the left
            grid[row][0] = new Tile( row, 0, PipeFitting.GROUND );

            String line = lines.get( lineIndex );
            for ( int ch = 0; ch < line.length( ); ch++ )
            {
                int col = ch + 1;
                PipeFitting pipeFitting = PipeFitting.parse( line.charAt( ch ) );
                Tile tile = new Tile( row, col, pipeFitting );
                grid[row][col] = tile;

                if ( pipeFitting == PipeFitting.BOWSER )
                {
                    bowserRow = row;
                    bowserCol = col;
                }

                Tile westTile = grid[row][col - 1];
                tile.checkNeighbor( westTile, Direction.WEST );

                Tile northTile = grid[row - 1][col];
                tile.checkNeighbor( northTile, Direction.NORTH );
            }
        }

        return new PlumbingDiagram( grid, bowserRow, bowserCol );
    }
}
