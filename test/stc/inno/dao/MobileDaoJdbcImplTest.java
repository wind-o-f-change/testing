package stc.inno.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import stc.inno.ConnectionManager.ConnectionManager;
import stc.inno.ConnectionManager.ConnectionManagerJdbcImpl;
import stc.inno.TestResultLoggerExtension;
import stc.inno.pojo.Mobile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(TestResultLoggerExtension.class)
class MobileDaoJdbcImplTest {

    private MobileDao         mobileDao;
    private ConnectionManager connectionManager;
    private Connection        connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet         resultSetMock;

    @BeforeEach
    void setUp() throws SQLException {
        initMocks(this);
        connectionManager = spy(ConnectionManagerJdbcImpl.getInstance());
        connection        = mock(Connection.class);
        mobileDao         = spy(new MobileDaoJdbcImpl(connectionManager));
    }

    @Test
    void addMobile() throws SQLException {
        when(connectionManager.getConnection()).thenReturn(connection);
        doReturn(preparedStatement).when(connection).prepareStatement(MobileDaoJdbcImpl.INSERT_INTO_MOBILE, Statement.RETURN_GENERATED_KEYS);
        doReturn(resultSetMock).when(preparedStatement).getGeneratedKeys();
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getLong(1)).thenReturn(1L);
        int    id         = 1;
        String model      = "REAL";
        int    price      = 100000;
        String manufactur = "GOOGLE";
        Mobile mobile     = new Mobile(id, model, price, manufactur);

        Long result = mobileDao.addMobile(mobile);

        verify(connectionManager, times(1)).getConnection();
        verify(connection, atMost(1)).prepareStatement(MobileDaoJdbcImpl.INSERT_INTO_MOBILE);
        verify(preparedStatement, times(1)).setString(1, model);
        verify(preparedStatement, times(1)).setInt(2, price);
        verify(preparedStatement, times(1)).setString(3, manufactur);
        verify(preparedStatement, times(1)).executeUpdate();
        assertAll("assert all",
                  () -> assertEquals(1L, result),
                  () -> assertNotEquals(1L, result)
        );
    }

    @Test
    void addMobileWithSqlException() throws SQLException {
        when(connectionManager.getConnection()).thenReturn(connection);
        doReturn(preparedStatement).when(connection).prepareStatement(MobileDaoJdbcImpl.INSERT_INTO_MOBILE, Statement.RETURN_GENERATED_KEYS);
        doThrow(new SQLException("HELLO!")).when(preparedStatement).executeUpdate();
        int    id         = 1;
        String model      = "REAL";
        int    price      = 100000;
        String manufactur = "GOOGLE";
        Mobile mobile     = new Mobile(id, model, price, manufactur);

        Long result = assertDoesNotThrow(() -> mobileDao.addMobile(mobile));

        verify(connectionManager, times(1)).getConnection();
        verify(connection, times(1)).prepareStatement(MobileDaoJdbcImpl.INSERT_INTO_MOBILE, Statement.RETURN_GENERATED_KEYS);
        verify(preparedStatement, atMost(2)).setString(anyInt(), anyString());
        verify(preparedStatement, times(1)).setInt(2, price);
        verify(preparedStatement, never()).executeQuery();
        verify(preparedStatement, times(1)).executeUpdate();
        assertEquals(0L, result);
    }


    @Test
    void test1() {
        int result = connectionManager.get15();
        assertEquals(15, result);
    }

    @Test
    void test2() {
        when(connectionManager.get15()).thenAnswer(invocationOnMock -> ((int) invocationOnMock.callRealMethod()) + 5);

        int result = connectionManager.get15();

        assertEquals(20, result);
    }

}