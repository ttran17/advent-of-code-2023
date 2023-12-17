package com.github.ttran17.day16;

public class BadRobot
{

    private final Grid grid;

    private final int maxRow;
    private final int maxCol;

    protected boolean active;

    protected int row;
    protected int col;

    protected Direction direction;

    public BadRobot( Grid grid, int row, int col, Direction direction )
    {
        this.grid = grid;

        this.maxRow = grid.nRows;
        this.maxCol = grid.nCols;

        this.active = true;

        this.row = row;
        this.col = col;

        this.direction = direction;
    }

    protected void setDirection( char c )
    {
        if ( c == '-' && ( direction == Direction.NORTH || direction == Direction.SOUTH ) && grid.splitters[row][col] < 1 )
        {
            active = false;
            return;
        }

        if ( c == '|' && ( direction == Direction.WEST || direction == Direction.EAST ) && grid.splitters[row][col] < 1 )
        {
            active = false;
            return;
        }

        switch ( c )
        {
            case '.' ->
            {
            }
            case '/' ->
            {
                switch ( direction )
                {
                    case NORTH -> direction = Direction.EAST;
                    case WEST -> direction = Direction.SOUTH;
                    case SOUTH -> direction = Direction.WEST;
                    case EAST -> direction = Direction.NORTH;
                }
            }
            case '\\' ->
            {
                switch ( direction )
                {
                    case NORTH -> direction = Direction.WEST;
                    case WEST -> direction = Direction.NORTH;
                    case SOUTH -> direction = Direction.EAST;
                    case EAST -> direction = Direction.SOUTH;
                }
            }
            case '|' ->
            {
                switch ( direction )
                {
                    case NORTH, SOUTH ->
                    {
                    }
                    case WEST, EAST ->
                    {
                        direction = Direction.NORTH;

                        grid.addBadRobot( row, col, Direction.SOUTH );
                        grid.splitters[row][col] = 0;
                    }
                }
            }
            case '-' ->
            {
                switch ( direction )
                {
                    case WEST, EAST ->
                    {
                    }
                    case NORTH, SOUTH ->
                    {
                        direction = Direction.WEST;

                        grid.addBadRobot( row, col, Direction.EAST );
                        grid.splitters[row][col] = 0;
                    }
                }
            }
        }
    }

    protected void step( )
    {
        row += direction.dRow( );
        col += direction.dCol( );

        active = -1 < row && row < maxRow && -1 < col && col < maxCol;

        if ( active )
        {
            grid.energize[row][col] = 1;

            char c = grid.layout[row][col];
            setDirection( c );
        }
    }
}
