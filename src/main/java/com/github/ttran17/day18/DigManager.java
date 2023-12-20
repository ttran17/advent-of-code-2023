package com.github.ttran17.day18;

import java.util.List;

import com.github.ttran17.GridUtils;

public class DigManager
{

    protected static long excavate( List<String> lines )
    {
        BadRobot badRobot = new BadRobot( 0, 0 );

        for ( String line : lines )
        {
            badRobot.step( line );
        }

        return GridUtils.Area( badRobot.getVisitedArray( ), accessor );
    }

    protected static long excavateActual( List<String> lines )
    {
        BadRobot badRobot = new BadRobot( 0, 0 );

        for ( String line : lines )
        {
            badRobot.stepActual( line );
        }

        return GridUtils.Area( badRobot.getVisitedArray( ), accessor );
    }

    protected static GridUtils.CellCoordsAccessor<BadRobot.Position> accessor = new GridUtils.CellCoordsAccessor<>( )
    {
        @Override
        public long getRow( BadRobot.Position position )
        {
            return position.row( );
        }

        @Override
        public long getCol( BadRobot.Position position )
        {
            return position.col( );
        }
    };
}
