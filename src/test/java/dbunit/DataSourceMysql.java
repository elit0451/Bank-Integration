package dbunit;

import com.mysql.cj.jdbc.MysqlDataSource;

public class DataSourceMysql
{
    private MysqlDataSource dataSource = new MysqlDataSource();
    
    public DataSourceMysql()
    {
        try
        {
            dataSource.setUser("root");
            dataSource.setPassword("123456");
            dataSource.setServerName("localhost");
            dataSource.setDatabaseName("bank_test");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public MysqlDataSource getDataSource()
    {
        return dataSource;
    }
}