package com.github.ttran17.day24;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

public class Descartes
{
    protected static long intersect( List<String> lines, long min, long max )
    {
        List<P> positions = new ArrayList<>( );
        List<V> velocities = new ArrayList<>( );

        for ( String line : lines )
        {
            int splitter = line.indexOf( '@' );
            String[] pos = line.substring( 0, splitter ).trim( ).split( ", " );
            String[] vel = line.substring( splitter + 1 ).trim( ).split( ", " );

            positions.add( new P( Long.parseLong( pos[0].trim( ) ), Long.parseLong( pos[1].trim( ) ), Long.parseLong( pos[2].trim( ) ) ) );
            velocities.add( new V( Integer.parseInt( vel[0].trim( ) ), Integer.parseInt( vel[1].trim( ) ), Integer.parseInt( vel[2].trim( ) ) ) );
        }

        int nVectors = positions.size( );

        double a = min;
        double b = max;

        int inside = 0;
        IntList permissible = new IntArrayList( );
        for ( int k = 0; k < nVectors; k++ )
        {
            P p = positions.get( k );
            V v = velocities.get( k );

            if ( ( p.x < min && v.dx < 0 ) || ( p.x > max && v.dx > 0 ) )
            {
                continue;
            }
            if ( ( p.y < min && v.dy < 0 ) || ( p.y > max && v.dy > 0 ) )
            {
                continue;
            }

            if ( ( v.dx == 0 && ( p.x < min || p.x > max ) ) || ( v.dy == 0 && ( p.y < min || p.y > max ) ) )
            {
                continue;
            }

            double txa = ( a - p.x ) / v.dx;
            P left = new P( a, p.y + txa * v.dy, p.z + txa * v.dz );

            double txb = ( b - p.x ) / v.dx;
            P right = new P( b, p.y + txb * v.dy, p.z + txb * v.dz );

            double tya = ( a - p.y ) / v.dy;
            P bottom = new P( p.x + tya * v.dx, a, p.z + tya * v.dz );

            double tyb = ( b - p.y ) / v.dy;
            P top = new P( p.x + tyb * v.dx, b, p.z + tyb * v.dz );

            if ( ( txa >= 0 && a <= left.y && left.y <= b ) ||
                    ( txb >= 0 && a <= right.y && right.y <= b ) ||
                    ( tya >= 0 && a <= bottom.x && bottom.x <= b ) ||
                    ( tyb >= 0 && a <= top.x && top.x <= b ) )
            {
                permissible.add( k );

                if ( a <= p.x && p.x <= b && a <= p.y && p.y <= b )
                {
                    inside++;
                }
            }
        }

        System.out.println( String.format( "%d out of %d permissible of which %d are inside", permissible.size( ), nVectors, inside ) );

        TreeMap<P, P> lefts = new TreeMap<>( Comparator.comparingDouble( ( P p ) -> p.y ) );
        TreeMap<P, P> rights = new TreeMap<>( Comparator.comparingDouble( ( P p ) -> p.y ) );
        TreeMap<P, P> bottoms = new TreeMap<>( Comparator.comparingDouble( ( P p ) -> p.x ) );
        TreeMap<P, P> tops = new TreeMap<>( Comparator.comparingDouble( ( P p ) -> p.x ) );

        Map<P, P> stfel = new LinkedHashMap<>( );
        Map<P, P> sthgir = new LinkedHashMap<>( );
        Map<P, P> smottob = new LinkedHashMap<>( );
        Map<P, P> spot = new LinkedHashMap<>( );

        Map<P, S> segments = new LinkedHashMap<>( );

        for ( int k : permissible )
        {
            P p = positions.get( k );
            V v = velocities.get( k );

            double txa = ( a - p.x ) / v.dx;
            P left = new P( a, p.y + txa * v.dy, p.z + txa * v.dz );

            double txb = ( b - p.x ) / v.dx;
            P right = new P( b, p.y + txb * v.dy, p.z + txb * v.dz );

            double tya = ( a - p.y ) / v.dy;
            P bottom = new P( p.x + tya * v.dx, a, p.z + tya * v.dz );

            double tyb = ( b - p.y ) / v.dy;
            P top = new P( p.x + tyb * v.dx, b, p.z + tyb * v.dz );

            lefts.put( left, p );
            stfel.put( p, left );

            rights.put( right, p );
            sthgir.put( p, right );

            bottoms.put( bottom, p );
            smottob.put( p, bottom );

            tops.put( top, p );
            spot.put( p, top );

            if ( a <= p.x && p.x <= b && a <= p.y && p.y <= b )
            {
                P start = p;
                P end;
                if ( txa >= 0 && a <= left.y && left.y <= b )
                {
                    end = left;
                }
                else if ( txb >= 0 && a <= right.y && right.y <= b )
                {
                    end = right;
                }
                else if ( tya >= 0 && a <= bottom.x && bottom.x <= b )
                {
                    end = bottom;
                }
                else if ( tyb >= 0 && a <= top.x && top.x <= b )
                {
                    end = top;
                }
                else
                {
                    throw new IllegalStateException( "Unexpected geometry inside the test area!" );
                }
                segments.put( p, new S( start, end ) );
            }
            else
            {
                List<P> list = new ArrayList( List.of( left, bottom, top, right ) );

                list.sort( Comparator.comparingDouble( o -> o.x ) );
                P start = null;
                for ( P q : list )
                {
                    if ( ( a <= q.x && q.x <= b ) || ( a <= q.y && q.y <= b ) )
                    {
                        start = q;
                        break;
                    }
                }
                Collections.reverse( list );
                P end = null;
                for ( P q : list )
                {
                    if ( ( a <= q.x && q.x <= b ) || ( a <= q.y && q.y <= b ) )
                    {
                        end = q;
                        break;
                    }
                }

                segments.put( p, new S( start, end ) );
            }
        }

        int checks = 0;
        long intersections = 0;
        for ( int k : permissible )
        {
            P p = positions.get( k );

            P pLeft = stfel.get( p );
            P pRight = sthgir.get( p );

            Set<P> Q = new LinkedHashSet<>( lefts.tailMap( pLeft ).values( ) );
            Q.retainAll( rights.headMap( pRight ).values( ) );

            for ( P q : Q )
            {
                S ps = segments.get( p );
                S qs = segments.get( q );

                // Now check if segments ps and qs intersect
                checks++;
                P r = intersect( ps, qs );
                if ( r != null && a <= r.x && r.x <= b && a <= r.y && r.y <= b )
                {
                    intersections++;
                }
            }
        }
        System.out.println( String.format( "Number of intersections checked: %d vs brute-force %d", checks, nVectors * ( nVectors + 1 ) / 2 ) );

        return intersections;
    }

