package com.bigdata.utility;

import java.math.BigDecimal;

import com.bigdata.coords.EastingNorthing;
import com.bigdata.coords.LatitudeLongitude;

public class Common
{
    public static String rm(String s)
    {
        return s.replace("\"", "");
    }
    
    public static String[] parseNGR(String s)
    {
        String[] a = s.split(" ");
        String[] r = new String[3];
        
        if (a.length == 3)
        {
            r = a;
        }
        else if (s.length() == 12)
        {
            r = new String[3];
            
            r[0] = s.substring(0, 2);
            r[1] = s.substring(2, 7);
            r[2] = s.substring(7, 12);            
        }
        else
            return null;
        
        if (r.length == 3 && r[0].matches("[A-Z]{2}") && r[1].matches("[0-9]{5}") && r[2].matches("[0-9]{5}"))
            return r;
        
        return null;
    }
    
    public static double[] enToLatLon(String e, String n)
    {
        EastingNorthing en = new EastingNorthing(new BigDecimal(e).floatValue(), new BigDecimal(n).floatValue());
        LatitudeLongitude l = en.toLatitudeLongitude();
        
        double[] r = new double[2];
        r[0] = l.getLat();
        r[1] = l.getLon();
        
        return r;
    }
    
    /**
     * Hectares to Square Meters
     * 
     * @param ha
     * @return
     */
    public static double haToM2(String ha)
    {
        return new BigDecimal(ha).doubleValue() * 10000.0;
    }
    
    /**
     * Returns a symbol of a 10km square
     * 
     * @param easting
     * @param northing
     * @return
     */
    public static String getSquareSymbol(int easting, int northing)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(Common.fillZeroes(easting).charAt(0));
        sb.append(Common.fillZeroes(northing).charAt(0));
        
        return sb.toString();
    }
    
    public static int getSquareSymbolInt(int easting, int northing)
    {
        return Integer.valueOf(Common.getSquareSymbol(easting, northing));
    }
    
    /**
     * Puts 0s before the number.
     * Eg. 832 -> 00832
     * 
     * @param value
     * @return
     */
    public static String fillZeroes(int value)
    {
        String s = String.valueOf(value);
        
        String r = "";
        
        for (int i = 0; i < 5 - s.length(); i++)
            r += "0";
        
        r += s;
        
        return r;
    }
    
    public static double shiftFromCorner(double base, int shift, int cellSize)
    {
        return base + shift * cellSize;
    }
    
    public static double distance(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
