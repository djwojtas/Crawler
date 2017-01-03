import pl.edu.agh.kis.Log;
import pl.edu.agh.kis.Logger;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiThreadedCrawler
{
    static final int NUMBER_OF_THREADS = 10;

    private Logger logger = new Log(System.out, Logger.Level.INFO);
    private DownloadQueue downloadQueue = new RAMDownloadQueue();
    private VisitedPages visitedPages = new RAMVisitedPages();
    //DownloadQueue downloadQueue = new H2DownloadQueue();
    //VisitedPages visitedPages = new H2VisitedPages();
    private AtomicInteger sleepingThreadsCounter = new AtomicInteger(0);

    public static void main(String[] args)
    {
        MultiThreadedCrawler crawler = new MultiThreadedCrawler();

        ArrayList<CrawlerWorker> threads = new ArrayList<>();
        final String startingUrl = "http://www.xvideos.com/";
        crawler.downloadQueue.addPage(startingUrl);
        crawler.visitedPages.addVisitedPage("");

        for(int i=0; i<NUMBER_OF_THREADS; ++i)
        {
            threads.add(new CrawlerWorker(crawler.logger, crawler.downloadQueue, crawler.visitedPages, crawler.sleepingThreadsCounter));
            threads.get(i).start();
        }

        for(int i=0; i<NUMBER_OF_THREADS; ++i)
        {
            while(true)
            {
                try
                {
                    threads.get(i).join();
                    crawler.logger.log(Logger.Level.INFO, "Finished thread " + i);
                    break;
                }
                catch(Exception ignored){}
            }
        }


        crawler.logger.log(Logger.Level.INFO, "Finished all threads");
    }
}
