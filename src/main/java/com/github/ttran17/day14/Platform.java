package com.github.ttran17.day14;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public class Platform
{

    protected final List<Rock> allRoundRocks;

    protected final Rock[][] grid;

    protected Platform( List<LinkedList<Rock>> rows, List<LinkedList<Rock>> cols )
    {
        this.grid = new Rock[rows.size( )][];
        for ( int row = 0; row < rows.size( ); row++ )
        {
            this.grid[row] = rows.get( row ).toArray( new Rock[0] );
        }

        this.allRoundRocks = rows.stream( ).flatMap( rocks -> rocks.stream( ) ).collect( Collectors.toList( ) );
    }

    protected static void tilt( Iterator<Rock> iterator )
    {
        RockType lastRockType = RockType.SQUARE;
        while ( iterator.hasNext( ) )
        {
            Rock currentRock = iterator.next( );
            if ( currentRock.getType( ) == RockType.EMPTY && lastRockType == RockType.ROUND )
            {
                iterator.remove( );
                continue;
            }
            lastRockType = currentRock.getType( );
        }
    }

    protected interface RockCoordinate
    {
        int get( Rock rock );

        void set( Rock rock, int coordinate );
    }

    protected interface RockCoordinateGetter
    {
        int get( Rock rock );
    }

    protected interface RockBuilder
    {

        Rock build( int row, int col );
    }

    protected static final RockCoordinate rowCoords = new RockCoordinate( )
    {
        @Override
        public int get( Rock rock )
        {
            return rock.getRow( );
        }

        @Override
        public void set( Rock rock, int coordinate )
        {
            rock.setRow( coordinate );
        }
    };

    protected static final RockCoordinate colCoords = new RockCoordinate( )
    {
        @Override
        public int get( Rock rock )
        {
            return rock.getCol( );
        }

        @Override
        public void set( Rock rock, int coordinate )
        {
            rock.setCol( coordinate );
        }
    };

    protected static void renumber( Iterator<Rock> iterator, RockCoordinate rockCoordinate )
    {
        int startingOffset = 0;
        while ( iterator.hasNext( ) )
        {
            Rock rock = iterator.next( );
            if ( rock.getType( ) == RockType.SQUARE )
            {
                startingOffset = rockCoordinate.get( rock ) + 1;
            }
            else if ( rock.getType( ) == RockType.ROUND )
            {
                rockCoordinate.set( rock, startingOffset++ );
            }
        }
    }

    protected static List<Rock> rebuild( Queue<Rock> queue, int max, RockCoordinateGetter dynamicCoord, RockBuilder rockBuilder )
    {
        List<Rock> rebuilt = new ArrayList<>( );

        Rock lastRock = queue.poll( );
        rebuilt.add( lastRock );

        while ( !queue.isEmpty( ) )
        {
            Rock currentRock = queue.poll( );

            if ( dynamicCoord.get( lastRock ) + 1 < dynamicCoord.get( currentRock ) )
            {
                do
                {
                    Rock rock = rockBuilder.build( lastRock.getRow( ), lastRock.getCol( ) );
                    rebuilt.add( rock );
                    lastRock = rock;
                }
                while ( dynamicCoord.get( lastRock ) + 1 < dynamicCoord.get( currentRock ) );
            }

            rebuilt.add( currentRock );
            lastRock = currentRock;
        }

        if ( dynamicCoord.get( lastRock ) < max - 1 )
        {
            do
            {
                Rock rock = rockBuilder.build( lastRock.getRow( ), lastRock.getCol( ) );
                rebuilt.add( rock );
                lastRock = rock;
            }
            while ( dynamicCoord.get( lastRock ) + 1 < max );
        }

        return rebuilt;
    }

    protected static Platform build( List<String> lines )
    {
        Int2ObjectMap<LinkedList<Rock>> rocksByRow = new Int2ObjectArrayMap<>( );
        Int2ObjectMap<LinkedList<Rock>> rocksByCol = new Int2ObjectArrayMap<>( );

        int nRows = lines.size( );
        int nCols = lines.get( 0 ).trim( ).length( );

        for ( int row = 0; row < nRows; row++ )
        {
            String line = lines.get( row ).trim( );
            for ( int col = 0; col < nCols; col++ )
            {
                char c = line.charAt( col );
                RockType rockType = RockType.parse( line.charAt( col ) );
                Rock rock = new Rock( row, col, rockType );

                rocksByRow.computeIfAbsent( row, key -> new LinkedList<>( ) ).add( rock );
                rocksByCol.computeIfAbsent( col, key -> new LinkedList<>( ) ).add( rock );
            }
        }

        return build( rocksByRow, rocksByCol, nRows, nCols );
    }

    protected static Platform build( Int2ObjectMap<LinkedList<Rock>> rocksByRow, Int2ObjectMap<LinkedList<Rock>> rocksByCol, int nRows, int nCols )
    {
        List<LinkedList<Rock>> rockListByRow = build( rocksByRow, nRows );
        List<LinkedList<Rock>> rockListByCol = build( rocksByCol, nCols );

        return new Platform( rockListByRow, rockListByCol );
    }

    protected static List<LinkedList<Rock>> build( Int2ObjectMap<LinkedList<Rock>> rockMap, int maxIndex )
    {
        List<LinkedList<Rock>> rockList = new ArrayList<>( );
        for ( int i = 0; i < maxIndex; i++ )
        {
            LinkedList<Rock> list = rockMap.getOrDefault( i, new LinkedList<>( ) );
            rockList.add( list );
        }
        return rockList;
    }

    protected static long computeLoad( List<String> lines )
    {
        Platform platform = Platform.build( lines );

        int nRows = platform.grid.length;
        int nCols = platform.grid[0].length;

        long sum = 0;
        for ( int col = 0; col < nCols; col++ )
        {
            LinkedList<Rock> column = new LinkedList( );
            for ( int row = 0; row < nRows; row++ )
            {
                column.add( platform.grid[row][col] );
            }

            Platform.tilt( column.descendingIterator( ) );
            Platform.renumber( column.iterator( ), rowCoords );
            sum += Platform.computeLoad( column, nRows );
            List<Rock> rebuilt = Platform.rebuild( column, nRows, ( rock ) -> rock.getRow( ), ( r, c ) -> new Rock( r + 1, c, RockType.EMPTY ) );

            for ( int row = 0; row < nRows; row++ )
            {
                platform.grid[row][col] = rebuilt.get( row );
            }
        }

        return sum;
    }

    protected static long computeLoadWithCycles( List<String> lines )
    {
        Platform platform = Platform.build( lines );

        int nRows = platform.grid.length;
        int nCols = platform.grid[0].length;

        List<RockType[][]> previousLayouts = new ArrayList<>( );
        boolean done = false;
        int cycle = 0;
        int startOfCycle = -1;
        do
        {
            cycle++;

            tiltNorth( platform );

            tiltWest( platform );

            tiltSouth( platform );

            tiltEast( platform );

            RockType[][] currentLayout = retrieveLayout( platform );

            startOfCycle = 0;
            for ( RockType[][] previousLayout : previousLayouts )
            {
                startOfCycle++;
                done = compareLayout( currentLayout, previousLayout );
                if ( done )
                {
                    break;
                }
            }
            if ( !done )
            {
                previousLayouts.add( currentLayout );
            }
        }
        while ( !done || cycle > 200 );

        if ( cycle > 200 )
        {
            throw new IllegalStateException( "Hmmm. Perhaps more cycles ... ?!" );
        }
        else
        {
            System.out.println( String.format( "Found a cycle after %d cycles starting at: %d (cycle length: %d )", cycle, startOfCycle, cycle - startOfCycle ) );
        }

        int moduloIndex = ( 1_000_000_000 - startOfCycle ) % ( cycle - startOfCycle );
        RockType[][] moduloGrid = previousLayouts.get( startOfCycle + moduloIndex - 1 );

        long sum = 0;
        for ( int col = 0; col < nCols; col++ )
        {
            for ( int row = 0; row < nRows; row++ )
            {
                RockType rockType = moduloGrid[row][col];
                if ( rockType == RockType.ROUND )
                {
                    sum += ( nRows - row );
                }
            }
        }

        return sum;
    }

    protected static void tiltNorth( Platform platform )
    {
        int nRows = platform.grid.length;
        int nCols = platform.grid[0].length;

        for ( int col = 0; col < nCols; col++ )
        {
            LinkedList<Rock> currentColumn = new LinkedList( );
            for ( int row = 0; row < nRows; row++ )
            {
                currentColumn.add( platform.grid[row][col] );
            }

            List<Rock> rebuilt = tiltNorth( currentColumn, nRows );

            for ( int row = 0; row < nRows; row++ )
            {
                platform.grid[row][col] = rebuilt.get( row );
            }
        }
    }

    protected static void tiltWest( Platform platform )
    {
        int nRows = platform.grid.length;
        int nCols = platform.grid[0].length;

        for ( int row = 0; row < nRows; row++ )
        {
            LinkedList<Rock> currentRow = new LinkedList( );
            for ( int col = 0; col < nCols; col++ )
            {
                currentRow.add( platform.grid[row][col] );
            }

            List<Rock> rebuilt = tiltWest( currentRow, nCols );

            for ( int col = 0; col < nCols; col++ )
            {
                platform.grid[row][col] = rebuilt.get( col );
            }
        }
    }

    protected static void tiltSouth( Platform platform )
    {
        int nRows = platform.grid.length;
        int nCols = platform.grid[0].length;

        for ( int col = 0; col < nCols; col++ )
        {
            LinkedList<Rock> currentColumn = new LinkedList( );
            int newRowNum = 0;
            for ( int row = nRows - 1; row > -1; row-- )
            {
                Rock rock = platform.grid[row][col];
                rock.setRow( newRowNum++ );
                currentColumn.add( rock );
            }

            List<Rock> rebuilt = tiltNorth( currentColumn, nRows );

            newRowNum = nRows - 1;
            for ( int row = 0; row < nRows; row++ )
            {
                Rock rock = rebuilt.get( row );
                rock.setRow( newRowNum );
                platform.grid[newRowNum][col] = rock;
                newRowNum--;
            }
        }
    }

    protected static void tiltEast( Platform platform )
    {
        int nRows = platform.grid.length;
        int nCols = platform.grid[0].length;

        for ( int row = 0; row < nRows; row++ )
        {
            LinkedList<Rock> currentRow = new LinkedList( );
            int newColNum = 0;
            for ( int col = nCols - 1; col > -1; col-- )
            {
                Rock rock = platform.grid[row][col];
                rock.setCol( newColNum++ );
                currentRow.add( rock );
            }

            List<Rock> rebuilt = tiltWest( currentRow, nCols );

            newColNum = nCols - 1;
            for ( int col = 0; col < nCols; col++ )
            {
                Rock rock = rebuilt.get( col );
                rock.setCol( newColNum );
                platform.grid[row][newColNum] = rock;
                newColNum--;
            }
        }
    }

    protected static List<Rock> tiltNorth( LinkedList<Rock> column, int nRows )
    {
        Platform.tilt( column.descendingIterator( ) );
        Platform.renumber( column.iterator( ), rowCoords );
        return Platform.rebuild( column, nRows, ( rock ) -> rock.getRow( ), ( r, c ) -> new Rock( r + 1, c, RockType.EMPTY ) );
    }

    protected static List<Rock> tiltWest( LinkedList<Rock> row, int nCols )
    {
        Platform.tilt( row.descendingIterator( ) );
        Platform.renumber( row.iterator( ), colCoords );
        return Platform.rebuild( row, nCols, ( rock ) -> rock.getCol( ), ( r, c ) -> new Rock( r, c + 1, RockType.EMPTY ) );
    }

    protected static long computeLoad( List<Rock> column, int nRows )
    {
        long sum = 0;

        for ( Rock rock : column )
        {
            if ( rock.getType( ) == RockType.ROUND )
            {
                sum += ( nRows - rock.getRow( ) );
            }
        }

        return sum;
    }

    protected static RockType[][] retrieveLayout( Platform platform )
    {
        int nRows = platform.grid.length;
        int nCols = platform.grid[0].length;

        RockType[][] layout = new RockType[nRows][nCols];
        for ( int row = 0; row < nRows; row++ )
        {
            for ( int col = 0; col < nCols; col++ )
            {
                layout[row][col] = platform.grid[row][col].getType( );
            }
        }
        return layout;
    }

    protected static boolean compareLayout( RockType[][] current, RockType[][] previous )
    {
        int nRows = current.length;
        int nCols = current[0].length;

        for ( int row = 0; row < nRows; row++ )
        {
            for ( int col = 0; col < nCols; col++ )
            {
                if ( current[row][col] != previous[row][col] )
                {
                    return false;
                }
            }
        }
        return true;
    }

    protected static void printPlatform( Platform platform )
    {
        int nRows = platform.grid.length;
        for ( int row = 0; row < nRows; row++ )
        {
            System.out.println( StringUtils.join( platform.grid[row], "" ) );
        }
    }
}
