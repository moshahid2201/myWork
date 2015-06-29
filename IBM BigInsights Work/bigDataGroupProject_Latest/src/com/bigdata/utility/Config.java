package com.bigdata.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Singleton class that holds all configuration.
 */
public class Config
{
    private static Config instance = null;

    // logging settings
    private FileHandler logFile;
    private final Logger log = Logger.getLogger(Config.class.getName());
    private final String logFileExtension = ".log";
    private final String logDirectory = "log/";
    
    private final String configFilename = "config";
    private HashMap<String, String> settings = new HashMap<String, String>();

    public static Config getInstance()
    {
        if (instance == null)
        {
            synchronized (Config.class)
            {
                if (instance == null)
                {
                    instance = new Config();
                }
            }
        }

        return instance;
    }

    private Config()
    {
        try
        {
            // set up a log file
            logFile = new FileHandler(logDirectory + getLogFilename(), false);
            logFile.setFormatter(new SimpleFormatter());
            registerLogger(log);
            
            readConfigFile();
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    
    private void readConfigFile()
    {
        BufferedReader configFile = null; 
        try
        {
            configFile = new BufferedReader(new FileReader(configFilename));

            
            String line;
            while (true)
            {
                line = configFile.readLine();
                
                if (line != null && line.isEmpty())
                    continue;
                
                if (line == null)
                    break;
                
                // omit lines starting with '#'
                // because they're comments
                if ( ! line.startsWith("#"))
                {
                    // syntax is: 
                    // key=value
                    String[] pair = line.split("=");
                    settings.put(pair[0], pair[1]);
                }
            }
            configFile.close();

        } catch (FileNotFoundException e)
        {
            log.severe("Configuration file " + configFilename + " not found. Shutting down...");
            System.exit(1);
        }
        catch(Exception e)
        {
            log.severe("Error reading configuration file...");
        }
        
    }

    /*
     * Adds logger from another class to the same logfile.
     */
    public void registerLogger(Logger p_logger)
    {
        p_logger.addHandler(logFile);
    }

    /*
     * Generates a new log file filename (based on current date and time).
     */
    public String getLogFilename()
    {
        String dateString = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
        return dateString.toString() + logFileExtension;
    }

    public String getSetting(String p_key)
    {
        return settings.get(p_key);
    }

}
