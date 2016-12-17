public class DownloaderException extends Exception
{
    private String url;
    public DownloaderException(String url)
    {
        this.url = url;
    }
    public String getUrl()
    {
        return url;
    }
}
