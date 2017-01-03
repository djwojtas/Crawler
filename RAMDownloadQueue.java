import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RAMDownloadQueue implements DownloadQueue
{
    private List<String> queue = Collections.synchronizedList(new ArrayList<String>());
    private int index = 0;

    @Override
    public void addPage(String pageURL)
    {
        queue.add(pageURL);
    }

    @Override
    public boolean isEmpty()
    {
        return queue.size()<=index;
    }

    @Override
    public synchronized String getNextPage()
    {
        if(!isEmpty())
        {
            return queue.get(index++);
        }
        else
        {
            return null;
        }
    }
}
