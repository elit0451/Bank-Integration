package mysql;

import org.junit.runner.RunWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import bank.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnitPlatform.class)
public class T_E_S_T_BankMapper
{
    private static BankMapper bankMapper;

    private static String sqlStatements = "";
    
    @BeforeAll
    public static void beforeAll()
    {        
        BufferedReader sqlScript;
        bankMapper = new BankMapper();

        try
        {
            sqlScript = new BufferedReader(new InputStreamReader(new FileInputStream("SQL/bank_test.sql"), "UTF-8"));
            
            String sqlStatement;
            while ((sqlStatement = sqlScript.readLine()) != null)
            {
                sqlStatements += sqlStatement;
            }

            bankMapper.setDataSource(new DataSourceMysql().getDataSource());
            bankMapper.preparedStatement(sqlStatements).executeUpdate();
            bankMapper.closeConnection();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        bankMapper.setDataSource(new DataSourceMysql().getDataSource());
    }

    @Test
    public void testBankMapperAccount()
    {
        AccountInterface account = bankMapper.getAccount(1);
        assertThat(account.getBalance(), is(100000.00));

        account.setBalance(0.00);
        bankMapper.updateAccount(account);
        account = bankMapper.getAccount(1);
        assertThat(account.getBalance(), is(0.00));
    }
}