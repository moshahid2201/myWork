package com.bigdata.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Logger;

import com.bigdata.utility.Common;
import com.bigdata.utility.Config;

public class OSData
{
    private final Logger log = Logger.getLogger(OSData.class.getName());
    
    String path;
    
    // Tile acronym in uppercase
    private String tileU;
    
    // Tile acronym in lowercase
    private String tileL;
    
    private OSDataSingle[] data;
    
    public OSData()
    {
        path = "/home/biadmin/Desktop/data/Squares";
    }
    
    private String generateFilePath(int i)
    {
        String ID;
        
        if (i < 10)
            ID = "0" + String.valueOf(i);
        else
            ID = String.valueOf(i);
        
        return path+ "/" + tileU + ID + ".asc";
    }
    
    public int getEastingModifier(int i)
    {
        return (int) Math.floor(i / 10);
    }
    
    public int getNorthingModifier(int i)
    {
        return i % 10;
    }
    
    public int count()
    {
        int c = 0;
        for (int i = 0; i < 100; i++)
        {
            if (data[i] != null)
                c++;
        }
        
        return c++;
    }
    
    public boolean isDataLoaded()
    {
        for (int i = 0; i < 100; i++)
        {
            if (data[i] != null)
                if ( ! data[i].hasData())
                    return false;
        }
        
        return true;
    }
    
    public void load(String tile)
    {
        data = new OSDataSingle[100];
        
        tileU = tile.toUpperCase();
        tileL = tile.toLowerCase();
        
        BufferedReader reader = null;
        
        for (int i = 0; i < 100; i++)
        {
            String line = null;
            int counter = 0;
            OSDataSingle s = new OSDataSingle();
        
            try
            {
                reader = new BufferedReader(new FileReader(generateFilePath(i)));
                
                while (true)
                {
//                    counter++;
               // 	System.out.println("progressing");
                    line = reader.readLine();
                    
                    // skip the first one
//                    if (counter == 5) continue;
                    
                    if (line != null && (line.isEmpty() || line.equals("") || line.equals("\n")))
                        continue;
                    
                    if (line == null)
                        break;
                    
                    String[] d = Common.rm(line).split(" ");
                    
                    if (d.length == 200)
                    {
                        s.addLine(d);
                    }
                    else
                    {
                        s.addMetadata(d);
                    }
                }
                
//                if (s.hasData() == false)
//                System.out.println(s.hasData());
                data[i] = s;
            }
            catch (FileNotFoundException e)
            {
//                log.info("OSFile " + generateFilePath(i) + " not found.");
                data[i] = null;
                
            }
            catch (Exception e)
            {
                log.severe("Error reading file " + generateFilePath(i));
                log.severe(line);
                e.printStackTrace();
            }
            
//            System.out.println(counter);
            
        }
    //    System.out.println("NOT NULL: " + count());
    }

    public OSDataSingle[] getData()
    {
        return data;
    }
    
    
}
