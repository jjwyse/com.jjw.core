package com.jjw.core.app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import org.apache.log4j.Logger;


public class PidManager
{
    /** Logger instance. */
    Logger LOG = Logger.getLogger(PidManager.class);

    /** Name of the file where we want to save our PID. */
    private String myFileName;
    
    /**
     * Constructor that takes in the name of the file where we want to save the PID.
     * 
     * @param fileName
     */
    public PidManager(String fileName)
    {
        myFileName = fileName;
    }
    
    /**
     * Writes our current PID to myFileName.
     * 
     * @throws IOException If there was an issue while closing the buffered writer.
     */
    public void writePid()
    {
        String mxBean = ManagementFactory.getRuntimeMXBean().getName();
        String pid = mxBean.split("@")[0];
        BufferedWriter bufferedWriter = null;
        
        try
        {
            bufferedWriter = new BufferedWriter(new FileWriter(myFileName));
            bufferedWriter.write(pid);
            LOG.info("Saving PID: " + pid + " to file: " + myFileName);
        }
        catch (IOException e)
        {
            LOG.error("Problem writing PID: " + pid + " to file: " + myFileName);
        }
        finally
        {
            try
            {
                bufferedWriter.close();
            }
            catch (IOException e)
            {
                // Ignore exception.
            }
        }
    }
}
