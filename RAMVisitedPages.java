import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class RAMVisitedPages implements VisitedPages
{
    TreeSet<String> set = new TreeSet<>();
    Set<String> visited = Collections.synchronizedSet(set);

    @Override
    public boolean pageAlreadyVisited(String pageURL)
    {
        return visited.contains(pageURL);
    }

    @Override
    public void addVisitedPage(String pageURL)
    {
        visited.add(pageURL);
    }
}
