package com.github.ttran17.day3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PartNumberManager
{
    protected static int consume( List<String> lines )
    {
        List<List<SchematicToken>> tokenLines = new ArrayList<>( );
        tokenLines.add( new ArrayList<>( ) ); // Padding
        for ( String line : lines )
        {
            tokenLines.add( Elf.consume( line ) );
        }
        tokenLines.add( new ArrayList<>( ) ); // Padding

        int sum = 0;
        for ( int lineNum = 1; lineNum <= tokenLines.size( ) - 2; lineNum++ )
        {
            List<SchematicToken> previousLine = tokenLines.get( lineNum - 1 );
            List<SchematicToken> currentLine = tokenLines.get( lineNum );
            List<SchematicToken> nextLine = tokenLines.get( lineNum + 1 );

            List<SchematicToken.Symbol> symbols = new ArrayList<>( );
            symbols.addAll( parse( previousLine, symbolPredicate, symbolMapper ) );
            symbols.addAll( parse( currentLine, symbolPredicate, symbolMapper ) );
            symbols.addAll( parse( nextLine, symbolPredicate, symbolMapper ) );

            List<SchematicToken.Number> numbers = parse( currentLine, numberPredicate, numberMapper );
            for ( SchematicToken.Number number : numbers )
            {
                for ( SchematicToken.Symbol symbol : symbols )
                {
                    if ( number.getIndexRange( ).isAdjacent( symbol.getIndexRange( ) ) )
                    {
                        sum += number.getValue( );
                    }
                }
            }
        }
        return sum;
    }

    protected static int consumeGearRatio( List<String> lines )
    {
        List<List<SchematicToken>> tokenLines = new ArrayList<>( );
        tokenLines.add( new ArrayList<>( ) ); // Padding
        for ( String line : lines )
        {
            tokenLines.add( Elf.consume( line ) );
        }
        tokenLines.add( new ArrayList<>( ) ); // Padding

        int sum = 0;
        for ( int lineNum = 1; lineNum <= tokenLines.size( ) - 2; lineNum++ )
        {
            List<SchematicToken> previousLine = tokenLines.get( lineNum - 1 );
            List<SchematicToken> currentLine = tokenLines.get( lineNum );
            List<SchematicToken> nextLine = tokenLines.get( lineNum + 1 );

            List<SchematicToken.Number> numbers = new ArrayList<>( );
            numbers.addAll( parse( previousLine, numberPredicate, numberMapper ) );
            numbers.addAll( parse( currentLine, numberPredicate, numberMapper ) );
            numbers.addAll( parse( nextLine, numberPredicate, numberMapper ) );

            List<SchematicToken.Symbol> symbols = parse( currentLine, symbolPredicate, symbolMapper );
            for ( SchematicToken.Symbol symbol : symbols )
            {
                List<SchematicToken.Number> adjacentNumbers = new ArrayList<>( );
                for ( SchematicToken.Number number : numbers )
                {
                    if ( symbol.getIndexRange( ).isAdjacent( number.getIndexRange( ) ) )
                    {
                        adjacentNumbers.add( number );
                    }
                }

                if ( adjacentNumbers.size( ) == 2 )
                {
                    int gearRatio = adjacentNumbers.stream( )
                                                   .mapToInt( n -> n.getValue( ) )
                                                   .reduce( ( i, j ) -> i * j ).orElseThrow( );
                    sum += gearRatio;
                }
            }
        }
        return sum;
    }

    protected static Predicate<SchematicToken> symbolPredicate = token -> token.isSymbol( );
    protected static Function<SchematicToken, SchematicToken.Symbol> symbolMapper = token -> ( SchematicToken.Symbol ) token;

    protected static Predicate<SchematicToken> numberPredicate = token -> !token.isSymbol( );
    protected static Function<SchematicToken, SchematicToken.Number> numberMapper = token -> ( SchematicToken.Number ) token;

    protected static <T, R> List<R> parse( List<T> tokens, Predicate<T> predicate, Function<? super T, ? extends R> mapper )
    {
        return tokens.stream( )
                     .filter( predicate )
                     .map( token -> mapper.apply( token ) )
                     .collect( Collectors.toList( ) );
    }
}
