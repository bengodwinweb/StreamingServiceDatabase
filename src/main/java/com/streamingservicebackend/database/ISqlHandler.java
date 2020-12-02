package com.streamingservicebackend.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public interface ISqlHandler {

    /**
     * Runs the given query against the db and returns the generated ResultSet
     *
     * @param sql sql query to execute
     * @return ResultSet returned by the db
     * @throws SQLException if there is an issue accessing the db or the query is malformed
     */
    ResultSet executeQuery(String sql) throws SQLException;

    /**
     * Executes the given sql command on the db.
     * Use executeSql() instead of this method unless you specifically want to handle any exceptions
     *
     * @param sql command to execute
     * @return true if command was run successfully
     * @throws SQLException if there is an issue accessing the db or the command is malformed
     */
    boolean execute(String sql) throws SQLException;

    /**
     * Executes the given sql command on the db and hides any exception thrown
     *
     * @param sql command to execute
     * @return true if command was run successfully and no exceptions were generated
     */
    boolean executeSql(String sql);

    /**
     * Executes a commit command on the db
     */
    void commit();

    /**
     * Returns a database-formatted string from a LocalDate
     *
     * @param date
     * @return
     */
    String formatDateString(LocalDate date);
}
