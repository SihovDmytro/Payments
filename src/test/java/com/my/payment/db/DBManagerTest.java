package com.my.payment.db;

import com.my.payment.db.entity.Card;
import com.my.payment.db.entity.Payment;
import com.my.payment.db.entity.User;
import com.my.payment.util.PasswordHash;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DBManagerTest {

    private DBManager dbM;
    private Connection con;
    private Statement s;
    private PreparedStatement ps;
    private ResultSet rs;

    private void setMock(DBManager mock) {
        try {
            Field instance = DBManager.class.getDeclaredField("dbManager");
            instance.setAccessible(true);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() throws SQLException {
        dbM = mock(DBManager.class);
        setMock(dbM);
        con = mock(Connection.class);
        ps = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);
        when(dbM.getConnection()).thenReturn(con);
        when(con.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
    }

    @Test
    public void try2LoginSuccess() throws NoSuchAlgorithmException, SQLException {
        when(rs.next()).thenReturn(true);
        when(dbM.try2Login(anyString(), anyString())).thenCallRealMethod();
        String pass = PasswordHash.hash("123456");

        boolean check = dbM.try2Login("dmytro", pass);

        assertTrue(check);

        verify(dbM, times(1)).getConnection();
        verify(con, times(1)).prepareStatement(anyString());
        verify(ps, times(2)).setString(anyInt(), anyString());
        verify(ps, times(1)).executeQuery();
        verify(rs, times(1)).next();

    }

    @Test
    public void findUser() throws NoSuchAlgorithmException, SQLException {
        when(dbM.findUser(anyString())).thenCallRealMethod();
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(1);
        when(rs.getString(2)).thenReturn("dmytro");
        when(rs.getString(3)).thenReturn("admin");
        when(rs.getString(4)).thenReturn(PasswordHash.hash("123456"));
        when(rs.getString(5)).thenReturn("dima.sigov1@gmail.com");
        when(rs.getString(6)).thenReturn("active");

        User user = dbM.findUser("dmytro");
        assertNotNull(user);
        verify(dbM, times(1)).getConnection();
        verify(con, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setString(anyInt(), anyString());
        verify(ps, times(1)).executeQuery();
        verify(rs, times(1)).next();
        verify(rs, times(1)).getInt(anyInt());
        verify(rs, times(5)).getString(anyInt());
    }

    @Test
    public void getCardsForUser() throws SQLException, ParseException {
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(dbM.getCardsForUser(any())).thenCallRealMethod();
        when(rs.getInt(1)).thenReturn(1);
        when(rs.getString(2)).thenReturn("Название");
        when(rs.getString(3)).thenReturn("1234567899876543");
        when(rs.getDate(4)).thenReturn(Date.valueOf("2024-11-12"));
        when(rs.getInt(5)).thenReturn(1);
        when(rs.getDouble(6)).thenReturn(9000.00);
        when(rs.getString(7)).thenReturn("active");
        when(rs.getInt(8)).thenReturn(455);

        List<Card> cards = dbM.getCardsForUser(new User());
        assertNotNull(cards);
        verify(dbM, times(1)).getConnection();
        verify(con, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setInt(anyInt(), anyInt());
        verify(ps, times(1)).executeQuery();
        verify(rs, times(2)).next();
        verify(rs, times(3)).getInt(anyInt());
        verify(rs, times(3)).getString(anyInt());
        verify(rs, times(1)).getDouble(anyInt());
        verify(rs, times(1)).getDate(anyInt());
    }

    @Test
    public void addUser() throws SQLException {
        when(ps.executeUpdate()).thenReturn(1);
        when(dbM.addUser(any())).thenCallRealMethod();
        User user = new User("namdasdae", Role.USER, "123321123123", "1234567899876543", Status.ACTIVE, "dmytro", Calendar.getInstance());
        boolean check = dbM.addUser(user);

        assertTrue(check);
        verify(dbM, times(1)).getConnection();
        verify(con, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setInt(anyInt(), anyInt());
        verify(ps, times(4)).setString(anyInt(), anyString());
        verify(ps, times(1)).executeUpdate();

    }

    @Test
    public void getCardByID() throws SQLException {
        when(rs.next()).thenReturn(true);
        when(dbM.getCardByID(anyInt())).thenCallRealMethod();
        when(rs.getInt(1)).thenReturn(1);
        when(rs.getString(2)).thenReturn("Название");
        when(rs.getString(3)).thenReturn("1234567899876543");
        when(rs.getDate(4)).thenReturn(Date.valueOf("2024-11-12"));
        when(rs.getInt(5)).thenReturn(1);
        when(rs.getDouble(6)).thenReturn(9000.00);
        when(rs.getString(7)).thenReturn("active");
        when(rs.getInt(8)).thenReturn(455);

        Card card = dbM.getCardByID(123);
        assertNotNull(card);

        verify(dbM, times(1)).getConnection();
        verify(con, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setInt(anyInt(), anyInt());
        verify(ps, times(1)).executeQuery();
        verify(rs, times(1)).next();
        verify(rs, times(3)).getInt(anyInt());
        verify(rs, times(3)).getString(anyInt());
        verify(rs, times(1)).getDouble(anyInt());
        verify(rs, times(1)).getDate(anyInt());
    }

    @Test
    public void createNewCard() throws SQLException {
        when(rs.next()).thenReturn(true);
        when(dbM.createNewCard(any())).thenCallRealMethod();
        doNothing().when(con).commit();
        when(con.prepareStatement(anyString(), anyInt())).thenReturn(ps);
        when(con.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(1);
        when(ps.getGeneratedKeys()).thenReturn(rs);
        when(rs.getInt(1)).thenReturn(1);

        User user = new User("namdasdae", Role.USER, "123321123123", "1234567899876543", Status.ACTIVE, "dmytro", Calendar.getInstance());
        Card card = new Card("название", "1999567899876543", Calendar.getInstance(), 1, 0, Status.ACTIVE, 1);
        boolean check = dbM.createNewCard(card);
        assertTrue(check);

        verify(dbM, times(1)).getConnection();
        verify(con, times(1)).setAutoCommit(false);
        verify(con, times(1)).prepareStatement(anyString(), anyInt());
        verify(ps, times(2)).setString(anyInt(), anyString());
        verify(ps, times(4)).setInt(anyInt(), anyInt());
        verify(ps, times(2)).executeUpdate();
        verify(ps, times(1)).getGeneratedKeys();
        verify(rs, times(1)).next();
        verify(rs, times(1)).getInt(anyInt());
        verify(con, times(1)).prepareStatement(anyString());
        verify(con, times(1)).commit();
    }

    @Test
    public void findCard() throws SQLException {
        when(rs.next()).thenReturn(true);
        when(dbM.findCard(any())).thenCallRealMethod();


        Card card = new Card("название", "1999567899876543", Calendar.getInstance(), 1, 0, Status.ACTIVE, 1);
        boolean check = dbM.findCard(card);
        assertTrue(check);

        verify(dbM, times(1)).getConnection();
        verify(con, times(1)).prepareStatement(anyString());
        verify(ps, times(2)).setInt(anyInt(), anyInt());
        verify(ps, times(1)).executeQuery();
        verify(rs, times(1)).next();
    }

    @Test
    public void getPayments() throws SQLException {
        when(dbM.getPayments(any())).thenCallRealMethod();
        when(rs.next()).thenReturn(true).thenReturn(false).thenReturn(false);
        when(rs.getInt(1)).thenReturn(1);
        when(rs.getString(2)).thenReturn("Название");
        when(rs.getString(3)).thenReturn("1234567899876543");
        when(rs.getDate(4)).thenReturn(Date.valueOf("2024-11-12"));
        when(rs.getInt(5)).thenReturn(1);
        when(rs.getDouble(6)).thenReturn(9000.00);
        when(rs.getString(7)).thenReturn("active");
        when(rs.getInt(8)).thenReturn(455);
        when(rs.getInt(9)).thenReturn(2);
        when(rs.getString(10)).thenReturn("Название2");
        when(rs.getString(11)).thenReturn("1111222233334444");
        when(rs.getDate(12)).thenReturn(Date.valueOf("2024-10-10"));
        when(rs.getInt(13)).thenReturn(2);
        when(rs.getDouble(14)).thenReturn(9.00);
        when(rs.getString(15)).thenReturn("active");
        when(rs.getInt(16)).thenReturn(4);
        when(rs.getString(17)).thenReturn("2024-10-10 12:12:12");
        when(rs.getDouble(18)).thenReturn(5.56);
        when(rs.getString(19)).thenReturn("prepared");
        when(rs.getInt(20)).thenReturn(1);

        Card card = new Card("название", "1999567899876543", Calendar.getInstance(), 1, 0, Status.ACTIVE, 1);
        List<Payment> payments = dbM.getPayments(card);
        assertNotNull(payments);

        verify(dbM, times(1)).getConnection();
        verify(con, times(2)).prepareStatement(anyString());
        verify(ps, times(2)).setInt(anyInt(), anyInt());
        verify(ps, times(2)).executeQuery();
        verify(rs, times(3)).next();
        verify(rs, times(7)).getInt(anyInt());
        verify(rs, times(8)).getString(anyInt());
        verify(rs, times(3)).getDouble(anyInt());
        verify(rs, times(2)).getDate(anyInt());

    }

    @Test
    public void getPaymentByID() throws SQLException {
        when(dbM.getPaymentByID(anyInt())).thenCallRealMethod();
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(1);
        when(rs.getString(2)).thenReturn("Название");
        when(rs.getString(3)).thenReturn("1234567899876543");
        when(rs.getDate(4)).thenReturn(Date.valueOf("2024-11-12"));
        when(rs.getInt(5)).thenReturn(1);
        when(rs.getDouble(6)).thenReturn(9000.00);
        when(rs.getString(7)).thenReturn("active");
        when(rs.getInt(8)).thenReturn(455);
        when(rs.getInt(9)).thenReturn(2);
        when(rs.getString(10)).thenReturn("Название2");
        when(rs.getString(11)).thenReturn("1111222233334444");
        when(rs.getDate(12)).thenReturn(Date.valueOf("2024-10-10"));
        when(rs.getInt(13)).thenReturn(2);
        when(rs.getDouble(14)).thenReturn(9.00);
        when(rs.getString(15)).thenReturn("active");
        when(rs.getInt(16)).thenReturn(4);
        when(rs.getString(17)).thenReturn("2024-10-10 12:12:12");
        when(rs.getDouble(18)).thenReturn(5.56);
        when(rs.getString(19)).thenReturn("prepared");
        when(rs.getInt(20)).thenReturn(1);

        Payment payment = dbM.getPaymentByID(1);

        assertNotNull(payment);

        verify(dbM, times(1)).getConnection();
        verify(con, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setInt(anyInt(), anyInt());
        verify(ps, times(1)).executeQuery();
        verify(rs, times(1)).next();
        verify(ps, times(1)).setInt(anyInt(), anyInt());
        verify(ps, times(1)).executeQuery();
        verify(rs, times(1)).next();
        verify(rs, times(7)).getInt(anyInt());
        verify(rs, times(8)).getString(anyInt());
        verify(rs, times(3)).getDouble(anyInt());
        verify(rs, times(2)).getDate(anyInt());
    }

    @Test
    public void getCardByNumber() throws SQLException {
        when(rs.next()).thenReturn(true);
        when(dbM.getCardByNumber(anyString())).thenCallRealMethod();
        when(rs.getInt(1)).thenReturn(1);
        when(rs.getString(2)).thenReturn("Название");
        when(rs.getString(3)).thenReturn("1234567899876543");
        when(rs.getDate(4)).thenReturn(Date.valueOf("2024-11-12"));
        when(rs.getInt(5)).thenReturn(1);
        when(rs.getDouble(6)).thenReturn(9000.00);
        when(rs.getString(7)).thenReturn("active");
        when(rs.getInt(8)).thenReturn(455);

        Card card = dbM.getCardByNumber("1234567899876543");
        assertNotNull(card);

        verify(dbM, times(1)).getConnection();
        verify(con, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setString(anyInt(), anyString());
        verify(ps, times(1)).executeQuery();
        verify(rs, times(1)).next();
        verify(rs, times(3)).getInt(anyInt());
        verify(rs, times(3)).getString(anyInt());
        verify(rs, times(1)).getDouble(anyInt());
        verify(rs, times(1)).getDate(anyInt());
    }

    @Test
    public void commitPayment() throws SQLException {
        when(dbM.commitPayment(any())).thenCallRealMethod();
        doNothing().when(con).commit();
        when(ps.executeUpdate()).thenReturn(1);

        Card card1 = new Card("название", "1999567899876543", Calendar.getInstance(), 1, 1000, Status.ACTIVE, 123);
        Card card2 = new Card("название2", "1111222233334444", Calendar.getInstance(), 1234, 123, Status.ACTIVE, 321);
        Payment payment = new Payment(card1, card2, Calendar.getInstance(), 100.50, PaymentStatus.PREPARED);
        boolean check = dbM.commitPayment(payment);
        assertTrue(check);

        verify(dbM, times(1)).getConnection();
        verify(con, times(1)).setAutoCommit(false);
        verify(con, times(3)).prepareStatement(anyString());
        verify(con, times(1)).setAutoCommit(false);
        verify(ps, times(3)).executeUpdate();
        verify(ps, times(2)).setDouble(anyInt(), anyDouble());
        verify(ps, times(3)).setInt(anyInt(), anyInt());
        verify(con, times(1)).commit();
    }

    @Test
    void preparePayment() throws SQLException {
        when(dbM.preparePayment(any())).thenCallRealMethod();
        when(ps.executeUpdate()).thenReturn(1);

        Card card1 = new Card("название", "1999567899876543", Calendar.getInstance(), 1, 1000, Status.ACTIVE, 123);
        Card card2 = new Card("название2", "1111222233334444", Calendar.getInstance(), 1234, 123, Status.ACTIVE, 321);
        Payment payment = new Payment(card1, card2, Calendar.getInstance(), 100.50, PaymentStatus.PREPARED);
        boolean check = dbM.preparePayment(payment);
        assertTrue(check);

        verify(dbM, times(1)).getConnection();
        verify(con, times(1)).prepareStatement(anyString());
        verify(ps, times(2)).setInt(anyInt(), anyInt());
        verify(ps, times(1)).setTimestamp(anyInt(), any());
        verify(ps, times(1)).setDouble(anyInt(), anyDouble());
        verify(ps, times(1)).setString(anyInt(), anyString());
        verify(ps, times(1)).executeUpdate();
    }

    @Test
    public void makePayment() throws SQLException {
        when(dbM.makePayment(any())).thenCallRealMethod();
        when(con.prepareStatement(anyString(), anyInt())).thenReturn(ps);
        when(ps.getGeneratedKeys()).thenReturn(rs);
        when(rs.getInt(1)).thenReturn(1);
        when(rs.next()).thenReturn(true);
        doNothing().when(con).commit();
        when(ps.executeUpdate()).thenReturn(1);

        Card card1 = new Card("название", "1999567899876543", Calendar.getInstance(), 1, 1000, Status.ACTIVE, 123);
        Card card2 = new Card("название2", "1111222233334444", Calendar.getInstance(), 1234, 123, Status.ACTIVE, 321);
        Payment payment = new Payment(card1, card2, Calendar.getInstance(), 100.50, PaymentStatus.PREPARED);
        int result = dbM.makePayment(payment);
        assertEquals(result, 1);

        verify(dbM, times(1)).getConnection();
        verify(con, times(1)).setAutoCommit(false);
        verify(con, times(2)).prepareStatement(anyString());
        verify(con, times(1)).prepareStatement(anyString(), anyInt());
        verify(con, times(1)).setAutoCommit(false);
        verify(ps, times(3)).executeUpdate();
        verify(rs, times(1)).next();
        verify(ps, times(3)).setDouble(anyInt(), anyDouble());
        verify(ps, times(4)).setInt(anyInt(), anyInt());
        verify(ps, times(1)).setTimestamp(anyInt(), any());
        verify(con, times(1)).commit();
    }

    @Test
    public void changeCardStatus() throws SQLException {
        when(dbM.changeCardStatus(anyInt(), any())).thenCallRealMethod();
        when(ps.executeUpdate()).thenReturn(1);


        boolean check = dbM.changeCardStatus(1, Status.BLOCKED);
        assertTrue(check);

        verify(dbM, times(1)).getConnection();
        verify(con, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setString(anyInt(), anyString());
        verify(ps, times(1)).setInt(anyInt(), anyInt());
        verify(ps, times(1)).executeUpdate();
    }

    @Test
    public void topUpCard() throws SQLException {
        when(dbM.topUpCard(any(), anyDouble())).thenCallRealMethod();
        when(ps.executeUpdate()).thenReturn(1);

        Card card1 = new Card("название", "1999567899876543", Calendar.getInstance(), 1, 1000, Status.ACTIVE, 123);
        boolean check = dbM.topUpCard(card1, 100);
        assertTrue(check);

        verify(dbM, times(1)).getConnection();
        verify(con, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setDouble(anyInt(), anyDouble());
        verify(ps, times(1)).setInt(anyInt(), anyInt());
        verify(ps, times(1)).executeUpdate();
    }

    @Test
    public void getAllUsers() throws SQLException, NoSuchAlgorithmException {
        s = mock(Statement.class);
        when(dbM.getAllUsers()).thenCallRealMethod();
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(con.createStatement()).thenReturn(s);
        when(s.executeQuery(anyString())).thenReturn(rs);
        when(rs.getInt(1)).thenReturn(1);
        when(rs.getString(2)).thenReturn("dmytro");
        when(rs.getString(3)).thenReturn("admin");
        when(rs.getString(4)).thenReturn(PasswordHash.hash("123456"));
        when(rs.getString(5)).thenReturn("dima.sigov1@gmail.com");
        when(rs.getString(6)).thenReturn("active");

        List<User> users = dbM.getAllUsers();
        assertNotNull(users);

        verify(dbM, times(1)).getConnection();
        verify(con, times(1)).createStatement();
        verify(s, times(1)).executeQuery(anyString());
        verify(rs, times(2)).next();
        verify(rs, times(1)).getInt(anyInt());
        verify(rs, times(5)).getString(anyInt());
    }

    @Test
    public void changeUserStatus() throws SQLException {
        when(dbM.changeUserStatus(anyInt(), any())).thenCallRealMethod();
        when(ps.executeUpdate()).thenReturn(1);

        boolean check = dbM.changeUserStatus(1, Status.BLOCKED);
        assertTrue(check);

        verify(dbM, times(1)).getConnection();
        verify(con, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setString(anyInt(), anyString());
        verify(ps, times(1)).setInt(anyInt(), anyInt());
        verify(ps, times(1)).executeUpdate();
    }

    @Test
    public void cancelPayment() throws SQLException {
        when(dbM.cancelPayment(any())).thenCallRealMethod();
        when(ps.executeUpdate()).thenReturn(1);

        Card card1 = new Card("название", "1999567899876543", Calendar.getInstance(), 1, 1000, Status.ACTIVE, 123);
        Card card2 = new Card("название2", "1111222233334444", Calendar.getInstance(), 1234, 123, Status.ACTIVE, 321);
        Payment payment = new Payment(card1, card2, Calendar.getInstance(), 100.50, PaymentStatus.PREPARED);
        boolean check = dbM.cancelPayment(payment);
        assertTrue(check);

        verify(dbM, times(1)).getConnection();
        verify(con, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setInt(anyInt(), anyInt());
        verify(ps, times(1)).executeUpdate();
    }

    @Test
    public void changeCardName() throws SQLException {
        when(dbM.changeCardName(any(), anyString())).thenCallRealMethod();
        when(ps.executeUpdate()).thenReturn(1);

        Card card1 = new Card("название", "1999567899876543", Calendar.getInstance(), 1, 1000, Status.ACTIVE, 123);
        boolean check = dbM.changeCardName(card1, "newName");
        assertTrue(check);

        verify(dbM, times(1)).getConnection();
        verify(con, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setInt(anyInt(), anyInt());
        verify(ps, times(1)).setString(anyInt(), anyString());
        verify(ps, times(1)).executeUpdate();
    }
}