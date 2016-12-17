package pl.edu.agh.kis;

import java.io.FileOutputStream;
import java.util.Date;

public class LogTester
{
    public static void main(String[] args) throws Exception
    {
        System.out.println(new Date());

        Log logger = new Log(new FileOutputStream("logs.txt"), Logger.Level.DEBUG);

        logger.log(Logger.Level.TRACE, "test");
        logger.log(Logger.Level.DEBUG, "test2");
        logger.log(Logger.Level.INFO, "test3");

        Thread.sleep(500);

        logger.log(Logger.Level.WARN, "test4");
        logger.log(Logger.Level.ERROR, "test5");
        logger.log(Logger.Level.FATAL, "test6");
    }
}
