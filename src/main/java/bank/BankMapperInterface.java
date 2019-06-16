package bank;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public interface BankMapperInterface {
    public void setDataSource(DataSource ds);
    public CreditCardInterface createCreditCard(CreditCardInterface creditCard);
    public CreditCardInterface updateCreditCard(CreditCardInterface creditCard);
    public CreditCardInterface getCreditCard(int id);
    public List<CreditCardInterface> getCreditCards();
    public AccountInterface createAccount(AccountInterface account);
    public void updateAccount(AccountInterface account);
    public AccountInterface getAccount(int id);
    public List<AccountInterface> getAccounts();
    public PreparedStatement preparedStatement(String sql);
    public Connection getConnection();
    public void closeConnection();
}

