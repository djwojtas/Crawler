package pl.edu.agh.kis;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Implementation of Logger able to print logs to any <b>OutputStream</b>.
 *
 * Format of log is [ty of log] [date] -- [log message]
 */
public class Log implements Logger
{
    /**
     * Determines what level of logs will be printed
     */
    private Level logRange;

    /**
     * Holds reference to PrintWriter that writes to output stream defined in constructor
     */
    private PrintWriter out;

    /**
     * Holds date format for logs
     */
    final String dateFormat = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * Constructor of Log
     *
     * @param out output stream
     * @param logRange level from which logs should be saved
     */
    public Log(OutputStream out, Level logRange)
    {
        try
        {
            this.out = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));
            this.logRange = logRange;
        }
        catch(Exception e)
        {
            System.out.println("Cannot init logging output");
            e.printStackTrace();
        }
    }

    /**
     * Creates date in requested format
     *
     * @return Date as String object
     */
    private String getDate()
    {
        return (new SimpleDateFormat(dateFormat)).format(new Date());
    }

    @Override
    public synchronized void trace(String lineToLog)
    {
        out.println("TRACE " + getDate() + " -- " + lineToLog);
        out.flush();
    }

    @Override
    public synchronized void debug(String lineToLog)
    {
        out.println("DEBUG " + getDate() + " -- " + lineToLog);
        out.flush();
    }

    @Override
    public synchronized void info(String lineToLog)
    {
        out.println("INFO " + getDate() + " -- " + lineToLog);
        out.flush();
    }

    @Override
    public synchronized void warn(String lineToLog)
    {
        out.println("WARN " + getDate() + " -- " + lineToLog);
        out.flush();
    }

    @Override
    public synchronized void error(String lineToLog)
    {
        out.println("ERROR " + getDate() + " -- " + lineToLog);
        out.flush();
    }

    @Override
    public synchronized void fatal(String lineToLog)
    {
        out.println("FATAL " + getDate() + " -- " + lineToLog);
        out.flush();
    }

    @Override
    public synchronized void log(Level lvl, String lineToLog)
    {
        if(logRange.compareTo(lvl) <= 0 && Level.TRACE.compareTo(lvl) == 0)
        {
            trace(lineToLog);
        }
        else if(logRange.compareTo(lvl) <= 0 && Level.DEBUG.compareTo(lvl) == 0)
        {
            debug(lineToLog);
        }
        else if(logRange.compareTo(lvl) <= 0 && Level.INFO.compareTo(lvl) == 0)
        {
            info(lineToLog);
        }
        else if(logRange.compareTo(lvl) <= 0 && Level.WARN.compareTo(lvl) == 0)
        {
            warn(lineToLog);
        }
        else if(logRange.compareTo(lvl) <= 0 && Level.ERROR.compareTo(lvl) == 0)
        {
            error(lineToLog);
        }
        else if(logRange.compareTo(lvl) <= 0 && Level.FATAL.compareTo(lvl) == 0)
        {
            fatal(lineToLog);
        }
    }
}