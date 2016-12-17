import java.sql.*;

public class H2VisitedPages implements VisitedPages
{
    @Override
    public boolean pageAlreadyVisited(String pageURL)
    {
        int recordCount = 0;
        PreparedStatement stmt = null;
        try
        {
            Connection dbConnection = DatabaseConnection.getConnection();
            stmt = dbConnection.prepareStatement("select count(*) from visited where link='" + pageURL + "'");
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
        return recordCount != 0;
    }

    @Override
    public void addVisitedPage(String pageURL)
    {
        if(!pageAlreadyVisited(pageURL))
        {
            PreparedStatement stmt = null;
            try
            {
                Connection dbConnection = DatabaseConnection.getConnection();
                stmt = dbConnection.prepareStatement("INSERT INTO visited (link) values (?)");
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
    }

    public static void main(String[] args)
    {
        H2VisitedPages test = new H2VisitedPages();
        System.out.println(test.pageAlreadyVisited("testowa"));
        System.out.println(test.pageAlreadyVisited("testowa1"));
        System.out.println(test.pageAlreadyVisited("testow"));
        test.addVisitedPage("testowa1");
    }
}
