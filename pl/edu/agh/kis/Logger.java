package pl.edu.agh.kis;

/**
 * Describes a Logger interface
 *
 * Represents functions for different levels of alert along with function that accepts logging requests
 */
public interface Logger
{
    /**
     * Represents different levels of logs, higher number for higher priority
     */
    enum Level
    {
        TRACE, DEBUG, INFO, WARN, ERROR, FATAL
    }

    /**
     * Useful for tracking down part of functions
     *
     * @param lineToLog message to be logged
     */
    void trace(String lineToLog);

    /**
     * Useful for debugging code
     *
     * @param lineToLog message to be logged
     */
    void debug(String lineToLog);

    /**
     * Useful log information
     *
     * @param lineToLog message to be logged
     */
    void info(String lineToLog);

    /**
     * Oddities that can handle themselves but should be monitored
     *
     * @param lineToLog message to be logged
     */
    void warn(String lineToLog);

    /**
     * Errors of operations requiring intervention
     *
     * @param lineToLog message to be logged
     */
    void error(String lineToLog);

    /**
     * Errors causing shutdown of application
     *
     * @param lineToLog message to be logged
     */
    void fatal(String lineToLog);

    /**
     * Logs message with given level
     *
     * @param lvl level of message to log
     * @param lineToLog message to be logged
     */
    void log(Level lvl, String lineToLog);
}