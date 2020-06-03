package stc.inno;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stc.inno.ConnectionManager.ConnectionManager;
import stc.inno.dao.MobileDao;
import stc.inno.dao.MobileDaoJdbcImpl;
import stc.inno.mocks.ConnectionManagerMock;

import java.sql.SQLException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class MainTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainTest.class);

    private Main              main;
    private MobileDao         mobileDao;
    private ConnectionManager connectionManager;

    @BeforeEach
    void setUp() throws SQLException {
        LOGGER.trace("BeforeEach in MainTest");
        main              = new Main();
        connectionManager = new ConnectionManagerMock();
        mobileDao         = new MobileDaoJdbcImpl(connectionManager);
    }

    @BeforeAll
    static void tearDownAll() {
        LOGGER.trace("BeforeAll in MainTest");
    }

    @AfterAll
    static void setUpAll() {
        LOGGER.trace("AfterAll in MainTest");
    }

    @AfterEach
    void tearDown() {
        LOGGER.trace("AfterEach in MainTest");
    }

    @Test
    @DisplayName("ТЕСТ MAIN МЕТОДА, КОГДА ВСЁ ОК!")
    void main() {
        assumeTrue(main != null);
        assertDoesNotThrow(() -> main.method1(mobileDao));
    }

    @Test
    @Disabled
    void disabledTest() {
        //
    }

    @Test
    void mainWithException() {
        NullPointerException nullPointerException = assertThrows(NullPointerException.class, () -> main.method1(null));
        assertNull(nullPointerException.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
                "1, 1, 1",
                "2, 3, 6"
        })
    void getPriceTestWithValueSource(int oldPrice, int multiply, int result) {
        int price = main.getPrice(oldPrice, multiply);
        assertEquals(result, price);
    }

    @ParameterizedTest(name = "{index} / {0} * {1} = {2}")
    @MethodSource("getPriceTestArgumentProvider")
    void getPriceTestWithMethodSource(ArgumentsAccessor argumentsAccessor) {
        int oldPrice = argumentsAccessor.getInteger(0);
        int multiply = argumentsAccessor.get(1, Integer.class);
        int result = (int) argumentsAccessor.get(2);
        int price = main.getPrice(oldPrice, multiply);
        assertEquals(result, price);
    }

    @Disabled
    @ParameterizedTest
    @ValueSource(strings = {"A", "B", "C"})
    //@ArgumentsSource(CustomArgumentProvider.class)
    //@CsvSource({
        //            "1, 1, 1",
        //            "2, 3, 6"
        //    })
    //@CsvFileSource(resources = "/data.csv", numLinesToSkip = 1)
    void parametrizedTest(ArgumentsAccessor argumentsAccessor) {
        String s1 = argumentsAccessor.getString(0);
        assertEquals("A", s1);
    }

    private static Stream<Arguments> getPriceTestArgumentProvider() {
        return Stream.of(
                Arguments.of(1, 1, 1),
                Arguments.of(2, 3, 6)
        );
    }

}