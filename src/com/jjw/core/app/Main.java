package com.jjw.core.app;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Main
{
    /** Logger instance. */
    Logger LOG = Logger.getLogger(Main.class);

    /** How often we'll query the log4j configuration file for changes. */
    private static final long myLog4jTimer = 30;

    /** The application context. */
    private FileSystemXmlApplicationContext myApplicationContext;

    /** Manages our current PID. */
    private PidManager myPidManager;

    /** The ID for this java process. */
    private String myId;

    /** Constants. */
    private static final String ID = "ID";
    private static final String PID = "PidFile";

    /**
     * Main Constructor.
     * 
     * @param log4jFile
     *            The string path to the Log4J config file
     * @param applicationContext
     *            The string path to the application context.
     */
    private Main(String log4jFile, String applicationContext)
    {
        setupLogging(log4jFile);
        setupApplicationContext(applicationContext);
        setupPidFile();
    }

    /**
     * Sets up our Log4J logging configuration.
     * 
     * @param log4jFile
     *            The path to the Log4J config file.
     */
    private void setupLogging(String log4jFile)
    {
        if (log4jFile.endsWith(".xml"))
        {
            DOMConfigurator.configureAndWatch(log4jFile, myLog4jTimer);
        }
        else
        {
            PropertyConfigurator.configureAndWatch(log4jFile, myLog4jTimer);
        }
    }

    /**
     * Sets up our application context.
     * 
     * @param applicationContext
     *            The path to the application context file.
     */
    private void setupApplicationContext(String applicationContext)
    {
        myApplicationContext = new FileSystemXmlApplicationContext(applicationContext);

        myId = myApplicationContext.getBean(ID, String.class);

        LOG.info("Loading in application context for: " + myId);
    }

    /**
     * Sets up our PID file with our PID.
     */
    private void setupPidFile()
    {
        String pidFile = myApplicationContext.getBean(PID, String.class);

        if (pidFile == null)
        {
            LOG.warn("No PidFile bean found.  Not able to save PID.");
        }
        myPidManager = new PidManager(pidFile);
        myPidManager.writePid();
    }

    /**
     * Main entry point of the program.
     * 
     * @param args
     *            Expects a Log4J configuration file and an application context.
     */
    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            throw new RuntimeException("Usage: " + Main.class.getName() + " [Log4J_XML_Config] [Application_Context]");
        }

        new Main(args[0], args[1]);
    }
}
