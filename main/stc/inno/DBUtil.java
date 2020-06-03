package stc.inno;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    private DBUtil() {
    }

    public static void renewDatabase(Connection connection) throws SQLException {

        try (Statement statement = connection.createStatement();
        ) {
            statement.execute("-- Database: jdbcDB\n"
                              + "DROP TABLE IF EXISTS mobile;"
                              + "\n"
                              + "CREATE TABLE mobile (\n"
                              + "    id bigserial primary key,\n"
                              + "    model varchar(100) NOT NULL,\n"
                              + "    price integer NOT NULL,\n"
                              + "    manufacturer varchar(100) NOT NULL);"
                              + "\n"
                              + "INSERT INTO mobile (model, price, manufacturer)\n"
                              + "VALUES\n"
                              + "   ('P1', 100, 'Xiaomi'),\n"
                              + "   ('EDGE', 1, 'Micro'),\n"
                              + "   ('FRY1', 1001, 'Apple'),\n"
                              + "   ('FRY1', 1002, 'Apple'),\n"
                              + "   ('OGO', 10000, 'SAMSUNG');"
                              + "\n"
                              + "DROP TABLE IF EXISTS mobile;"
                              + "\n"
                              + "CREATE TABLE mobile (\n"
                              + "    id bigserial primary key,\n"
                              + "    model varchar(100) NOT NULL,\n"
                              + "    price integer NOT NULL,\n"
                              + "    manufacturer varchar(100) NOT NULL);"
                              + "\n"
                              + "INSERT INTO mobile (model, price, manufacturer)\n"
                              + "VALUES\n"
                              + "   ('P1', 100, 'Xiaomi'),\n"
                              + "   ('EDGE', 1, 'Micro'),\n"
                              + "   ('FRY1', 1001, 'Apple'),\n"
                              + "   ('FRY1', 1002, 'Apple'),\n"
                              + "   ('OGO', 10000, 'SAMSUNG');"
                              + "\n"
                              + "DROP PROCEDURE IF EXISTS insert_data(integer);"
                              + "\n"
                              + "CREATE OR REPLACE PROCEDURE insert_data(a integer)\n"
                              + "    LANGUAGE SQL\n"
                              + "AS\n"
                              + "    $$\n"
                              + "    UPDATE mobile SET price = price + 1 WHERE id = a\n"
                              + "$$;"
                              + "DROP FUNCTION IF EXISTS multiply(integer);"
                              + "\n"
                              + "CREATE OR REPLACE FUNCTION multiply(a INT) RETURNS INT AS $$\n"
                              + "BEGIN\n"
                              + "    RETURN a * 2;\n"
                              + "END\n"
                              + "$$ LANGUAGE 'plpgsql';");
        }
    }
}
