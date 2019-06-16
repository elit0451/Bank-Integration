package h2.embedded;

import org.h2.jdbcx.JdbcDataSource;

public class DataSourceH2Embedded
{
    private JdbcDataSource dataSource = new JdbcDataSource();
    
    public DataSourceH2Embedded()
    {
        dataSource.setURL("jdbc:h2:./bank");
    }
    
    public JdbcDataSource getDataSource()
    {
        return dataSource;
    }
}