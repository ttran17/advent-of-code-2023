package com.github.ttran17.day7;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;
import com.github.ttran17.TestAdventUtils;

public class TestGambler
{
    @Test
    public void testCardPars( )
    {
        Assertions.assertThrows( IllegalArgumentException.class, ( ) -> Card.parse( '1' ) );
    }

    @Test
    public void testHandType( )
    {
        Assertions.assertEquals( Hand.Type.FIVE_OF_A_KIND, ( new Hand( "JJJJJ 99", cards -> Hand.resolveHandType( cards ) ) ).type );
        Assertions.assertEquals( Hand.Type.FOUR_OF_A_KIND, ( new Hand( "ATAAA 99", cards -> Hand.resolveHandType( cards ) ) ).type );
        Assertions.assertEquals( Hand.Type.FULL_HOUSE, ( new Hand( "22QQQ 99", cards -> Hand.resolveHandType( cards ) ) ).type );
        Assertions.assertEquals( Hand.Type.FULL_HOUSE, ( new Hand( "2Q2QQ 99", cards -> Hand.resolveHandType( cards ) ) ).type );
        Assertions.assertNotEquals( Hand.Type.THREE_OF_A_KIND, ( new Hand( "22QQQ 99", cards -> Hand.resolveHandType( cards ) ) ).type );
        Assertions.assertNotEquals( Hand.Type.ONE_PAIR, ( new Hand( "22QQQ 99", cards -> Hand.resolveHandType( cards ) ) ).type );
        Assertions.assertEquals( Hand.Type.THREE_OF_A_KIND, ( new Hand( "2Q4QQ 99", cards -> Hand.resolveHandType( cards ) ) ).type );
        Assertions.assertEquals( Hand.Type.TWO_PAIR, ( new Hand( "2Q23Q 99", cards -> Hand.resolveHandType( cards ) ) ).type );
        Assertions.assertNotEquals( Hand.Type.ONE_PAIR, ( new Hand( "22KQQ 99", cards -> Hand.resolveHandType( cards ) ) ).type );
        Assertions.assertEquals( Hand.Type.ONE_PAIR, ( new Hand( "22KQJ 99", cards -> Hand.resolveHandType( cards ) ) ).type );
        Assertions.assertEquals( Hand.Type.HIGH_CARD, ( new Hand( "25KQJ 99", cards -> Hand.resolveHandType( cards ) ) ).type );
    }

    @Test
    public void testHandRank( )
    {
        Hand fullHouse = new Hand( "2Q2QQ 9", cards -> Hand.resolveHandType( cards ) );
        Hand threeOfAKind = new Hand( "JKJ3J 111", cards -> Hand.resolveHandType( cards ) );
        Hand highCard = new Hand( "2JA93 12", cards -> Hand.resolveHandType( cards ) );

        List<Hand> rankedHands = Gambler.rankHands( new LinkedList<>( List.of( threeOfAKind, highCard, fullHouse ) ), ties -> Gambler.tieBreaker( ties ) );
        Assertions.assertEquals( fullHouse, rankedHands.get( 0 ) );
        Assertions.assertEquals( highCard, rankedHands.get( 2 ) );
    }

    @Test
    public void testOneStar( )
    {
        File testInputFile = TestAdventUtils.getTestInputFile( 7 );
        List<String> lines = AdventUtils.readLines( testInputFile );

        LinkedList<Hand> hands = Gambler.buildHands( lines, cards -> Hand.resolveHandType( cards ) );
        Hand hand3 = hands.get( 3 );
        Hand hand4 = hands.get( 4 );

        List<Hand> rankedHands = Gambler.rankHands( hands, ties -> Gambler.tieBreaker( ties ) );
        Collections.reverse( rankedHands );
        Assertions.assertEquals( hand4, rankedHands.get( 4 ) );
        Assertions.assertEquals( hand3, rankedHands.get( 1 ) );
    }

    @Test
    public void testSubmitOneStar( )
    {
        TestAdventUtils.testSubmit( 7, ( lines ) -> Gambler.score( lines ), 6440L );
    }

    @Test
    public void submitOneStar( )
    {
        AdventUtils.submit( 7, ( lines ) -> Gambler.score( lines ) );
    }

    @Test
    public void testBreakTiesJoker( )
    {
        List<String> lines = List.of( "JT234 1", "QT2J4 1", "QT2JJ 1", "2T2JJ 1", "3T2JJ 1" );

        LinkedList<Hand> hands = Gambler.buildHands( lines, cards -> Hand.resolveHandType( cards ) );
        Hand top = hands.get( 1 );
        Hand middle1 = hands.get( 2 );
        Hand middle2 = hands.get( 4 );
        Hand middle3 = hands.get( 3 );
        Hand low = hands.get( 0 );
        Gambler.tieBreakerWithJoker( hands );
        Assertions.assertEquals( top, hands.get( 0 ) );
        Assertions.assertEquals( middle1, hands.get( 1 ) );
        Assertions.assertEquals( middle2, hands.get( 2 ) );
        Assertions.assertEquals( middle3, hands.get( 3 ) );
        Assertions.assertEquals( low, hands.get( 4 ) );
    }

    @Test
    public void testBreakTiesJokerRedux( )
    {
        List<String> lines = List.of( "JJJJJ 1", "J8J88 1" );

        LinkedList<Hand> hands = Gambler.buildHands( lines, cards -> Hand.resolveHandType( cards ) );
        Hand top = hands.get( 1 );
        Hand low = hands.get( 0 );
        Gambler.tieBreakerWithJoker( hands );
        Assertions.assertEquals( top, hands.get( 0 ) );
        Assertions.assertEquals( low, hands.get( 1 ) );
    }

    @Test
    public void testResolveHandTypeWithJoker( )
    {
        Assertions.assertEquals( Hand.Type.ONE_PAIR, Hand.resolveHandTypeWithJoker( new Card[] { Card.A, Card._2, Card.J, Card.K, Card.T } ) );
        Assertions.assertEquals( Hand.Type.FIVE_OF_A_KIND, Hand.resolveHandTypeWithJoker( new Card[] { Card.A, Card.A, Card.J, Card.A, Card.A } ) );
        Assertions.assertEquals( Hand.Type.FOUR_OF_A_KIND, Hand.resolveHandTypeWithJoker( new Card[] { Card.A, Card.A, Card.J, Card.A, Card.T } ) );
        Assertions.assertEquals( Hand.Type.FULL_HOUSE, Hand.resolveHandTypeWithJoker( new Card[] { Card.A, Card.A, Card.J, Card.T, Card.T } ) );
        Assertions.assertEquals( Hand.Type.FULL_HOUSE, Hand.resolveHandTypeWithJoker( new Card[] { Card.A, Card.A, Card.J, Card.T, Card.T } ) );
        Assertions.assertEquals( Hand.Type.THREE_OF_A_KIND, Hand.resolveHandTypeWithJoker( new Card[] { Card.A, Card.A, Card.J, Card.K, Card.Q } ) );
        Assertions.assertEquals( Hand.Type.ONE_PAIR, Hand.resolveHandTypeWithJoker( new Card[] { Card.A, Card.J, Card._2, Card.K, Card.Q } ) );
    }

    @Test
    public void testSubmitTwoStar( )
    {
        TestAdventUtils.testSubmit( 7, ( lines ) -> Gambler.scoreWithJoker( lines ), 5905L );
    }

    @Test
    public void submitTwoStar( )
    {
        AdventUtils.submit( 7, ( lines ) -> Gambler.scoreWithJoker( lines ) );
    }
}
