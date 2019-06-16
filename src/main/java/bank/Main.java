package bank;

import org.h2.jdbcx.JdbcDataSource;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        /*MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("123456");
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName("bank");*/

        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:bank;DB_CLOSE_DELAY=-1");

        BankMapperInterface mapper = new BankMapper();
        mapper.setDataSource(dataSource);

        BufferedReader sqlScript;
        String sqlStatements = "";
        try
        {
            sqlScript = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/elitsa/Downloads/Bank_h2.sql"), "UTF-8"));

            String sqlStatement;
            while ((sqlStatement = sqlScript.readLine()) != null)
            {
                sqlStatements += sqlStatement;
            }

            mapper.preparedStatement(sqlStatements).executeUpdate();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


        AccountInterface account = new Account();
        CreditCardInterface creditCard = new CreditCard();

        try{
            account = mapper.createAccount(account);
            System.out.println(account.getId());
            creditCard.setAccount(account);

            creditCard = mapper.createCreditCard(creditCard);
            System.out.println("CC - " + creditCard.getId());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
