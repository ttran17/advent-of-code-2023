package com.github.ttran17.day25;

import java.util.List;

import org.jgrapht.alg.StoerWagnerMinimumCut;
import org.jgrapht.graph.DefaultEdge;

import com.google.ortools.Loader;
import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverSolutionCallback;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.LinearExpr;

public class Menger
{

    protected static long humbug( List<String> lines )
    {
        WiringDiagram wiringDiagram = new WiringDiagram( lines );
        StoerWagnerMinimumCut<WiringDiagram.Node, DefaultEdge> stoerWagnerMinimumCut = new StoerWagnerMinimumCut<>( wiringDiagram.graph );
        long v1 = stoerWagnerMinimumCut.minCut( ).size( );
        long v2 = wiringDiagram.graph.vertexSet( ).size( ) - v1;
        return v1 * v2;
    }

    protected static void main( String[] args )
    {
        Loader.loadNativeLibraries( );

        CpModel model = new CpModel( );

        IntVar k1 = model.newIntVar( 0, 3, "k1" );
        IntVar k2 = model.newIntVar( 0, 3, "k2" );
        IntVar k3 = model.newIntVar( 0, 3, "k3" );

        IntVar k4 = model.newIntVar( -3, 3, "k4" );
        IntVar k5 = model.newIntVar( -3, 3, "k5" );
        IntVar k6 = model.newIntVar( -3, 3, "k6" );
        IntVar k7 = model.newIntVar( -3, 3, "k7" );
        IntVar k8 = model.newIntVar( -3, 3, "k8" );

        BoolVar k9 = model.newBoolVar( "k9" );
        BoolVar k10 = model.newBoolVar( "k10" );

        IntVar ak4 = model.newIntVar( 0, 3, "ak4" );
        IntVar ak5 = model.newIntVar( 0, 3, "ak5" );
        IntVar ak6 = model.newIntVar( 0, 3, "ak6" );
        IntVar ak7 = model.newIntVar( 0, 3, "ak7" );
        IntVar ak8 = model.newIntVar( 0, 3, "ak8" );

        model.addAbsEquality( ak4, k4 );
        model.addAbsEquality( ak5, k5 );
        model.addAbsEquality( ak6, k6 );
        model.addAbsEquality( ak7, k7 );
        model.addAbsEquality( ak8, k8 );

        {
            IntVar[] vars = new IntVar[] { k1, k2, k3, ak4, ak5, ak6, ak7, ak8, k9 };
            int[] coeffs = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1 };
            model.addEquality( LinearExpr.newBuilder( ).addWeightedSum( vars, coeffs ).build( ), 6 );
        }

        {
            IntVar[] vars = new IntVar[] { k1, k2, k3, k4, k5, k6, k7, k8 };
            int[] coeffs = new int[] { 1, 2, 3, 4, 5, 6, 7, 8 };
            model.addEquality( LinearExpr.newBuilder( ).addWeightedSum( vars, coeffs ).build( ), -6 ).onlyEnforceIf( k10 );
            int[] coeffs2 = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };
            model.addEquality( LinearExpr.newBuilder( ).addWeightedSum( vars, coeffs2 ).build( ), 0 ).onlyEnforceIf( k10 );
            model.addLessOrEqual( k8, 0 ).onlyEnforceIf( k10 );
        }

        {
            IntVar[] vars = new IntVar[] { k1, k2, k3, k4, k5, k6, k7, k8, k9 };
            int[] coeffs = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
            model.addEquality( LinearExpr.newBuilder( ).addWeightedSum( vars, coeffs ).build( ), 12 ).onlyEnforceIf( k9 );
            int[] coeffs2 = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1 };
            model.addEquality( LinearExpr.newBuilder( ).addWeightedSum( vars, coeffs2 ).build( ), 0 ).onlyEnforceIf( k9 );
        }

        model.addEquality( LinearExpr.newBuilder( )
                                     .addTerm( k9, 1 )
                                     .addTerm( k10, 1 )
                                     .build( ), 1 );

        model.addLessOrEqual( LinearExpr.newBuilder( )
                                        .addTerm( k1, 1 )
                                        .addTerm( k2, 1 )
                                        .addTerm( k3, 1 )
                                        .build( ), 3 );

        CpSolver solver = new CpSolver( );
        solver.getParameters( ).setEnumerateAllSolutions( true );
        solver.solve( model, new CpSolverSolutionCallback( )
        {
            @Override
            public void onSolutionCallback( )
            {
                IntVar[] vars = new IntVar[] { k1, k2, k3, k4, k5, k6, k7, k8, k9, k10 };
                for ( IntVar var : vars )
                {
                    System.out.println( String.format( "%s: %d", var.getName( ), value( var ) ) );
                }
                System.out.println( );
            }
        } );
    }

}
