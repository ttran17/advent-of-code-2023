package com.github.ttran17.day11;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.lang3.function.TriConsumer;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

public class Universe
{
    protected final List<List<Galaxy>> rows;
    protected final List<List<Galaxy>> cols;

    protected final List<Galaxy> allGalaxies;

    protected Universe( List<List<Galaxy>> rows, List<List<Galaxy>> cols )
    {
        this.rows = rows;
        this.cols = cols;

        this.allGalaxies = new ArrayList<>( );
        allGalaxies.addAll( rows.stream( ).flatMap( list -> list.stream( ) ).collect( Collectors.toList( ) ) );
    }

    protected void expand( )
    {
        expand( 2, ( galaxies, dims2Expand, expansionFactor ) -> {
            for ( int d : dims2Expand )
            {
                for ( int i = 0; i < expansionFactor - 1; i++ )
                {
                    galaxies.add( d, new ArrayList<>( ) );
                }
            }
        } );
    }

    protected void expand( int expansionFactor, TriConsumer<List<List<Galaxy>>, IntList, Integer> universeExpander )
    {
        IntList rows2expand = findExpansionDimensions( rows );
        IntList cols2expand = findExpansionDimensions( cols );

        int offset = expansionFactor - 1;
        expand( rows, rows2expand, ( galaxy -> galaxy.setRow( galaxy.getRow( ) + offset ) ), expansionFactor, universeExpander );
        expand( cols, cols2expand, ( galaxy -> galaxy.setCol( galaxy.getCol( ) + offset ) ), expansionFactor, universeExpander );
    }

    protected IntList findExpansionDimensions( List<List<Galaxy>> galaxies )
    {
        IntList dims2expand = new IntArrayList( );
        for ( int i = galaxies.size( ) - 1; i > -1; i-- )
        {
            if ( galaxies.get( i ).isEmpty( ) )
            {
                dims2expand.add( i );
            }
        }
        return dims2expand;
    }

    protected void expand( List<List<Galaxy>> galaxies, IntList dims2Expand, Consumer<Galaxy> galaxyExpander, int expansionFactor, TriConsumer<List<List<Galaxy>>, IntList, Integer> universeExpander )
    {
        for ( int d : dims2Expand )
        {
            List<List<Galaxy>> toExpand = galaxies.subList( d + 1, galaxies.size( ) );
            for ( List<Galaxy> list : toExpand )
            {
                for ( Galaxy galaxy : list )
                {
                    galaxyExpander.accept( galaxy );
                }
            }
        }

        universeExpander.accept( galaxies, dims2Expand, expansionFactor );
    }

    protected static class Galaxy
    {
        private int row;
        private int col;

        private char c;

        public Galaxy( int row, int col, char c )
        {
            this.row = row;
            this.col = col;
            this.c = c;
        }

        public int getRow( )
        {
            return row;
        }

        public void setRow( int row )
        {
            this.row = row;
        }

        public int getCol( )
        {
            return col;
        }

        public void setCol( int col )
        {
            this.col = col;
        }

        public char getC( )
        {
            return c;
        }

        public void setC( char c )
        {
            this.c = c;
        }
    }

    protected static Universe build( List<String> lines )
    {
        Int2ObjectMap<List<Galaxy>> galaxiesByRow = new Int2ObjectArrayMap<>( );
        Int2ObjectMap<List<Galaxy>> galaxiesByCol = new Int2ObjectArrayMap<>( );

        int nRows = lines.size( );
        int nCols = lines.get( 0 ).trim( ).length( );

        for ( int row = 0; row < nRows; row++ )
        {
            String line = lines.get( row ).trim( );
            for ( int col = 0; col < nCols; col++ )
            {
                char c = line.charAt( col );
                if ( c == '#' )
                {
                    Universe.Galaxy galaxy = new Galaxy( row, col, c );
                    galaxiesByRow.computeIfAbsent( row, key -> new ArrayList<>( ) ).add( galaxy );
                    galaxiesByCol.computeIfAbsent( col, key -> new ArrayList<>( ) ).add( galaxy );
                }
            }
        }

        return build( galaxiesByRow, galaxiesByCol, nRows, nCols );
    }

    protected static Universe build( Int2ObjectMap<List<Galaxy>> galaxiesByRow, Int2ObjectMap<List<Galaxy>> galaxiesByCol, int nRows, int nCols )
    {
        List<List<Galaxy>> galaxyListByRow = build( galaxiesByRow, nRows );
        List<List<Galaxy>> galaxyListByCol = build( galaxiesByCol, nCols );

        return new Universe( galaxyListByRow, galaxyListByCol );
    }

    protected static List<List<Galaxy>> build( Int2ObjectMap<List<Galaxy>> galaxyMap, int maxIndex )
    {
        List<List<Galaxy>> galaxyList = new LinkedList<>( );
        for ( int i = 0; i < maxIndex; i++ )
        {
            List<Galaxy> list = galaxyMap.getOrDefault( i, new ArrayList( ) );
            galaxyList.add( list );
        }
        return galaxyList;
    }
}
