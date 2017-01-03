import pl.edu.agh.kis.Logger;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class CrawlerWorker extends Thread
{
    Logger logger;
    DownloadQueue downloadQueue;
    VisitedPages visitedPages;
    WWWPageDownloader pageDownloader = new SocketDownload();
    AtomicInteger sleepingThreadsCounter;

    CrawlerWorker(Logger logger, DownloadQueue downloadQueue, VisitedPages visitedPages, AtomicInteger sleepingThreadsCounter)
    {
        this.sleepingThreadsCounter = sleepingThreadsCounter;
        this.logger = logger;
        this.downloadQueue = downloadQueue;
        this.visitedPages = visitedPages;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void run()
    {
        ArrayList<String> links;
        String pageContent;
        String nextLink;
        while(true)
        {
            if(downloadQueue.isEmpty())
            {
                logger.log(Logger.Level.INFO, this + " is sleeping with " + sleepingThreadsCounter.getAndIncrement() + " other threads");

                try
                {
                    Thread.sleep(1000);
                }
                catch(Exception ignored) {}

                if(sleepingThreadsCounter.get() == MultiThreadedCrawler.NUMBER_OF_THREADS)
                {
                    logger.log(Logger.Level.INFO, "Nothing to do, exiting from " + this);
                    break;
                }
                logger.log(Logger.Level.INFO, this + " woke up, " + sleepingThreadsCounter.decrementAndGet() + " other sleeping threads left");
            }
            nextLink = null;
            synchronized(downloadQueue)
            {
                while (!downloadQueue.isEmpty())
                {
                    nextLink = downloadQueue.getNextPage();
                    synchronized (visitedPages)
                    {
                        if (!visitedPages.pageAlreadyVisited(nextLink))
                        {
                            visitedPages.addVisitedPage(nextLink);
                            break;
                        }
                    }
                }
            }
            if(nextLink == null)
            {
                continue;
            }

            try
            {
                pageContent = pageDownloader.downloadPage(nextLink);
                logger.log(Logger.Level.INFO, "Visited at " + this + ": " + nextLink);
            }
            catch(DownloaderException e)
            {
                logger.log(Logger.Level.WARN, "Bad link: " + e.getUrl());
                continue;
            }

            links = URLoperations.appendRelativeUrls(URLoperations.getUrlsFromContent(pageContent), nextLink);
            for(String link : links)
            {
                downloadQueue.addPage(link);
            }
        }
    }
}
