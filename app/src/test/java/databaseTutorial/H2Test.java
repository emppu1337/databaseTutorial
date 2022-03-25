package databaseTutorial;

import org.junit.Test;

import java.sql.*;

public class H2Test {

    private static final String connectionStringIM = "jdbc:h2:mem:";
    private static final String connectionStringFile = "jdbc:h2:~/db";
    @Test
    public void shouldRun() throws Exception {
        var con = DriverManager.getConnection(connectionStringIM);
        var stmt = con.createStatement();
        var rs  = stmt.executeQuery("SELECT 1 + 1");
        if(rs.next()) {
            System.out.println(rs.getInt(1));
            System.out.println("End of test \"shouldRun\"");
        }
    }
    @Test
    public void shouldInsertInDb() throws Exception {
        var con = DriverManager.getConnection(connectionStringIM);
        var stmt = con.createStatement();
        stmt.execute("create table animal(id bigint auto_increment, name varchar(255))");
        stmt.execute("insert into animal(name) values('kitty')");
        stmt.execute("insert into animal(name) values('missan')");
        stmt.execute("insert into animal(name) values('doggy')");

        var rs = stmt.executeQuery("SELECT id, name from animal");
        while(rs.next()) {
            System.out.print(rs.getInt(1));
            System.out.print("\t");
            System.out.println(rs.getString(2));
        }
        System.out.println("end of test \"shouldInsertInDb\"");
    }

    @Test
    public void shouldCreateTablesInDatabaseHandlingExceptions() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(connectionStringIM);
            stmt = con.createStatement();
            stmt.execute("create table animal(id bigint auto_increment, name varchar(255))");
            stmt.execute("insert into animal(name) values('kitty')");
            stmt.execute("insert into animal(name) values('missan')");
            stmt.execute("insert into animal(name) values('doggy')");

            rs = stmt.executeQuery("SELECT id, name from animal");
            while(rs.next()) {
                System.out.print(rs.getInt(1));
                System.out.print("\t");
                System.out.println(rs.getString(2));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                rs.close();
            }
            catch (SQLException ignore) {
            }
            try {
                stmt.close();
            }
            catch (SQLException ignore) {
            }
            try {
                con.close();
            }
            catch (SQLException ignore) {
            }
        }
    }
}
