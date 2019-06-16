package bank;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BankMapper implements BankMapperInterface{
    DataSource _data;
    AccountInterface _account;
    CreditCardInterface _creditCard;
    Connection _connection;

    public void setDataSource(DataSource dataSource) {
        _data = dataSource;

        try {
            _connection = _data.getConnection();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public CreditCardInterface createCreditCard(CreditCardInterface creditCard) {
        _creditCard = creditCard;
        _account = _creditCard.getAccount();

        PreparedStatement statement;

        try {
            if(!_connection.isClosed()) {
                String query = "INSERT INTO creditCard (account_id, last_used, pin_code, wrong_pin_code_attempts, blocked) VALUES (?, current_date(), ?, 0, 0);";
                statement = _connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1,_account.getId());
                statement.setInt(2, _creditCard.getPinCode());

                statement.executeUpdate();

                ResultSet result = statement.getGeneratedKeys();
                if(result.next())
                    _creditCard.setId(result.getInt(1));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return _creditCard;
    }

    public CreditCardInterface updateCreditCard(CreditCardInterface creditCard) {
        _creditCard = creditCard;
        _account = _creditCard.getAccount();

        PreparedStatement statement;

        try {
            if(!_connection.isClosed()) {
                String query = "UPDATE creditCard SET last_used = ?, wrong_pin_code_attempts = ?, blocked = ? WHERE id = " + creditCard.getId();
                statement = _connection.prepareStatement(query);
                statement.setDate(1,new java.sql.Date(creditCard.getLastUsed().getTime()));
                statement.setInt(2, creditCard.getWrongPinCodeAttempts());
                statement.setBoolean(3, creditCard.isBlocked());

                statement.executeUpdate();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return _creditCard;
    }

    public CreditCardInterface getCreditCard(int id) {
        Statement statement;
        ResultSet result;

        try {
            if(!_connection.isClosed()) {
                statement = _connection.createStatement();
                result = statement.executeQuery("SELECT * FROM creditCard, bankAccount WHERE creditCard.id = " + id + " AND creditCard.account_id = bankAccount.id LIMIT 1;");


                if(result.next()){
                    int account_id = result.getInt("account_id");
                    double balance = result.getDouble("balance");

                    int creditCard_id = result.getInt(1);
                    Date last_used = result.getDate("last_used");
                    int pin_code = result.getInt("pin_code");
                    int wrong_pin_code_attempts = result.getInt("wrong_pin_code_attempts");
                    boolean blocked = result.getBoolean("blocked");

                    AccountInterface account = new Account();
                    account.setId(account_id);
                    account.setBalance(balance);

                    CreditCardInterface creditCard = new CreditCard();
                    creditCard.setId(creditCard_id);
                    creditCard.setAccount(account);
                    creditCard.setLastUsed(last_used);
                    creditCard.setPinCode(pin_code);
                    creditCard.setWrongPinCodeAttempts(wrong_pin_code_attempts);
                    creditCard.setBlocked(blocked);

                    _creditCard = creditCard;
                }
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return _creditCard;
    }

    public List<CreditCardInterface> getCreditCards() {
        List<CreditCardInterface> cards = new ArrayList<CreditCardInterface>();
        Statement statement;
        ResultSet result;

        try {
            if(!_connection.isClosed()) {
                statement = _connection.createStatement();
                result = statement.executeQuery("SELECT * FROM creditCard, bankAccount WHERE creditCard.account_id = bankAccount.id;");


                while(result.next()){
                    int account_id = result.getInt("account_id");
                    double balance = result.getDouble("balance");

                    int creditCard_id = result.getInt(1);
                    Date last_used = result.getDate("last_used");
                    int pin_code = result.getInt("pin_code");
                    int wrong_pin_code_attempts = result.getInt("wrong_pin_code_attempts");
                    boolean blocked = result.getBoolean("blocked");

                    AccountInterface account = new Account();
                    account.setId(account_id);
                    account.setBalance(balance);

                    CreditCardInterface creditCard = new CreditCard();
                    creditCard.setId(creditCard_id);
                    creditCard.setAccount(account);
                    creditCard.setLastUsed(last_used);
                    creditCard.setPinCode(pin_code);
                    creditCard.setWrongPinCodeAttempts(wrong_pin_code_attempts);
                    creditCard.setBlocked(blocked);

                    cards.add(creditCard);
                }
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return cards;
    }

    public AccountInterface createAccount(AccountInterface account) {
        _account = account;

        Statement statement;

        try {
            if(!_connection.isClosed()) {
                String query = "INSERT INTO bankAccount (balance) VALUES (0);";
                statement = _connection.createStatement();

                statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

                ResultSet result = statement.getGeneratedKeys();
                if(result.next())
                    _account.setId(result.getInt(1));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return _account;
    }

    public void updateAccount(AccountInterface account) {
        _account = account;

        PreparedStatement statement;

        try {
            if(!_connection.isClosed()) {
                String query = "UPDATE bankAccount SET balance = ? WHERE id = " + account.getId();
                statement = _connection.prepareStatement(query);
                statement.setDouble(1, account.getBalance());

                statement.executeUpdate();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public AccountInterface getAccount(int id) {
        Statement statement;
        ResultSet result;

        try {
            if(!_connection.isClosed()) {
                statement = _connection.createStatement();
                result = statement.executeQuery("SELECT * FROM bankAccount WHERE id = " + id + " LIMIT 1;");


                if(result.next()){
                    int account_id = result.getInt("id");
                    double balance = result.getDouble("balance");

                    AccountInterface account = new Account();
                    account.setId(account_id);
                    account.setBalance(balance);

                    _account = account;
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return _account;
    }

    public List<AccountInterface> getAccounts() {
        List<AccountInterface> accounts = new ArrayList<AccountInterface>();
        Statement statement;
        ResultSet result;

        try {
            if(!_connection.isClosed()) {
                statement = _connection.createStatement();
                result = statement.executeQuery("SELECT * FROM bankAccount;");


                while(result.next()){
                    int account_id = result.getInt("id");
                    double balance = result.getDouble("balance");

                    AccountInterface account = new Account();
                    account.setId(account_id);
                    account.setBalance(balance);

                    accounts.add(account);
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    public PreparedStatement preparedStatement(String sql)
    {
        try
        {
            return _connection.prepareStatement(sql);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public Connection getConnection() {
        return _connection;
    }

    public void closeConnection() {
        try {
            _connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
