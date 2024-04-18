package com.simtechdata;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class SQLite {

    private static final File sqliteFile = Paths.get(System.getProperty("user.home"), "test", "database.sqlite").toFile();
    private static final String databaseName = "TestDatabase";
    private static final String connString = STR."jdbc:sqlite:\{sqliteFile.getAbsolutePath()}";


    public SQLite() {
        if (!sqliteFile.exists()) {
            try {
                cleanFile();
            }
            catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public Connection getConn() {
        try {
            Connection conn = DriverManager.getConnection(connString);
            if (conn == null) {
                System.out.println("Connection failed");
                System.exit(0);
            }
            return conn;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanFile() throws SQLException, IOException {
        if (!sqliteFile.exists()) {
            Files.createDirectories(sqliteFile.getParentFile().toPath());
        }
        else {
            Files.delete(sqliteFile.toPath());
        }
        DriverManager.getConnection(connString);
        if (checkFile()) {
            final Connection conn = getConn();
            conn.createStatement().executeUpdate(getSchema());
            System.out.println("Database created");
        }
        else {
            throw new RuntimeException("Database could not be created, check folder permissions and try again");
        }
    }

    private boolean checkFile() {
        boolean success = true;
        try {
            Connection conn = getConn();
            conn.setAutoCommit(true);
            conn.setSchema(databaseName);
        }
        catch (SQLException ignored) {
            success = false;
        }
        return success;
    }

    private String getSchema() {
        return """
                CREATE TABLE "Test" (
                  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                  "Item1" TEXT NOT NULL ON CONFLICT IGNORE DEFAULT ''
                );
                """;
    }

    public void addRecord() {
        String SQL = "INSERT INTO Test (Item1) VALUES (?)";
        try (Connection conn = getConn();
             PreparedStatement pst = conn.prepareStatement(SQL)) {
            pst.setString(1, "Test Data");
            pst.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getRecord() {
        String SQL = "SELECT * FROM Test;";
        try (Connection conn = getConn();
             ResultSet rs = conn.createStatement().executeQuery(SQL)) {
            while (rs.next()) {
                System.out.println(rs.getString("Item1"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        String SQL = "DELETE FROM Test WHERE id > 0;";
        try (Connection conn = getConn()) {
            conn.createStatement().executeUpdate(SQL);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
