import pl.edu.agh.kis.Log;
import pl.edu.agh.kis.Logger;
import java.util.ArrayList;

public class Crawler
{
    @SuppressWarnings("Duplicates")
    public static void main(String[] args)
    {
        final String startingUrl = "http://www.projektzphp.c0.pl/";

        Logger logger = new Log(System.out, Logger.Level.INFO);

        DownloadQueue downloadQueue = new RAMDownloadQueue();
        VisitedPages visitedPages = new RAMVisitedPages();
        //DownloadQueue downloadQueue = new H2DownloadQueue();
        //VisitedPages visitedPages = new H2VisitedPages();
        WWWPageDownloader pageDownloader = new SocketDownload();
        visitedPages.addVisitedPage("");

        String pageContent = "";
        try
        {
            pageContent = pageDownloader.downloadPage(startingUrl);
        }
        catch(DownloaderException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        ArrayList<String> links = URLoperations.appendRelativeUrls(URLoperations.getUrlsFromContent(pageContent), startingUrl);
        for(String link : links)
        {
            downloadQueue.addPage(link);
        }
        visitedPages.addVisitedPage(startingUrl);

        String nextLink;
        while(true)
        {
            nextLink = null;
            while(!downloadQueue.isEmpty())
            {
                nextLink = downloadQueue.getNextPage();
                if(!visitedPages.pageAlreadyVisited(nextLink)) break;
            }
            if(nextLink == null) break;

            logger.log(Logger.Level.INFO, "Visited: " + nextLink);

            try
            {
                pageContent = pageDownloader.downloadPage(nextLink);
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

            visitedPages.addVisitedPage(nextLink);
        }
    }
}