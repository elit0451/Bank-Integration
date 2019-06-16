package h2.embedded;

import org.junit.runner.RunWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.h2.tools.DeleteDbFiles;
import bank.*;

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
            sqlScript = new BufferedReader(new InputStreamReader(new FileInputStream("SQL/bank_h2.sql"), "UTF-8"));
            
            String sqlStatement;
            while ((sqlStatement = sqlScript.readLine()) != null)
            {
                sqlStatements += sqlStatement;
            }

            bankMapper.setDataSource(new DataSourceH2Embedded().getDataSource());
            bankMapper.preparedStatement(sqlStatements).executeUpdate();
            bankMapper.closeConnection();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        bankMapper.setDataSource(new DataSourceH2Embedded().getDataSource());
    }
    
    @AfterAll
    public static void afterAll()
    {
        DeleteDbFiles.execute("./", "bank", true);
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