    protected static class P
    {
        private final double x;
        private final double y;
        private final double z;

        public P( double x, double y, double z )
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    protected record V( int dx, int dy, int dz ) { }

    protected record S( P start, P end ) { }

    protected static P intersect( S s1, S s2 )
    {
        return intersect( s1.start.x, s1.start.y, s1.end.x, s1.end.y, s2.start.x, s2.start.y, s2.end.x, s2.end.y );
    }

    protected static final double EPS = 1e-12;

    // https://paulbourke.net/geometry/pointlineplane/pdb.c
    protected static P intersect( double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4 )
    {
        double denom = ( y4 - y3 ) * ( x2 - x1 ) - ( x4 - x3 ) * ( y2 - y1 );

        // Are lines parallel
        if ( denom == 0 )
        {
            return null;
        }

        double numera = ( x4 - x3 ) * ( y1 - y3 ) - ( y4 - y3 ) * ( x1 - x3 );
        double numerb = ( x2 - x1 ) * ( y1 - y3 ) - ( y2 - y1 ) * ( x1 - x3 );

        // Are lines coincident?
        if ( Math.abs( numera ) < EPS && Math.abs( numerb ) < EPS && Math.abs( denom ) < EPS )
        {
            return ( new P( ( x1 + x2 ) / 2, ( y1 + y2 ) / 2, 0 ) );
        }

        // Is intersection along the segments
        double mua = numera / denom;
        double mub = numerb / denom;
        if ( mua < 0 || mua > 1 || mub < 0 || mub > 1 )
        {
            return null;
        }

        return new P( x1 + mua * ( x2 - x1 ), y1 + mua * ( y2 - y1 ), 0 );
    }
}
