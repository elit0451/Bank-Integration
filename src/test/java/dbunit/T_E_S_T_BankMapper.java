package dbunit;

import org.dbunit.database.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.junit.platform.runner.JUnitPlatform;
import bank.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnitPlatform.class)
public class T_E_S_T_BankMapper
{
    private static BankMapper bankMapper;

    private static Connection connection;
    private static IDatabaseConnection dbConnection;
    private static IDataSet xmlDataSet;
    private static QueryDataSet databaseDataSet;
    private static ITable xmlTable, databaseTable;
    
    @BeforeEach
    public void beforeEach()
    {
        bankMapper = new BankMapper();
        try
        {
            bankMapper.setDataSource(new DataSourceMysql().getDataSource());
            connection = bankMapper.getConnection();
            dbConnection = new DatabaseConnection(connection, "bank_test");
            dbConnection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
            dbConnection.getConfig().setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());
        
            xmlDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("DATASETS/account.xml"));
            
            DatabaseOperation.CLEAN_INSERT.execute(dbConnection, xmlDataSet);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    
    @AfterEach
    public void afterEach()
    {
        try
        {
            dbConnection.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        bankMapper.closeConnection();
    }

    @Test
    public void testBankMapperAccounts()
    {
        AccountInterface account = bankMapper.getAccount(1);
        assertThat(account.getBalance(), is(100000.00));

        account.setBalance(0.00);
        bankMapper.updateAccount(account);
        account = bankMapper.getAccount(1);
        assertThat(account.getBalance(), is(0.00));
    }

    @Test
    public void testBankMapperGetAllAccounts() throws Exception
    {
        List<AccountInterface> accounts = bankMapper.getAccounts();
        
        assertThat(accounts.size(), is(2));
        assertThat(accounts.get(0).getBalance(), is(100000.00));
        assertThat(accounts.get(1).getBalance(), is(0.00));
    }
    @Test
    public void testBankMapperCreateCreditCard() throws Exception
    {
        AccountInterface account = bankMapper.getAccount(2);
        CreditCardInterface creditCard = new CreditCard();

        creditCard.setPinCode(5678);
        creditCard.setAccount(account);
        bankMapper.createCreditCard(creditCard);

        databaseDataSet = new QueryDataSet(dbConnection);
        databaseDataSet.addTable("creditCard");
        databaseTable = databaseDataSet.getTable("creditCard");

        assertThat(Integer.parseInt(databaseTable.getValue(2, "pin_code").toString()), is(creditCard.getPinCode()));
    }


    @Test
    public void testBankMapperCreateCreditCardXML() throws Exception
    {
        AccountInterface account = bankMapper.getAccount(2);
        CreditCardInterface creditCard = new CreditCard();

        creditCard.setPinCode(5678);
        creditCard.setAccount(account);
        bankMapper.createCreditCard(creditCard);

        databaseDataSet = new QueryDataSet(dbConnection);
        databaseDataSet.addTable("creditCard");
        databaseTable = databaseDataSet.getTable("creditCard");

        xmlDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("DATASETS/creditCard.xml"));
        xmlTable = xmlDataSet.getTable("creditCard");

        for(int row = 0; row < xmlTable.getRowCount(); row++)
        {
            String column = "pin_code";

            assertThat(databaseTable.getValue(row, column).toString(), is(xmlTable.getValue(row, column).toString()));

            column = "account_id";

            assertThat(databaseTable.getValue(row, column).toString(), is(xmlTable.getValue(row, column).toString()));
        }
    }
}