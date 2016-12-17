import java.sql.*;

public class H2DownloadQueue implements DownloadQueue
{
    @Override
    public void addPage(String pageURL)
    {
        PreparedStatement stmt = null;
        try
        {
            Connection dbConnection = DatabaseConnection.getConnection();
            stmt = dbConnection.prepareStatement("INSERT INTO queue (link) values (?)");
            stmt.setString(1, pageURL);
            stmt.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (stmt != null)
            {
                try
                {
                    stmt.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean isEmpty()
    {
        int recordCount = 0;
        PreparedStatement stmt = null;
        try
        {
            Connection dbConnection = DatabaseConnection.getConnection();
            stmt = dbConnection.prepareStatement("select count(*) from queue");
            ResultSet numOfRecords = stmt.executeQuery();

            numOfRecords.next();
            recordCount = numOfRecords.getInt("count(*)");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(stmt != null)
            {
                try
                {
                    stmt.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return recordCount == 0;
    }

    @Override
    public synchronized String getNextPage()
    {
        String nextPage = "";
        PreparedStatement stmt = null;
        try
        {
            Connection dbConnection = DatabaseConnection.getConnection();
            stmt = dbConnection.prepareStatement("select top 1 * from queue");
            ResultSet records = stmt.executeQuery();

            records.next();
            nextPage = records.getString("link");
            String drop = records.getString("id");

            Statement dropStmt = dbConnection.createStatement();
            dropStmt.executeUpdate("delete from queue where id=" + drop);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(stmt != null)
            {
                try
                {
                    stmt.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return nextPage;
    }

    public static void main(String[] args)
    {
        H2DownloadQueue test = new H2DownloadQueue();
        test.addPage("testowa");
        System.out.println(test.isEmpty());
        System.out.println(test.getNextPage());
    }
}
