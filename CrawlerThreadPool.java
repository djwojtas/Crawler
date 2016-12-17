import pl.edu.agh.kis.Log;
import pl.edu.agh.kis.Logger;

import java.util.ArrayList;

public class CrawlerThreadPool
{
    static final int NUMBER_OF_THREADS = 10;

    Logger logger = new Log(System.out, Logger.Level.INFO);
    DownloadQueue downloadQueue = new RAMDownloadQueue();
    VisitedPages visitedPages = new RAMVisitedPages();
    //DownloadQueue downloadQueue = new H2DownloadQueue();
    //VisitedPages visitedPages = new H2VisitedPages();

    public static void main(String[] args)
    {
        CrawlerThreadPool crawler = new CrawlerThreadPool();

        ArrayList<CrawlerWorker> threads = new ArrayList<>();
        final String startingUrl = "http://www.wykop.pl/";

        WWWPageDownloader pageDownloader = new SocketDownload();
        crawler.visitedPages.addVisitedPage("");


        String pageContent = "";
        try
        {
            pageContent = pageDownloader.downloadPage(startingUrl);
            crawler.logger.log(Logger.Level.INFO, "Visited as start: " + startingUrl);
        }
        catch(DownloaderException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        ArrayList<String> links = URLoperations.appendRelativeUrls(URLoperations.getUrlsFromContent(pageContent), startingUrl);
        for(String link : links)
        {
            crawler.downloadQueue.addPage(link);
        }
        crawler.visitedPages.addVisitedPage(startingUrl);

        for(int i=0; i<NUMBER_OF_THREADS; ++i)
        {
            threads.add(new CrawlerWorker(crawler.logger, crawler.downloadQueue, crawler.visitedPages));
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
