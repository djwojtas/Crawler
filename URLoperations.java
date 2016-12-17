import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLoperations
{
    static String[] splitUrl(String url)
    {
        String[] result = new String[4];

        Pattern p = Pattern.compile("(https?://)?(www\\.)?([^/]+)(.+)?");
        Matcher m = p.matcher(url);
        m.find();

        result[0] = m.group(1) == null ? "http://" : (m.group(1).equals("https://") ? "https://" : "http://");
        result[1] = m.group(2) == null ? "" : "www.";
        result[2] = m.group(3);
        result[3] = m.group(4) == null ? "" : m.group(4);

        return result;
    }

    static ArrayList<String> getUrlsFromContent(String content)
    {
        ArrayList<String> links = new ArrayList<>();

        Pattern p = Pattern.compile("<[aA] [^>]*[hH][rR][eE][fF]=\"([^\"]+)\"");
        Matcher m = p.matcher(content);

        while(m.find())
        {
            links.add(m.group(1));
        }

        /*p = Pattern.compile(" Location: ([^ ]+) ");
        m = p.matcher(content);

        while(m.find())
        {
            links.add(m.group(1));
        }*/

        return links;
    }

    static ArrayList<String> appendRelativeUrls(ArrayList<String> links, String url)
    {
        String[] address = splitUrl(url);

        String link;
        String noGetLink;
        Pattern relativeToPage = Pattern.compile("^(?!http)[^/]([^\\.]+/?)*(\\..+)?");
        Pattern relativeToServer = Pattern.compile("^/([^\\.]+/?)*(\\..+)?");
        Pattern pointingToSelf;
        Matcher m;
        Matcher pointingToSelfMatcher;
        for(int i=0; i<links.size(); i++)
        {
            link = links.get(i).replaceAll("#[^&]*", "");
            noGetLink = link.replaceAll("\\?.*", "");

            if(link.equals(""))
            {
                links.set(i, link);
                continue;
            }

            m = relativeToServer.matcher(link);
            if(m.matches())
            {
                links.set(i, address[0] + address[1] + address[2] + link);
                continue;
            }

            m = relativeToPage.matcher(link);
            if(m.matches())
            {
                String baseAddress = address[0] + address[1] + address[2];
                pointingToSelf = Pattern.compile("/" + noGetLink, Pattern.LITERAL);
                pointingToSelfMatcher = pointingToSelf.matcher(baseAddress + address[3]);
                if(pointingToSelfMatcher.find())
                {
                    links.set(i, (baseAddress + address[3]).split(Pattern.quote(noGetLink))[0] + link);
                    continue;
                }

                links.set(i, baseAddress + (address[3].replaceAll("\\?.*", "") + "/" + link).replaceAll("//", "/"));
                continue;
            }

            links.set(i, link);
        }

        return links;
    }
}
