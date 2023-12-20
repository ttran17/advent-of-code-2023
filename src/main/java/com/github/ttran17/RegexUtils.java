package com.github.ttran17;

public class RegexUtils
{
    public static String removeCurlyBraces( String line )
    {
        return line.replaceAll("[{}]","");
    }
}
