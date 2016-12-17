/**
 * Interfejs do pobierania stron WWW z internetu.
 */
public interface WWWPageDownloader
{
    /**
     * Pobiera wskazana strone z internetu i zwraca jej zawartosc jako napis.
     * @param pageURL adres URL strony do pobrania
     * @return napis zawierajacy zawrtosc strony (w html'u)
     * @throws DownloaderException zglaszany w sytuacji
     *         gdy strona nie moze byc pobrana (np. nie ma strony o takim
     *         adresie, wystapil blad I/O podczas pobierania itp.)
     *
     */
    String downloadPage(String pageURL) throws DownloaderException;
}