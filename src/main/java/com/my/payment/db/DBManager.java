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
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DBManager {
    private static final Logger LOG = LogManager.getLogger(DBManager.class);
    private static final String FIND_USER = "SELECT u.id, u.login, r.name, u.pass, u.email, u.status FROM user u join roles r on u.roles_id=r.id WHERE login=?";
    private static final String ADD_USER = "INSERT INTO user (login, roles_id, pass, email, status) VALUES (?,?,?,?,?)";
    private static final String TRY_TO_LOGIN = "SELECT * FROM user WHERE BINARY login=? AND BINARY pass=?";
    private static final String GET_CARDS_FOR_USER = "SELECT c.id, c.name,c.number,c.end_date,c.cvv,c.balance,c.status FROM user_has_card uhc JOIN user u on uhc.user_id=u.id JOIN card c on uhc.card_id=c.id WHERE u.id=?";
    private static final String GET_ALL_CARDS="SELECT * FROM card";
    private static final String GET_CARD = "SELECT * FROM card WHERE number=? AND end_date=? AND cvv=?";
    private static final String CREATE_NEW_CARD = "INSERT INTO card(name, number,end_date,cvv,balance,status) VALUES(?,?,?,?,?,?)";
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
    private static final String GET_PAYMENT_BY_ID="SELECT c2.*,c1.*, p.date, p.amount, p.status,p.id FROM payment p JOIN card c1 on c1.id  = p.card_id_to join card c2 on c2.id = p.card_id_from WHERE p.id=?";
    private static final String COMMIT_PAYMENT="UPDATE payment SET status='sent', date=current_timestamp()  WHERE id=?";
    private static DBManager dbManager;
    private DataSource ds;

    private DBManager() {
        Context initContext = null;
        try {
            initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            ds = (DataSource) envContext.lookup("jdbc/MyDB");
            LOG.trace("Data source ==> " + ds);
        } catch (NamingException e) {
            LOG.warn(Message.CANNOT_OBTAIN_DATA_SOURCE);
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        Connection con = null;
        try {
            con = ds.getConnection();
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_OBTAIN_CONNECTION);

        }
        return con;
    }

    public static synchronized DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }
        return dbManager;
    }

    private void close(Connection con, Statement statement, ResultSet rs) {
        close(rs);
        close(con);
        close(statement);
    }

    private void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException exception) {
                LOG.warn(Message.CANNOT_CLOSE_RESULT_SET);

            }
        }
    }

    private void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException exception) {
                LOG.warn(Message.CANNOT_CLOSE_STATEMENT);

            }
        }
    }

    private void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException exception) {
                LOG.warn(Message.CANNOT_CLOSE_CONNECTION);

            }
        }
    }

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
                Card card = new Card(rs.getInt(1), rs.getString(2), rs.getString(3), c, rs.getInt(5), rs.getDouble(6), Status.valueOf(rs.getString(7).toUpperCase()));
                cards.add(card);
            }
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_OBTAIN_CARDS);

        } finally {
            close(con, ps, rs);
        }
        return cards;
    }

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
                Card card = new Card(rs.getInt(1), rs.getString(2), rs.getString(3), c, rs.getInt(5), rs.getDouble(6), Status.valueOf(rs.getString(7).toUpperCase()));
                cards.add(card);
            }
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_OBTAIN_CARDS);

        } finally {
            close(con, s, rs);
        }
        return cards;
    }

    public boolean addNewCard(Card card, User user) {
        PreparedStatement ps = null;
        Connection con = null;
        card.setCardID(isCardExist(card));
        try {
            if (card.getCardID() == -1) {
                LOG.trace("Create new card");
                card.setCardID(createCard(card));
            }
            con = dbManager.getConnection();
            ps = con.prepareStatement(ADD_CARD);
            ps.setInt(1, user.getUserID());
            ps.setInt(2, card.getCardID());
            int count = ps.executeUpdate();
            if (count != 1) {
                throw new SQLException();
            }
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_ADD_CARD);

            return false;
        } finally {
            close(con);
            close(ps);
        }
        return true;
    }

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
                card = new Card(rs.getInt(1), rs.getString(2), rs.getString(3), c, rs.getInt(5), rs.getDouble(6), Status.valueOf(rs.getString(7).toUpperCase()));
            }
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_OBTAIN_CARDS);

        } finally {
            close(con, ps, rs);
        }
        return card;
    }

    private int createCard(Card card) {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        int id = -1;
        try {
            con = dbManager.getConnection();
            ps = con.prepareStatement(CREATE_NEW_CARD, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, card.getName());
            ps.setString(2, card.getNumber());
            ps.setDate(3, new Date(card.getDate().getTimeInMillis()));
            ps.setInt(4, card.getCvv());
            ps.setDouble(5, card.getBalance());
            ps.setString(6, card.getStatus().toString().toLowerCase());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_OBTAIN_CARDS);

        } finally {
            close(con, ps, rs);
        }
        return id;
    }

    private int isCardExist(Card card) {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        int id = -1;
        try {
            con = dbManager.getConnection();
            ps = con.prepareStatement(GET_CARD);
            ps.setString(1, card.getNumber());
            ps.setDate(2, new Date(card.getDate().getTimeInMillis()));
            ps.setInt(3, card.getCvv());
            rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_OBTAIN_CARDS);
        } finally {
            close(con, ps, rs);
        }

        return id;
    }

    public boolean findCard(Card card, User user) {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dbManager.getConnection();
            ps = con.prepareStatement(GET_CARDS_FOR_USER);
            ps.setInt(1, user.getUserID());
            rs = ps.executeQuery();
            while (rs.next()) {
                String number = rs.getString(3);
                Calendar c = Calendar.getInstance();
                c.setTime(rs.getDate(4));
                int cvv = rs.getInt(5);
                if (number.equals(card.getNumber()) && cvv == card.getCvv() && c.equals(card.getDate())) {
                    LOG.trace("Card found");
                    return true;
                }
            }
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_OBTAIN_CARDS);

        } finally {
            close(con, ps, rs);
        }
        LOG.trace("Card doesn't found");
        return false;
    }

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
                Card cardFrom = new Card(rs.getInt(1), rs.getString(2), rs.getString(3), c1, rs.getInt(5), rs.getDouble(6), Status.valueOf(rs.getString(7).toUpperCase()));
                Calendar c2 = Calendar.getInstance();
                c2.setTime(rs.getDate(11));
                Card cardto = new Card(rs.getInt(8), rs.getString(9), rs.getString(10), c2, rs.getInt(12), rs.getDouble(13), Status.valueOf(rs.getString(14).toUpperCase()));
                Calendar paymentDate = Calendar.getInstance();
                String textDate = rs.getString(15);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date date = sdf.parse(textDate);
                paymentDate.setTime(date);
                payments.add(new Payment(rs.getInt(18), cardFrom, cardto, paymentDate, rs.getDouble(16), PaymentStatus.valueOf(rs.getString(17).toUpperCase())));
            }
            ps = con.prepareStatement(GET_PAYMENTS_OUT);
            ps.setInt(1, card.getCardID());
            rs = ps.executeQuery();
            while (rs.next()) {
                Calendar c1 = Calendar.getInstance();
                c1.setTime(rs.getDate(4));
                Card cardFrom = new Card(rs.getInt(1), rs.getString(2), rs.getString(3), c1, rs.getInt(5), rs.getDouble(6), Status.valueOf(rs.getString(7).toUpperCase()));
                Calendar c2 = Calendar.getInstance();
                c2.setTime(rs.getDate(11));
                Card cardto = new Card(rs.getInt(8), rs.getString(9), rs.getString(10), c2, rs.getInt(12), rs.getDouble(13), Status.valueOf(rs.getString(14).toUpperCase()));
                Calendar paymentDate = Calendar.getInstance();
                String textDate = rs.getString(15);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date date = sdf.parse(textDate);
                paymentDate.setTime(date);
                payments.add(new Payment(rs.getInt(18),cardFrom, cardto, paymentDate, rs.getDouble(16), PaymentStatus.valueOf(rs.getString(17).toUpperCase())));
            }

        } catch (SQLException | ParseException exception) {
            LOG.warn(Message.CANNOT_OBTAIN_CARDS);

        } finally {
            close(con, ps, rs);
        }
        return payments;
    }

    public Payment getPaymentByID(int id) {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        Payment payment=null;
        try {
            con = dbManager.getConnection();
            ps = con.prepareStatement(GET_PAYMENT_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Calendar c1 = Calendar.getInstance();
                c1.setTime(rs.getDate(4));
                Card cardFrom = new Card(rs.getInt(1), rs.getString(2), rs.getString(3), c1, rs.getInt(5), rs.getDouble(6), Status.valueOf(rs.getString(7).toUpperCase()));
                Calendar c2 = Calendar.getInstance();
                c2.setTime(rs.getDate(11));
                Card cardto = new Card(rs.getInt(8), rs.getString(9), rs.getString(10), c2, rs.getInt(12), rs.getDouble(13), Status.valueOf(rs.getString(14).toUpperCase()));
                Calendar paymentDate = Calendar.getInstance();
                String textDate = rs.getString(15);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date date = sdf.parse(textDate);
                paymentDate.setTime(date);
                payment = new Payment(rs.getInt(18), cardFrom, cardto, paymentDate, rs.getDouble(16), PaymentStatus.valueOf(rs.getString(17).toUpperCase()));
            }
        } catch (SQLException | ParseException exception) {
            LOG.warn(Message.CANNOT_OBTAIN_CARDS);

        } finally {
            close(con, ps, rs);
        }
        return payment;
    }

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
                cardFrom = new Card(rs.getInt(1), rs.getString(2), rs.getString(3), c, rs.getInt(5), rs.getDouble(6), Status.valueOf(rs.getString(7).toUpperCase()));
            }
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_OBTAIN_CARDS);
        } finally {
            close(con, ps, rs);
        }
        return cardFrom;
    }

    public boolean commitPayment(Payment payment)
    {
        PreparedStatement ps = null;
        Connection con = null;
        boolean result = false;
        try {
            con = dbManager.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(COMMIT_PAYMENT);
            ps.setInt(1, payment.getId());
            if(ps.executeUpdate()==1) {
                withdraw(payment.getFrom().getCardID(), payment.getAmount(), con);
                transfer(payment.getTo().getCardID(), payment.getAmount(), con);
                con.commit();
                result = true;
            }else throw new SQLException();

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
            } else {
                throw new SQLException();
            }
        } catch (SQLException exception) {
            LOG.warn("Cannot transfer");
        } finally {
            close(con);
            close(ps);
        }
        return result;
    }
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
            if (count != 1) {
                throw new SQLException();
            }
            result = true;
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_CHANGE_STATUS);
        } finally {
            close(con);
            close(ps);
        }
        return result;
    }

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
            if (count != 1) {
                throw new SQLException();
            }
            result = true;
        } catch (SQLException exception) {
            LOG.warn(Message.CANNOT_CHANGE_STATUS);
        } finally {
            close(con);
            close(ps);
        }
        return result;
    }
}
