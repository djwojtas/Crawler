import java.net.URL;

/**
 * Kolejka FIFO zawierajaca adresy stron, ktore maja zostac pobrane (odwiedzone).
 */
public interface DownloadQueue {
    /**
     * Dodaje adres strony do odwiedzenia na koniec kolejki
     *
     * @param pageURL adres strony do odwiedzenia
     */
    void addPage(String pageURL);

    /**
     * Zwraca informacje czy kolejka jest pusta, czy nie
     * @return true - kolejka pusta, false - w przeciwnym razie
     */
    boolean isEmpty();

    /**
     * Zwraca adres pierwszej strony w kolejce, ktora ma zostac odwiedzona i
     * usuwa ja z kolejki.
     *
     * @return adres URL strony do odwiedzenia
     */
    String getNextPage();
}