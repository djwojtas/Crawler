import pl.edu.agh.kis.Logger;

import java.util.ArrayList;

public class CrawlerWorker extends Thread
{
    Logger logger;
    DownloadQueue downloadQueue;
    VisitedPages visitedPages;
    WWWPageDownloader pageDownloader = new SocketDownload();

    CrawlerWorker(Logger logger, DownloadQueue downloadQueue, VisitedPages visitedPages)
    {
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
