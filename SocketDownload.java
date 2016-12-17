import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocketDownload implements WWWPageDownloader
{
    public String downloadPage(String url) throws DownloaderException
    {
        String[] urls = URLoperations.splitUrl(url);

        return downloadPage(urls);
    }

    public String downloadPage(String[] url) throws DownloaderException
    {
        Pattern p = Pattern.compile("^https.*", Pattern.UNIX_LINES);
        Matcher m = p.matcher(url[0] + url[1] + url[2] + url[3]);

        if(m.matches())
        {
            return downloadSSL(url);
        }
        else
        {
            return downloadInsecure(url);
        }
    }

    public String downloadInsecure(String[] url) throws DownloaderException
    {
        try
        {
            Socket s = new Socket(InetAddress.getByName(url[2]), 80);
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            pw.print("GET " + url[3] + " HTTP/1.1\r\n");
            pw.print("Host: www." + url[2] + "\r\n");
            pw.print("Connection: Close\r\n\r\n");
            pw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null)
            {
                sb.append(line);
                sb.append(" ");
            }
            br.close();
            pw.close();
            s.close();
            return sb.toString();
        }
        catch(UnknownHostException e)
        {
            throw new DownloaderException(url[0] + url[1] + url[2] + url[3]);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public String downloadSSL(String[] url) throws DownloaderException
    {

        try
        {
            SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket)factory.createSocket(url[2], 443);
            socket.startHandshake();

            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.print("GET " + url[3] + " HTTP/1.1\r\n");
            pw.print("Host: www." + url[2] + "\r\n");
            pw.print("Connection: Close\r\n\r\n");
            pw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null)
            {
                sb.append(line);
                sb.append(" ");
            }
            br.close();
            pw.close();
            socket.close();
            return sb.toString();
        }
        catch(Exception e)
        {
            throw new DownloaderException(url[0] + url[1] + url[2] + url[3]);
        }
    }
}
