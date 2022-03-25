package databaseTutorial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MessageRepository {

    private static final String CREATE_TABLE_SQL = "CREATE TABLE TEST " +
            "(ID BIGINT AUTO_INCREMENT, " +
            "USERNAME VARCHAR(255), " +
            "MESSAGECOUNT INT DEFAULT 1)";
    private static final String CONNECTION_STRING_FILE = "jdbc:h2:./test_db";

    public static boolean createTable() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(CONNECTION_STRING_FILE);
            var statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS TEST");
            return statement.execute(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Author create(Author userName) {

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(CONNECTION_STRING_FILE);
            var statementToCreate = connection.prepareStatement("INSERT INTO TEST (USERNAME) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            statementToCreate.setString(1, userName.getUserName());

            // Creates user in database
            // first instance gets id 1 and auto-increments by one with every new instance
            // username is authors username - this is an issue since usernames are not unique
            // messagecount default 1 as author is put into database at first message.
            // author is first created, then saved in database and fields for id and messagecount are updated after creation with data from database

            statementToCreate.execute();

            var resultSetCreate = statementToCreate.getGeneratedKeys();
            if (resultSetCreate.next()) {
                userName.setId(resultSetCreate.getInt(1));
            }
            var statementToRead = connection.prepareStatement("SELECT ID, USERNAME, MESSAGECOUNT FROM TEST WHERE USERNAME = ?");
            statementToRead.setString(1, userName.getUserName());
            var resultSetRead = statementToRead.executeQuery();

            if (resultSetRead.next()) {
                userName = new Author(userName.getUserName());
                userName.setId(resultSetRead.getInt(1));
                userName.setUserName(resultSetRead.getString(2));
                userName.setMessageCount(resultSetRead.getInt(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userName;
    }

    public void update(Author testUserUpdate) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(CONNECTION_STRING_FILE);
            var statementToUpdate = connection.prepareStatement("UPDATE TEST SET MESSAGECOUNT = ? WHERE USERNAME = ?;");
            statementToUpdate.setInt(1, testUserUpdate.getMessageCount() + 1);
            statementToUpdate.setString(2, testUserUpdate.getUserName());
            statementToUpdate.executeUpdate();

            var statementToUpdateAuthor = connection.prepareStatement("SELECT ID, MESSAGECOUNT FROM TEST WHERE ID = ?");
            statementToUpdateAuthor.setInt(1, testUserUpdate.getId());
            var resultSet = statementToUpdateAuthor.executeQuery();
            if (resultSet.next()) {
                testUserUpdate.setId(resultSet.getInt(1));
                testUserUpdate.setMessageCount(resultSet.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void printAll () {
        Connection con = null;

        try {
            con = DriverManager.getConnection(CONNECTION_STRING_FILE);

            for (int i = 1; i <= 2; i++) {
                var stmt = con.prepareStatement("SELECT USERNAME, MESSAGECOUNT FROM TEST WHERE ID = ?");
                stmt.setInt(1, i);
                var results = stmt.executeQuery();
                if (results.next()) {
                    System.out.println("User id " + i + " is called " + results.getString(1) + " with messagecount " + results.getInt(2));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
