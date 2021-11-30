package com.my.payment.db;

import com.my.payment.constants.Message;
import com.my.payment.db.entity.Card;
import com.my.payment.db.entity.Payment;
import com.my.payment.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/***
 * Class for work with DB
 * @author Sihov Dmytro
 */
public class DBManager {
    private static final Logger LOG = LogManager.getLogger(DBManager.class);
    private static final String FIND_USER = "SELECT u.id, u.login, r.name, u.pass, u.email, u.status FROM user u join roles r on u.roles_id=r.id WHERE login=?";
    private static final String ADD_USER = "INSERT INTO user (login, roles_id, pass, email, status) VALUES (?,?,?,?,?)";
    private static final String TRY_TO_LOGIN = "SELECT * FROM user WHERE BINARY login=? AND BINARY pass=?";
    private static final String GET_CARDS_FOR_USER = "SELECT c.id, c.name,c.number,c.end_date,c.cvv,c.balance,c.status,c.pin FROM user_has_card uhc JOIN user u on uhc.user_id=u.id JOIN card c on uhc.card_id=c.id WHERE u.id=?";
    private static final String GET_CARD_FOR_USER = "SELECT c.id, c.name,c.number,c.end_date,c.cvv,c.balance,c.status,c.pin FROM user_has_card uhc JOIN user u on uhc.user_id=u.id JOIN card c on uhc.card_id=c.id WHERE u.id=? AND c.id=?";
    private static final String GET_ALL_CARDS = "SELECT * FROM card";
    private static final String GET_CARD = "SELECT * FROM card WHERE number=? AND end_date=? AND cvv=?";
    private static final String CREATE_NEW_CARD = "INSERT INTO card(name, number,end_date,cvv,balance,status,pin) VALUES(?,?,date_add(curdate(), interval 5 YEAR),?,0,'active',?); ";
    private static final String ADD_CARD = "INSERT INTO user_has_card(user_id,card_id) VALUES(?,?)";
    private static final String GET_PAYMENTS_IN = "SELECT c2.*,c1.*, p.date, p.amount, p.status,p.id FROM payment p JOIN card c1 on c1.id  = p.card_id_to join card c2 on c2.id = p.card_id_from WHERE c1.id=? AND p.status='sent' ORDER BY p.date DESC;";
    private static final String GET_PAYMENTS_OUT = "SELECT c2.*,c1.*, p.date, p.amount, p.status,p.id FROM payment p JOIN card c1 on c1.id  = p.card_id_to join card c2 on c2.id = p.card_id_from WHERE c2.id=? ORDER BY p.date DESC";
    private static final String GET_USERS = "SELECT u.id, u.login, r.name, u.pass, u.email, u.status FROM user u join roles r on u.roles_id=r.id";
    private static final String GET_CARD_BY_ID = "SELECT * FROM card WHERE id=?";
    private static final String GET_CARD_BY_NUMBER = "SELECT * FROM card WHERE number=?";
    private static final String MAKE_PAYMENT = "INSERT INTO payment(card_id_from,card_id_to, date,amount,status) values(?,?,?,?,?)";
    private static final String TRANSFER = "UPDATE card SET balance=balance+ ? WHERE id=?";
    private static final String CHANGE_CARD_STATUS = "UPDATE card SET status=? WHERE id=?";
    private static final String CHANGE_USER_STATUS = "UPDATE user SET status = ? WHERE id=?";
    private static final String GET_PAYMENT_BY_ID = "SELECT c2.*,c1.*, p.date, p.amount, p.status,p.id FROM payment p JOIN card c1 on c1.id  = p.card_id_to join card c2 on c2.id = p.card_id_from WHERE p.id=?";
    private static final String COMMIT_PAYMENT = "UPDATE payment SET status='sent', date=current_timestamp()  WHERE id=?";
    private static final String CANCEL_PAYMENT="DELETE FROM payment WHERE (id = ?)";
    private static final String CHANGE_CARD_NAME="UPDATE card SET name=? WHERE id=?";
    private static DBManager dbManager;
    private DataSource ds;

