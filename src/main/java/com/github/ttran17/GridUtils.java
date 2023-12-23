package com.github.ttran17;

public class GridUtils
{

    public interface CellCoordsAccessor<T>
    {

        long getRow( T t );

        long getCol( T t );
    }

    public static <T> boolean isClosedLoop( T[] cells, CellCoordsAccessor<T> accessor )
    {
        T start = cells[0];
        T end = cells[cells.length - 1];

        return accessor.getRow( start ) == accessor.getRow( end ) && accessor.getCol( start ) == accessor.getCol( end );
    }

    /**
     * For a simple closed rectilinear loop of cells this method returns the area of the region bounded the cell coordinates.
     * <br><br>
     * For example, { [0,0], [1,0], [1,1], [0,1], [0,0] } would return an area of 1.
     * <br><br>
     * For the area of the region enclosed by the cells (including the cells themselves) use {@link #Area(Object[], CellCoordsAccessor)} instead.
     */
    public static <T> long GreensTheorem( T[] cells, CellCoordsAccessor<T> accessor )
    {
        int K = cells.length;

        T previousCell = cells[0];
        long y0 = accessor.getRow( previousCell );

        long sum = 0;
        for ( int k = 1; k < K; k++ )
        {
            T currentCell = cells[k];

            long x = accessor.getCol( currentCell );
            long y = accessor.getRow( currentCell );

            long dy = y - y0;

            sum += x * dy;

            y0 = y;
        }
        return Math.abs( sum );
    }

    /**
     * For a simple closed loop <strong>L</strong> of square cells, this method returns the area <strong>A</strong> of the region enclosed by the
     * square cells in <strong>L</strong> (including the area of the square cells in <strong>L</strong>).
     * <a href="https://en.wikipedia.org/wiki/Green%27s_theorem#:~:text=In%20vector%20calculus%2C%20Green%27s%20theorem,special%20case%20of%20Stokes%27%20theorem.">Green's Theorem</a>
     * gives the area of the region <em>enclosed by the cell coordinates</em> of <strong>L</strong>. To get the area of the region enclosed by the
     * <em>cells</em> in <strong>L</strong>, including the cells in <strong>L</strong>, we need to account for the area contributed by cells in <strong>L</strong>
     * that are not accounted for by Green's Theorem.
     * <br><br>
     * Let <strong>A</strong> be the area defined above. Let B be the boundary of <strong>A</strong>. A cell C in <strong>L</strong> is said to be an
     * <em>inside</em> corner if all sides of C are interior to B (except one vertex of course); otherwise, C is said to be an <em>outside</em> corner. Let
     * N be the number of outside corners. Because <strong>L</strong> is a simple closed loop of square cells, it follows that N-4 is the number of inside
     * corners. Each outside corner of a unit square in <strong>L</strong> contributes an area 3/4 not accounted for by Green's Theorem. Similarly, each inside corner
     * contributes an area of 1/4, and each remaining cell in <strong>L</strong> contributes an area of 1/2. Thus,
     * <pre>
     *     A = Green's Theorem + 3/4*N + 1/4*(N-4) + 1/2*( |L| - N - (N-4) )
     *       = Green's Theorem + 1/2*|L| + 1
     * </pre>
     *
     * From <a href="https://en.wikipedia.org/wiki/Pick%27s_theorem">Pick's Theorem</a> we know that there is a relationship between <strong>A</strong>
     * and <strong>L</strong>. Suitable interpretation of Pick's Theorem also leads to the formula used here, though one must be careful regarding -1 vs +1.
     *
     */
    public static <T> long Area( T[] cells, CellCoordsAccessor<T> accessor )
    {
        long L = 0;

        long x0 = accessor.getCol( cells[0] );
        long y0 = accessor.getRow( cells[0] );

        for (int k = 1; k < cells.length; k++)
        {
            long x = accessor.getCol( cells[k] );
            long y = accessor.getRow( cells[k] );

            L += Math.abs((x - x0) + ( y - y0 )); // assumes L is rectilinear (no diagonal steps)

            x0 = x;
            y0 = y;
        }

        return GreensTheorem( cells, accessor ) + L/2 + 1;
    }

}
