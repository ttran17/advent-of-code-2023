package com.github.ttran17.day5;

public enum GardenItem
{
    SEED( "seed" ),
    SOIL( "soil" ),
    FERTILIZER( "fertilizer" ),
    WATER( "water" ),
    LIGHT( "light" ),
    TEMPERATURE( "temperature" ),
    HUMIDITY( "humidity" ),
    LOCATION( "location" );

    private final String name;

    GardenItem( String name )
    {
        this.name = name;
    }

    @Override
    public String toString( )
    {
        return name;
    }

    public static GardenItem parse( String s )
    {
        for ( GardenItem type : GardenItem.values( ) )
        {
            if ( type.name.equalsIgnoreCase( s ) )
            {
                return type;
            }
        }

        throw new IllegalArgumentException( "Tried to parse unknown garden item type: " + s );
    }

}
