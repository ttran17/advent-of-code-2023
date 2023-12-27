package com.github.ttran17.day25;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.ttran17.AdventUtils;

public class TestMenger
{
    @Test
    public void testStats( )
    {
        File inputFile = AdventUtils.getInputFile( 25 );
        List<String> lines = AdventUtils.readLines( inputFile );

        WiringDiagram wiringDiagram = new WiringDiagram( lines );
        wiringDiagram.stats( );
    }

    @Test
    public void submit( )
    {
        AdventUtils.submit( 25, ( lines ) -> Menger.humbug( lines ) );
    }
}
