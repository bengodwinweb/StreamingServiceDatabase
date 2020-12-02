package com.streamingservicebackend.database;

import com.streamingservicebackend.server.Config;
import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DatabaseHandler implements ISqlHandler {

    public static final String TABLE_SERVICE = "streaming_service";
    public static final String COLUMN_SERVICE_ID = "service_id";
    public static final String COLUMN_SERVICE_NAME = "service_name";
    public static final String COLUMN_SERVICE_SUBSCRIPTION_PRICE = "subscription_price";
    public static final String COLUMN_SERVICE_TRIAL_PERIOD_DAYS = "trial_period_days";
    public static final String COLUMN_SERVICE_SHOWS_ADS = "shows_ads";

    public static final String TABLE_SHOW = "show";
    public static final String COLUMN_SHOW_ID = "show_id";
    public static final String COLUMN_SHOW_NAME = "show_name";
    public static final String COLUMN_SHOW_DESCRIPTION = "show_description";
    public static final String COLUMN_SHOW_DATE = "show_date";
    public static final String COLUMN_SHOW_GENRE = "show_genre";
    public static final String COLUMN_SHOW_NUM_SEASONS = "show_num_seasons";
    public static final String COLUMN_SHOW_NUM_EPISODES = "show_num_episodes";

    public static final String TABLE_MOVIE = "movie";
    public static final String COLUMN_MOVIE_ID = "movie_id";
    public static final String COLUMN_MOVIE_NAME = "movie_name";
    public static final String COLUMN_MOVIE_DESCRIPTION = "movie_description";
    public static final String COLUMN_MOVIE_DATE = "movie_date";
    public static final String COLUMN_MOVIE_GENRE = "movie_genre";
    public static final String COLUMN_MOVIE_RUNTIME = "movie_runtime";

    public static final String TABLE_PERSON = "person";
    public static final String COLUMN_PERSON_ID = "person_id";
    public static final String COLUMN_PERSON_FNAME = "person_fname";
    public static final String COLUMN_PERSON_MNAME = "person_mname";
    public static final String COLUMN_PERSON_LNAME = "person_lname";
    public static final String COLUMN_PERSON_STAGE_NAME = "person_stagename";
    public static final String COLUMN_PERSON_DOB = "person_dob";

    public static final String TABLE_SERVICE_SHOW = "service_show";
    public static final String TABLE_SERVICE_MOVIE = "service_movie";
    public static final String COLUMN_RENTAL_PRICE = "movie_rental_price";
    public static final String TABLE_SHOW_DIRECTOR = "show_director";
    public static final String TABLE_MOVIE_DIRECTOR = "movie_director";
    public static final String TABLE_SHOW_ACTOR = "show_actor";
    public static final String TABLE_MOVIE_ACTOR = "movie_actor";

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    private static Connection _conn;

    /**
     * Creates the connection object with the config parameters provided in src/main/resources/dbconfig.properties
     */
    private static void createConnection() throws SQLException {
        OracleDataSource ds = new OracleDataSource();
        ds.setURL(Config.DB_URL);
        _conn = ds.getConnection(Config.DB_USER_ID, Config.DB_PASSWORD);
    }

    @Override
    public ResultSet executeQuery(String query) {
        try {
            if (_conn == null || _conn.isClosed()) {
                createConnection();
            }
            return _conn.createStatement().executeQuery(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean executeSql(String sql) {
        boolean success = false;
        try {
            success = execute(sql);
            commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        if (_conn == null || _conn.isClosed()) {
            createConnection();
        }
        return _conn.createStatement().execute(sql);
    }

    @Override
    public void commit() {
        try {
            execute("COMMIT");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String formatDateString(LocalDate date) {
        return df.format(date);
    }
}
