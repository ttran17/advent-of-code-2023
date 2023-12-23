package com.github.ttran17.day21;

import static com.github.ttran17.day21.InfiniteGarden.Quadrant.E;
import static com.github.ttran17.day21.InfiniteGarden.Quadrant.N;
import static com.github.ttran17.day21.InfiniteGarden.Quadrant.NE;
import static com.github.ttran17.day21.InfiniteGarden.Quadrant.NW;
import static com.github.ttran17.day21.InfiniteGarden.Quadrant.S;
import static com.github.ttran17.day21.InfiniteGarden.Quadrant.SE;
import static com.github.ttran17.day21.InfiniteGarden.Quadrant.SW;
import static com.github.ttran17.day21.InfiniteGarden.Quadrant.W;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.ttran17.day21.Garden.Gnome;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

public class InfiniteGarden
{
    protected enum Quadrant
    {
        N,
        NW,
        W,
        SW,
        S,
        SE,
        E,
        NE;
    }

    protected final Garden origin;

    protected final Map<Quadrant, Garden> evenGardenMap = new LinkedHashMap<>( );
    protected final Map<Quadrant, Garden> oddGardenMap = new LinkedHashMap<>( );

    protected final Map<OnTheFly, Garden> onTheFlyGardenMap = new LinkedHashMap<>( );

    protected final Map<Quadrant, Gnome> initialGnomeMap = new LinkedHashMap<>( );
    protected final Object2IntMap<Quadrant> maxStepCount = new Object2IntLinkedOpenHashMap<>( );

    protected InfiniteGarden( Garden origin, int maxSteps )
    {
        this.origin = origin;

        buildGardenMap( origin, evenGardenMap, maxSteps );
        buildGardenMap( origin, oddGardenMap, maxSteps - 1 );
    }

    protected void buildGardenMap( Garden origin, Map<Quadrant, Garden> gardenMap, int stepsRemaining )
    {
        int nRows = origin.nRows; // 131
        int nCols = origin.nCols; // 131

        int walkStartRow = origin.walkStartRow; // 65
        int walkStartCol = origin.walkStartCol; // 65

        initialGnomeMap.put( N, new Gnome( nRows - 1, walkStartCol, stepsRemaining ) );
        initialGnomeMap.put( NW, new Gnome( nRows - 1, nCols - 1, stepsRemaining ) );
        initialGnomeMap.put( W, new Gnome( walkStartRow, nCols - 1, stepsRemaining ) );
        initialGnomeMap.put( SW, new Gnome( 0, nCols - 1, stepsRemaining ) );
        initialGnomeMap.put( S, new Gnome( 0, walkStartCol, stepsRemaining ) );
        initialGnomeMap.put( SE, new Gnome( 0, 0, stepsRemaining ) );
        initialGnomeMap.put( E, new Gnome( walkStartRow, 0, stepsRemaining ) );
        initialGnomeMap.put( NE, new Gnome( nRows - 1, 0, stepsRemaining ) );

        for ( Quadrant quadrant : Quadrant.values( ) )
        {
            Gnome gnome = initialGnomeMap.get( quadrant );
            Garden garden = new Garden( origin, gnome.row( ), gnome.col( ), gnome.stepsRemaining( ) );
            garden.walk( );
            gardenMap.put( quadrant, garden );
            maxStepCount.put( quadrant, garden.maxStepCount( ) );
        }
    }

    protected long getExactVisitSize( Quadrant quadrant, int stepsRemaining )
    {
        int minStepsRequired = maxStepCount.getInt( quadrant );
        if ( stepsRemaining < minStepsRequired )
        {
            OnTheFly key = new OnTheFly( quadrant, stepsRemaining );

            return onTheFlyGardenMap.computeIfAbsent( key, k ->
            {
                Gnome gnome = initialGnomeMap.get( quadrant );
                Garden garden = new Garden( origin, gnome.row( ), gnome.col( ), stepsRemaining );
                garden.walk( );
                return garden;
            } ).exactVisits.size( );
        }

        boolean even = IS_EVEN.accept( stepsRemaining );
        return even ? evenGardenMap.get( quadrant ).exactVisits.size( ) : oddGardenMap.get( quadrant ).exactVisits.size( );
    }

    protected long walkCardinal( int totalSteps )
    {
        List<Integer> tailRemaining = new ArrayList<>( );
        {
            int stepsRemaining = ( totalSteps - ( 65 + 1 ) ) % 131;
            tailRemaining.add( stepsRemaining );
            while ( stepsRemaining + 131 < 260 )
            {
                stepsRemaining += 131;
                tailRemaining.add( stepsRemaining );
            }
        }

        int stepStart = totalSteps - ( 65 + 1 );
        int stepEnd = tailRemaining.get( tailRemaining.size( ) - 1 );

        long sum = 0;

        for ( int stepsRemaining = stepStart; stepsRemaining > stepEnd; stepsRemaining -= 131 )
        {
            sum += getExactVisitSize( N, stepsRemaining );
            sum += getExactVisitSize( W, stepsRemaining );
            sum += getExactVisitSize( S, stepsRemaining );
            sum += getExactVisitSize( E, stepsRemaining );
        }

        for ( int stepsRemaining : tailRemaining )
        {
            sum += getExactVisitSize( N, stepsRemaining );
            sum += getExactVisitSize( W, stepsRemaining );
            sum += getExactVisitSize( S, stepsRemaining );
            sum += getExactVisitSize( E, stepsRemaining );
        }

        // System.out.println("cardinal: " + sum);

        return sum;
    }

