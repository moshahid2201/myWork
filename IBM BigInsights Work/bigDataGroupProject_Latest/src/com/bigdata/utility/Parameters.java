package com.bigdata.utility;

import java.text.ParseException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;


public class Parameters
{
    private Options options;
    private String[] args;
    
    private CommandLineParser parser;
    private CommandLine cmd;
    
    public Parameters(String[] p_args) throws ParseException, Exception
    {
        args = p_args;
        options = new Options();
        options.addOption("g", true, "Generate training data");
        options.addOption("t", true, "Generate test data");
        options.addOption("h", false, "Help");
        
        parser = new BasicParser();
        cmd = parser.parse(options, args);
    }
    
    public String generateTrainingData()
    {
        if( ! cmd.hasOption("g"))
            return null;
        else
            return cmd.getOptionValue("g");
    }
    
    public boolean help()
    {
        if(cmd.hasOption("h"))
            return true;
        
        return false;
    }
    
    public String generateTestData()
    {
        if( ! cmd.hasOption("t"))
            return null;
        else
            return cmd.getOptionValue("t");
    }
    

}
