package com.bigdata.data;

public class OSDataSingle
{
    private double[][] data;
    private int iterator;
    
    private int cols;
    private int rows;
    private double xcorner;
    private double ycorner;
    private int cellsize;
    
    public OSDataSingle()
    {
        cols = -1;
        rows = -1;
        xcorner = -1;
        ycorner = -1;
        cellsize = -1;
        
//        System.out.println(data[0][0]);
    }
    
    private void init()
    {
        iterator = 0;
        data = new double[rows][cols];
        
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                data[i][j] = -100;
            }
        }
    }
    
    public void addLine(String[] line)
    {
//        System.out.println(line.length + ": " + iterator);
//        System.arraycopy(line, 0, data[iterator], 0, line.length);
        
        for (int i = 0; i < cols; i++)
        {
            data[iterator][i] = Double.parseDouble(line[i]);
        }
        
        iterator++;
    }
    
    public void addMetadata(String[] line)
    {
        if (line.length == 2)
        {
            if (line[0].equals("ncols"))
                cols = Integer.parseInt(line[1]);
                            
            if (line[0].equals("nrows"))
                rows = Integer.parseInt(line[1]);
            
            if (line[0].equals("xllcorner"))
                xcorner = Double.parseDouble(line[1]);
                           
            if (line[0].equals("yllcorner"))
                ycorner = Double.parseDouble(line[1]);
            
            if (line[0].equals("cellsize"))
                cellsize = Integer.parseInt(line[1]);
        }
        
        if (rows > -1 && cols > -1)
            init();
    }
    
    public boolean hasData()
    {
        for (int i = 0; i < 200; i++)
        {
            for (int j = 0; j < 200; j++)
            {
                if (data[i][j] <= -99)
                    return false;
            }
        }
        
        return true;
    }

    public double[][] getData()
    {
        return data;
    }

    public int getIterator()
    {
        return iterator;
    }

    public void setIterator(int iterator)
    {
        this.iterator = iterator;
    }

    public int getCols()
    {
        return cols;
    }

    public void setCols(int cols)
    {
        this.cols = cols;
    }

    public int getRows()
    {
        return rows;
    }

    public void setRows(int rows)
    {
        this.rows = rows;
    }

    public double getXcorner()
    {
        return xcorner;
    }

    public void setXcorner(double xcorner)
    {
        this.xcorner = xcorner;
    }

    public double getYcorner()
    {
        return ycorner;
    }

    public void setYcorner(double ycorner)
    {
        this.ycorner = ycorner;
    }

    public int getCellsize()
    {
        return cellsize;
    }

    public void setCellsize(int cellsize)
    {
        this.cellsize = cellsize;
    }
    
    
}
