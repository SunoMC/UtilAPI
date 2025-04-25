package net.sunomc.api.sql;

import net.sunomc.api.logger.Logger;

import java.sql.*;

/**
 * A thread-safe utility class for managing a single SQL database connection.
 * <p>
 * Credentials must be provided during initialization. The connection is lazily
 * established upon the first call to {@link #getConnection()}.
 * </p>
 *
 * @implNote This class is designed for reuse in Minecraft plugins or standalone apps.
 * @since 1.0.0
 */
public final class SQL {
    private static Connection connection = null;
    private static String url;
    private static String user;
    private static String password;

    /**
     * Initializes the SQL connection credentials.
     *
     * @param url      JDBC connection URL (e.g., "jdbc:mysql://localhost:3306/db").
     * @param user     Database username.
     * @param password Database password.
     * @throws IllegalArgumentException If any credential is null or empty.
     */
    public static void initialize(String url, String user, String password) {
        if (url == null || url.trim().isEmpty() ||
                user == null || user.trim().isEmpty() ||
                password == null) {
            throw new IllegalArgumentException("Database credentials cannot be null or empty.");
        }
        SQL.url = url;
        SQL.user = user;
        SQL.password = password;
    }

    /**
     * Returns the active database connection. If none exists, a new connection is established.
     *
     * @return The active {@link Connection} object.
     * @throws IllegalStateException If {@link #initialize(String url, String user, String password)} was not called first,
     *                               or if the connection fails.
     */
    public static Connection getConnection() {
        if (url == null) {
            throw new IllegalStateException("SQL.initialize() must be called first.");
        }

        if (connection == null) {
            try {
                connection = DriverManager.getConnection(url, user, password);
                Logger.info("Connection to the DataBase was successfully.");
            } catch (SQLException e) {
                Logger.error("Connection to the DataBase failed: \" + e.getMessage()");
                throw new IllegalStateException("Failed to connect to the database.", e);
            }
        }
        return connection;
    }

    /**
     * Closes the active connection (if it exists).
     * <p>
     * This should be called during plugin shutdown or when the connection is no longer needed.
     * </p>
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                Logger.info("Connection to the DataBase was closed successfully.");
            } catch (SQLException e) {
                Logger.warn("Failed to close connection to the DataBase: " + e.getMessage());
            }
        }
    }

    /**
     * Creates a {@link Statement} object for executing SQL queries.
     *
     * @return A new {@link Statement} instance.
     * @throws SQLException If delegation to {@link Connection#createStatement()} fails.
     * @see Connection#createStatement()
     */
    public static Statement createStatement() throws SQLException {
        return getConnection().createStatement();
    }

    /**
     * Creates a {@link PreparedStatement} for parameterized SQL queries.
     *
     * @param sql The SQL query with optional placeholders (`?`).
     * @return A new {@link PreparedStatement} instance.
     * @throws SQLException If delegation to {@link Connection#prepareStatement(String)} fails.
     * @see Connection#prepareStatement(String)
     */
    public static PreparedStatement prepareStatement(String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }

    /**
     * Checks if the connection is valid (or establishes it if null).
     *
     * @return {@code true} if the connection is alive, {@code false} otherwise.
     * @throws SQLException If delegation to {@link Connection#isValid(int)} fails.
     * @see Connection#isValid(int)
     */
    public static boolean isValid() throws SQLException {
        return getConnection().isValid(5); // 5-second timeout
    }

    /**
     * Starts a transaction by disabling auto-commit mode.
     *
     * @throws SQLException If delegation to {@link Connection#setAutoCommit(boolean)} fails.
     * @see Connection#setAutoCommit(boolean)
     */
    public static void beginTransaction() throws SQLException {
        getConnection().setAutoCommit(false);
    }

    /**
     * Commits the current transaction.
     *
     * @throws SQLException If delegation to {@link Connection#commit()} fails.
     * @see Connection#commit()
     */
    public static void commitTransaction() throws SQLException {
        getConnection().commit();
        getConnection().setAutoCommit(true);
    }

    /**
     * Rolls back the current transaction.
     *
     * @throws SQLException If delegation to {@link Connection#rollback()} fails.
     * @see Connection#rollback()
     */
    public static void rollbackTransaction() throws SQLException {
        getConnection().rollback();
        getConnection().setAutoCommit(true);
    }
}