    protected long walkQuadrant( int totalSteps )
    {
        List<Integer> tailRemaining = new ArrayList<>( );
        {
            int stepsRemaining = ( totalSteps - ( 65 + 1 + 65 + 1 ) ) % 131;
            tailRemaining.add( stepsRemaining );
            while ( stepsRemaining + 131 < 260 )
            {
                stepsRemaining += 131;
                tailRemaining.add( stepsRemaining );
            }
        }

        long sum = 0;

        int nEven = 0;
        int nOdd = 0;
        {
            int stepStart = totalSteps - ( 65 + 1 + 65 + 1 );
            int stepEnd = tailRemaining.get( tailRemaining.size( ) - 1 );
            for ( int colStepsRemaining = stepStart; colStepsRemaining > stepEnd; colStepsRemaining -= 131 )
            {
                boolean even = IS_EVEN.accept( colStepsRemaining );
                if ( even )
                {
                    nEven++;
                }
                else
                {
                    nOdd++;
                }

                sum += getExactVisitSize( NW, colStepsRemaining );
                sum += getExactVisitSize( SW, colStepsRemaining );
                sum += getExactVisitSize( SE, colStepsRemaining );
                sum += getExactVisitSize( NE, colStepsRemaining );
            }

            for ( int stepsRemaining : tailRemaining )
            {
                sum += getExactVisitSize( NW, stepsRemaining );
                sum += getExactVisitSize( SW, stepsRemaining );
                sum += getExactVisitSize( SE, stepsRemaining );
                sum += getExactVisitSize( NE, stepsRemaining );
            }
        }

        int stepStart = totalSteps - ( 65 + 1 + 65 + 1 ) - 131; // -131 to account for column stepping above
        int stepEnd = tailRemaining.get( tailRemaining.size( ) - 1 );
        for ( int rowStepsRemaining = stepStart; rowStepsRemaining > stepEnd; rowStepsRemaining -= 131 )
        {
            boolean even = IS_EVEN.accept( rowStepsRemaining );
            if ( even )
            {
                nOdd--;

                sum += nEven * getExactVisitSize( NW, rowStepsRemaining );
                sum += nEven * getExactVisitSize( SW, rowStepsRemaining );
                sum += nEven * getExactVisitSize( SE, rowStepsRemaining );
                sum += nEven * getExactVisitSize( NE, rowStepsRemaining );

                sum += nOdd * getExactVisitSize( NW, rowStepsRemaining + 1 );
                sum += nOdd * getExactVisitSize( SW, rowStepsRemaining + 1 );
                sum += nOdd * getExactVisitSize( SE, rowStepsRemaining + 1 );
                sum += nOdd * getExactVisitSize( NE, rowStepsRemaining + 1 );
            }
            else
            {
                nEven--;

                sum += nEven * getExactVisitSize( NW, rowStepsRemaining + 1 );
                sum += nEven * getExactVisitSize( SW, rowStepsRemaining + 1 );
                sum += nEven * getExactVisitSize( SE, rowStepsRemaining + 1 );
                sum += nEven * getExactVisitSize( NE, rowStepsRemaining + 1 );

                sum += nOdd * getExactVisitSize( NW, rowStepsRemaining );
                sum += nOdd * getExactVisitSize( SW, rowStepsRemaining );
                sum += nOdd * getExactVisitSize( SE, rowStepsRemaining );
                sum += nOdd * getExactVisitSize( NE, rowStepsRemaining );
            }

            for ( int stepsRemaining : tailRemaining )
            {
                sum += getExactVisitSize( NW, stepsRemaining );
                sum += getExactVisitSize( SW, stepsRemaining );
                sum += getExactVisitSize( SE, stepsRemaining );
                sum += getExactVisitSize( NE, stepsRemaining );
            }
        }

        for ( int k = tailRemaining.size( ); k > 0; k-- )
        {
            for ( int stepsRemaining : tailRemaining.subList( 0, k ) )
            {
                sum += getExactVisitSize( NW, stepsRemaining );
                sum += getExactVisitSize( SW, stepsRemaining );
                sum += getExactVisitSize( SE, stepsRemaining );
                sum += getExactVisitSize( NE, stepsRemaining );
            }
        }

        // System.out.println("quadrant: " + sum);

        return sum;
    }

    protected long walk( int totalSteps )
    {
        long sum = 0;

        sum += origin.walk( totalSteps );
        sum += walkCardinal( totalSteps );
        sum += walkQuadrant( totalSteps );

        return sum;
    }

    protected record OnTheFly( Quadrant quadrant, int stepsRemaining ) { }

    protected interface ParityCheck
    {
        boolean accept( int stepsRemaining );
    }

    protected static final ParityCheck IS_EVEN = stepsRemaining -> stepsRemaining % 2 == 0;

    protected static long walk( List<String> lines, int totalSteps )
    {
        int maxSteps = 512; // Large enough to ensure walking gets to every non-rock garden tile

        Garden origin = new Garden( lines, maxSteps );

        InfiniteGarden infiniteGarden = new InfiniteGarden( origin, maxSteps );

        return infiniteGarden.walk( totalSteps );
    }
}