    /**
     * Class constructor
     */
    private DBManager() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            ds = (DataSource) envContext.lookup("jdbc/MyDB");
            LOG.trace("Data source ==> " + ds);
        } catch (NamingException e) {
            LOG.warn(Message.CANNOT_OBTAIN_DATA_SOURCE);
            e.printStackTrace();
        }
    }

    /**
     * gets connection with database
     * @return Connection
    */
    public Connection getConnection() {
        Connection con = null;
        try {
            con = ds.getConnection();
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_OBTAIN_CONNECTION);

        }
        return con;
    }

    /**
     * Singleton
     * @return instance of DBManager
     */
    public static synchronized DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }
        return dbManager;
    }

    /**
     * Closes connection, resultset and statement
     * @param con Connection
     * @param statement Statement
     * @param rs ResultSet
     */
    private void close(Connection con, Statement statement, ResultSet rs) {
        close(rs);
        close(con);
        close(statement);
    }

    /**
     * Closes ResultSet
     * @param rs ResultSet
     */
    private void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException exception) {
                LOG.warn(Message.CANNOT_CLOSE_RESULT_SET);

            }
        }
    }

    /**
     * Closes Statement
     * @param st Statement
     */
    private void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException exception) {
                LOG.warn(Message.CANNOT_CLOSE_STATEMENT);

            }
        }
    }

    /**
     * Closes connection
     * @param con Connection
     */
    private void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException exception) {
                LOG.warn(Message.CANNOT_CLOSE_CONNECTION);

            }
        }
    }

    /**
     * Selects user by login
     * @param login user login
     * @return User
     */
    public User findUser(String login) {
        if (login == null) return null;
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dbManager.getConnection();
            ps = con.prepareStatement(FIND_USER);
            ps.setString(1, login);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt(1), rs.getString(2), Role.valueOf(rs.getString(3).toUpperCase()), rs.getString(4), rs.getString(5), Status.valueOf(rs.getString(6).toUpperCase()));
            }
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_FIND_USER);
        } finally {
            close(con, ps, rs);
        }
        return null;
    }

    /**
     * Inserts a new user
     * @param user User object
     * @return result
     */
    public boolean addUser(User user) {
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            ps = con.prepareStatement(ADD_USER);
            ps.setString(1, user.getLogin());
            ps.setInt(2, user.getRole().getId());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getStatus().toString());
            int count = ps.executeUpdate();
            if (count > 0) return true;
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_CREATE_USER);

        } finally {
            close(con);
            close(ps);
        }
        return false;
    }

    /**
     * Trying to login
     * @param login
     * @param password
     * @return result
     */
    public boolean try2Login(String login, String password) {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dbManager.getConnection();
            ps = con.prepareStatement(TRY_TO_LOGIN);
            ps.setString(1, login);
            ps.setString(2, password);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_LOGIN);

        } finally {
            close(con, ps, rs);
        }
        return false;
    }

    /**
     * Selects all cards for user
     * @param user User object
     * @return User objects list
     */
    public List<Card> getCardsForUser(User user) {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        List<Card> cards = new ArrayList<>();
        try {
            con = dbManager.getConnection();
            ps = con.prepareStatement(GET_CARDS_FOR_USER);
            ps.setInt(1, user.getUserID());
            rs = ps.executeQuery();
            while (rs.next()) {
                Calendar c = Calendar.getInstance();
                c.setTime(rs.getDate(4));
                Card card = new Card(rs.getInt(1), rs.getString(2), rs.getString(3), c, rs.getInt(5), rs.getDouble(6), Status.valueOf(rs.getString(7).toUpperCase()), rs.getInt(8));
                cards.add(card);
            }
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_OBTAIN_CARDS);

        } finally {
            close(con, ps, rs);
        }
        return cards;
    }

    /**
     * Selects all cards
     * @return Card objects list
     */
    public List<Card> getAllCards() {
        Statement s = null;
        Connection con = null;
        ResultSet rs = null;
        List<Card> cards = new ArrayList<>();
        try {
            con = dbManager.getConnection();
            s = con.createStatement();
            rs = s.executeQuery(GET_ALL_CARDS);
            while (rs.next()) {
                Calendar c = Calendar.getInstance();
                c.setTime(rs.getDate(4));
                Card card = new Card(rs.getInt(1), rs.getString(2), rs.getString(3), c, rs.getInt(5), rs.getDouble(6), Status.valueOf(rs.getString(7).toUpperCase()), rs.getInt(8));
                cards.add(card);
            }
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_OBTAIN_CARDS);

        } finally {
            close(con, s, rs);
        }
        return cards;
    }

    /**
     * Inserts card for user
     * @param card
     * @param user
     * @return operation result
     */
    public boolean addCard(Card card, User user) {
        PreparedStatement ps = null;
        Connection con = null;
        boolean result = false;
        try {
            con = dbManager.getConnection();
            ps = con.prepareStatement(ADD_CARD);
            ps.setInt(1, user.getUserID());
            ps.setInt(2, card.getCardID());
            int count = ps.executeUpdate();
            if (count == 1) {
                result = true;
            }
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_ADD_CARD);
        } finally {
            close(con);
            close(ps);
        }
        return result;
    }

    /**
     * Inserts a new card
     * @param card
     * @param user
     * @param con
     * @return operation result
     */
    private boolean addNewCard(Card card, User user, Connection con) {
        PreparedStatement ps = null;
        boolean result = false;
        try {
            ps = con.prepareStatement(ADD_CARD);
            ps.setInt(1, user.getUserID());
            ps.setInt(2, card.getCardID());
            int count = ps.executeUpdate();
            if (count == 1) {
                result = true;
            }
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_ADD_CARD);
            return result;
        } finally {
            close(ps);
        }
        return result;
    }

    /**
     * Selects card by id
     * @param id card id
     * @return Card object
     */
    public Card getCardByID(int id) {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        Card card = null;
        try {
            con = dbManager.getConnection();
            ps = con.prepareStatement(GET_CARD_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Calendar c = Calendar.getInstance();
                c.setTime(rs.getDate(4));
                card = new Card(rs.getInt(1), rs.getString(2), rs.getString(3), c, rs.getInt(5), rs.getDouble(6), Status.valueOf(rs.getString(7).toUpperCase()), rs.getInt(8));
            }
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_OBTAIN_CARDS);

        } finally {
            close(con, ps, rs);
        }
        return card;
    }

    /**
     * Inserts a new card
     * @param card
     * @param user
     * @return operation result
     */
    public boolean createNewCard(Card card, User user) {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            con = dbManager.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(CREATE_NEW_CARD, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, card.getName());
            ps.setString(2, card.getNumber());
            ps.setInt(3, card.getCvv());
            ps.setInt(4, card.getPin());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                LOG.trace("generatedKey ==> " + rs.getInt(1));
                card.setCardID(rs.getInt(1));
                if (addNewCard(card, user, con)) {
                    LOG.trace("Add card");
                    con.commit();
                    result = true;
                }
            }
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_OBTAIN_CARDS);
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException exception1) {
                    LOG.warn("Cannot rollback " + exception);
                }
            }
        } finally {
            close(con, ps, rs);
        }
        return result;
    }

    /**
     * Checks if user already have this card
     * @param card
     * @param user
     * @return operation result
     */
    public boolean findCard(Card card, User user) {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            con = dbManager.getConnection();
            ps = con.prepareStatement(GET_CARD_FOR_USER);
            ps.setInt(1, user.getUserID());
            ps.setInt(2, card.getCardID());
            rs = ps.executeQuery();
            if (rs.next()) {
                result = true;
            }

        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_OBTAIN_CARDS);

        } finally {
            close(con, ps, rs);
        }
        return result;
    }

    /**
     * Selects payments for card
     * @param card
     * @return Payments objects list
     */
    public List<Payment> getPayments(Card card) {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        List<Payment> payments = new ArrayList<>();
        try {
            con = dbManager.getConnection();
            ps = con.prepareStatement(GET_PAYMENTS_IN);
            ps.setInt(1, card.getCardID());
            rs = ps.executeQuery();
            while (rs.next()) {
                Calendar c1 = Calendar.getInstance();
                c1.setTime(rs.getDate(4));
                Card cardFrom = new Card(rs.getInt(1), rs.getString(2), rs.getString(3), c1, rs.getInt(5), rs.getDouble(6), Status.valueOf(rs.getString(7).toUpperCase()), rs.getInt(8));
                Calendar c2 = Calendar.getInstance();
                c2.setTime(rs.getDate(12));
                Card cardto = new Card(rs.getInt(9), rs.getString(10), rs.getString(11), c2, rs.getInt(13), rs.getDouble(14), Status.valueOf(rs.getString(15).toUpperCase()), rs.getInt(16));
                Calendar paymentDate = Calendar.getInstance();
                String textDate = rs.getString(17);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date date = sdf.parse(textDate);
                paymentDate.setTime(date);
                payments.add(new Payment(rs.getInt(20), cardFrom, cardto, paymentDate, rs.getDouble(18), PaymentStatus.valueOf(rs.getString(19).toUpperCase())));
            }
            ps = con.prepareStatement(GET_PAYMENTS_OUT);
            ps.setInt(1, card.getCardID());
            rs = ps.executeQuery();
            while (rs.next()) {
                Calendar c1 = Calendar.getInstance();
                c1.setTime(rs.getDate(4));
                Card cardFrom = new Card(rs.getInt(1), rs.getString(2), rs.getString(3), c1, rs.getInt(5), rs.getDouble(6), Status.valueOf(rs.getString(7).toUpperCase()), rs.getInt(8));
                Calendar c2 = Calendar.getInstance();
                c2.setTime(rs.getDate(12));
                Card cardto = new Card(rs.getInt(9), rs.getString(10), rs.getString(11), c2, rs.getInt(13), rs.getDouble(14), Status.valueOf(rs.getString(15).toUpperCase()), rs.getInt(16));
                Calendar paymentDate = Calendar.getInstance();
                String textDate = rs.getString(17);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date date = sdf.parse(textDate);
                paymentDate.setTime(date);
                payments.add(new Payment(rs.getInt(20), cardFrom, cardto, paymentDate, rs.getDouble(18), PaymentStatus.valueOf(rs.getString(19).toUpperCase())));
            }

        } catch (SQLException | ParseException exception) {
            LOG.warn(Message.CANNOT_OBTAIN_CARDS);

        } finally {
            close(con, ps, rs);
        }
        return payments;
    }

    /**
     * Selects payment by id
     * @param id
     * @return Payment object
     */
    public Payment getPaymentByID(int id) {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        Payment payment = null;
        try {
            con = dbManager.getConnection();
            ps = con.prepareStatement(GET_PAYMENT_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Calendar c1 = Calendar.getInstance();
                c1.setTime(rs.getDate(4));
                Card cardFrom = new Card(rs.getInt(1), rs.getString(2), rs.getString(3), c1, rs.getInt(5), rs.getDouble(6), Status.valueOf(rs.getString(7).toUpperCase()), rs.getInt(8));
                Calendar c2 = Calendar.getInstance();
                c2.setTime(rs.getDate(12));
                Card cardto = new Card(rs.getInt(9), rs.getString(10), rs.getString(11), c2, rs.getInt(13), rs.getDouble(14), Status.valueOf(rs.getString(15).toUpperCase()), rs.getInt(16));
                Calendar paymentDate = Calendar.getInstance();
                String textDate = rs.getString(17);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date date = sdf.parse(textDate);
                paymentDate.setTime(date);
                payment = new Payment(rs.getInt(20), cardFrom, cardto, paymentDate, rs.getDouble(18), PaymentStatus.valueOf(rs.getString(19).toUpperCase()));
            }
        } catch (SQLException | ParseException exception) {
            LOG.warn(Message.CANNOT_OBTAIN_CARDS);

        } finally {
            close(con, ps, rs);
        }
        return payment;
    }

    /**
     * Selects card by number
     * @param number card number
     * @return Card object
     */
    public Card getCardByNumber(String number) {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        Card cardFrom = null;
        try {
            con = dbManager.getConnection();
            ps = con.prepareStatement(GET_CARD_BY_NUMBER);
            ps.setString(1, number);
            rs = ps.executeQuery();
            if (rs.next()) {
                Calendar c = Calendar.getInstance();
                c.setTime(rs.getDate(4));
                cardFrom = new Card(rs.getInt(1), rs.getString(2), rs.getString(3), c, rs.getInt(5), rs.getDouble(6), Status.valueOf(rs.getString(7).toUpperCase()), rs.getInt(8));
            }
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_OBTAIN_CARDS);
        } finally {
            close(con, ps, rs);
        }
        return cardFrom;
    }

    /**
     * Confirms payment and makes transaction
     * @param payment
     * @return operation result
     */
    public boolean commitPayment(Payment payment) {
        PreparedStatement ps = null;
        Connection con = null;
        boolean result = false;
        try {
            con = dbManager.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(COMMIT_PAYMENT);
            ps.setInt(1, payment.getId());
            if (ps.executeUpdate() == 1) {
                withdraw(payment.getFrom().getCardID(), payment.getAmount(), con);
                transfer(payment.getTo().getCardID(), payment.getAmount(), con);
                con.commit();
                result = true;
            } else throw new SQLException();

        } catch (SQLException exception) {
            LOG.warn("Cannot make payment");
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException exception1) {
                    LOG.warn("Cannot rollback " + exception);
                }
            }
        } finally {
            close(con);
            close(ps);
        }
        return result;
    }

    /**
     * Prepares payment
     * @param payment
     * @return operation result
     */
    public boolean preparePayment(Payment payment) {
        PreparedStatement ps = null;
        Connection con = null;
        boolean result = false;
        try {
            con = dbManager.getConnection();
            ps = con.prepareStatement(MAKE_PAYMENT);
            ps.setInt(1, payment.getFrom().getCardID());
            ps.setInt(2, payment.getTo().getCardID());
            Calendar c = payment.getDate();
            Timestamp ts = new Timestamp(c.getTimeInMillis());
            ps.setTimestamp(3, ts);
            ps.setDouble(4, payment.getAmount());
            ps.setString(5, payment.getStatus().toString().toLowerCase());
            if (ps.executeUpdate() > 0) {
                result = true;
            }
        } catch (SQLException exception) {
            LOG.warn("Cannot transfer");
        } finally {
            close(con);
            close(ps);
        }
        return result;
    }

    /**
     * Makes transaction
     * @param payment
     * @return
     */
    public boolean makePayment(Payment payment) {
        PreparedStatement ps = null;
        Connection con = null;
        boolean result = false;
        try {
            con = dbManager.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(MAKE_PAYMENT);
            ps.setInt(1, payment.getFrom().getCardID());
            ps.setInt(2, payment.getTo().getCardID());
            Calendar c = payment.getDate();
            Timestamp ts = new Timestamp(c.getTimeInMillis());
            ps.setTimestamp(3, ts);
            ps.setDouble(4, payment.getAmount());
            ps.setString(5, payment.getStatus().toString().toLowerCase());

            if (ps.executeUpdate() > 0) {
                withdraw(payment.getFrom().getCardID(), payment.getAmount(), con);
                transfer(payment.getTo().getCardID(), payment.getAmount(), con);
                con.commit();
                result = true;
            } else {
                throw new SQLException();
            }

        } catch (SQLException exception) {
            LOG.warn("Cannot make payment");
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException exception1) {
                    LOG.warn("Cannot rollback " + exception);
                }
            }
        } finally {
            close(con);
            close(ps);
        }
        return result;
    }

    /**
     * Withdraws money from card
     * @param id card id
     * @param amount
     * @param con Connection
     * @throws SQLException
     */
    private void withdraw(int id, double amount, Connection con) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(TRANSFER);
            ps.setDouble(1, -amount);
            ps.setInt(2, id);
            ps.executeUpdate();
        } finally {
            close(ps);
        }
    }

    /**
     * Transfers money to card
     * @param id card id
     * @param amount
     * @param con Connection
     * @return operation result
     * @throws SQLException
     */
    private boolean transfer(int id, double amount, Connection con) throws SQLException {
        PreparedStatement ps = null;
        boolean result;
        try {
            ps = con.prepareStatement(TRANSFER);
            ps.setDouble(1, amount);
            ps.setInt(2, id);
            int count = ps.executeUpdate();
            result = count == 1;
        } finally {
            close(ps);
        }
        return result;
    }

    /**
     * Updates card status
     * @param id card id
     * @param status new status
     * @return operation result
     */
    public boolean changeCardStatus(int id, Status status) {
        boolean result = false;
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            ps = con.prepareStatement(CHANGE_CARD_STATUS);
            ps.setString(1, status.toString().toLowerCase());
            ps.setInt(2, id);
            int count = ps.executeUpdate();
            if (count == 1) {
                result = true;
            }

        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_CHANGE_STATUS);
        } finally {
            close(con);
            close(ps);
        }
        return result;
    }

    /**
     * Replenish the card
     * @param c card object
     * @param amount
     * @return operation result
     */
    public boolean topUpCard(Card c, double amount) {
        boolean result = false;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            result = transfer(c.getCardID(), amount, con);
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_TOP_UP);
        } finally {
            close(con);
        }
        return result;
    }

    /**
     * Selects all users
     * @return users objects list
     */
    public List<User> getAllUsers() {
        Statement s = null;
        Connection con = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();
        try {
            con = dbManager.getConnection();
            s = con.createStatement();
            rs = s.executeQuery(GET_USERS);
            while (rs.next()) {
                users.add(new User(rs.getInt(1), rs.getString(2), Role.valueOf(rs.getString(3).toUpperCase()), rs.getString(4), rs.getString(5), Status.valueOf(rs.getString(6).toUpperCase())));
            }
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_OBTAIN_USERS);
        } finally {
            close(con, s, rs);
        }
        return users;
    }

    /**
     * Updates user status
     * @param id user id
     * @param status new status
     * @return operation result
     */
    public boolean changeUserStatus(int id, Status status) {
        boolean result = false;
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            ps = con.prepareStatement(CHANGE_USER_STATUS);
            ps.setString(1, status.toString().toLowerCase());
            ps.setInt(2, id);
            int count = ps.executeUpdate();
            if (count == 1) {
                result = true;
            }
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_CHANGE_STATUS);
        } finally {
            close(con);
            close(ps);
        }
        return result;
    }

    /**
     * Cancels payment
     * @param payment
     * @return operation result
     */
    public boolean cancelPayment(Payment payment)
    {
        PreparedStatement ps = null;
        Connection con = null;
        boolean result = false;
        try {
            con = dbManager.getConnection();
            ps = con.prepareStatement(CANCEL_PAYMENT);
            ps.setInt(1, payment.getId());
            if (ps.executeUpdate() == 1) {
                result = true;
            }

        } catch (SQLException exception) {
            LOG.warn("Cannot cancel payment");
        } finally {
            close(con);
            close(ps);
        }
        return result;
    }

    /**
     * Updates card name
     * @param card
     * @param newName
     * @return operation result
     */
    public boolean changeCardName(Card card, String newName)
    {
        boolean result=false;
        Connection con =null;
        PreparedStatement ps = null;
        try{
            con = dbManager.getConnection();
            ps = con.prepareStatement(CHANGE_CARD_NAME);
            ps.setString(1,newName);
            ps.setInt(2,card.getCardID());
            int count = ps.executeUpdate();
            if(count==1)
            {
                result=true;
            }
        }catch (SQLException exception)
        {
            LOG.trace("Cannot change card name");
        }
        return result;
    }

}
