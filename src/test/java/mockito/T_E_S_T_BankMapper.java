package mockito;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.junit.runner.RunWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import bank.*;
import org.mockito.MockitoAnnotations;

@RunWith(JUnitPlatform.class)
public class T_E_S_T_BankMapper
{
    private static BankMapper bankMapper;
    
    @BeforeAll
    public static void beforeAll()
    {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);
        bankMapper = new BankMapper();

        try
        {
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeQuery(anyString())).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getDouble("balance")).thenReturn(100000.00);
            when(resultSet.getInt("id")).thenReturn(1);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        bankMapper.setDataSource(dataSource);
    }

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBankMapperAccount()
    {
        AccountInterface account = bankMapper.getAccount(1);

        assertThat(account.getId(), is(1));
        assertThat(account.getBalance(), is(100000.00));
    }
}