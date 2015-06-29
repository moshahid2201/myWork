package com.bigdata.analysis;

public class EastingNorthingPoint
{
    private int easting;
    private int northing;
    private int e;
    private int n;
    private double lat;
    private double lon;
    
    public EastingNorthingPoint(int easting, int northing, int e, int n, double lat, double lon)
    {
        this.easting = easting;
        this.northing = northing;
        this.e = e;
        this.n = n;
        this.lat = lat;
        this.lon = lon;
    }
    
    public int getEasting()
    {
        return easting;
    }
    public void setEasting(int easting)
    {
        this.easting = easting;
    }
    public int getNorthing()
    {
        return northing;
    }
    public void setNorthing(int northing)
    {
        this.northing = northing;
    }
    public int getE()
    {
        return e;
    }
    public void setE(int e)
    {
        this.e = e;
    }
    public int getN()
    {
        return n;
    }
    public void setN(int n)
    {
        this.n = n;
    }
    public double getLat()
    {
        return lat;
    }
    public void setLat(double lat)
    {
        this.lat = lat;
    }
    public double getLon()
    {
        return lon;
    }
    public void setLon(double lon)
    {
        this.lon = lon;
    }
    
    


    
    
}