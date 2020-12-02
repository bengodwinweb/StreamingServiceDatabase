package com.streamingservicebackend.model.movie;

import com.streamingservicebackend.database.DatabaseHandler;
import com.streamingservicebackend.database.ISqlHandler;
import com.streamingservicebackend.model.Dao;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MovieDAO implements Dao<Movie> {

    private final ISqlHandler _sqlHandler;

    public MovieDAO(ISqlHandler _sqlHandler) {
        this._sqlHandler = _sqlHandler;
    }

    @Override
    public Optional<Movie> get(String id) {
        String query = "SELECT * FROM " + DatabaseHandler.TABLE_MOVIE + " WHERE " + DatabaseHandler.COLUMN_MOVIE_ID + " = '" + id + "'";
        List<Movie> list = getListFromQuery(query);

        if (list.size() > 0) {
            Movie m = list.get(0);
            if (m != null) {
                return Optional.of(m);
            }
        }

        return Optional.empty();
    }

    /**
     * Call this with one or more service_id's to get a list of movies on that/those services
     * Ex: getAllFromStreamingService("DISN", "HULU") will return a list of all movies on either/both Disney+ or Hulu
     *
     * @param serviceIds ID of services to include in search
     * @return list of matching movies
     */
    public List<Movie> getAllFromStreamingService(String... serviceIds) {
        if (serviceIds.length == 0) {
            return new ArrayList<>();
        }

        List<String> whereConditions = new ArrayList<>();
        for (String service : serviceIds) {
            whereConditions.add(DatabaseHandler.TABLE_SERVICE_MOVIE + "." + DatabaseHandler.COLUMN_SERVICE_ID + " = '" + service + "'");
        }
        String whereClause = String.join(" OR ", whereConditions);

        String query = "SELECT * FROM " + DatabaseHandler.TABLE_MOVIE + " INNER JOIN " + DatabaseHandler.TABLE_SERVICE_MOVIE + " ON " + DatabaseHandler.TABLE_MOVIE + "." + DatabaseHandler.COLUMN_MOVIE_ID + " = " + DatabaseHandler.TABLE_SERVICE_MOVIE + "." + DatabaseHandler.COLUMN_MOVIE_ID + " WHERE " + whereClause;
        return getListFromQuery(query);
    }

    public List<Movie> getAllFromPerson(String personId) {
        if (personId == null) {
            return new ArrayList<>();
        }

        String actorQuery = "SELECT * FROM " + DatabaseHandler.TABLE_MOVIE + " INNER JOIN " + DatabaseHandler.TABLE_MOVIE_ACTOR + " ON " + DatabaseHandler.TABLE_MOVIE + "." + DatabaseHandler.COLUMN_MOVIE_ID + " = " + DatabaseHandler.TABLE_MOVIE_ACTOR + "." + DatabaseHandler.COLUMN_MOVIE_ID + " WHERE " + DatabaseHandler.TABLE_MOVIE_ACTOR + "." + DatabaseHandler.COLUMN_PERSON_ID + " = '" + personId + "'";
        String directorQuery = "SELECT * FROM " + DatabaseHandler.TABLE_MOVIE + " INNER JOIN " + DatabaseHandler.TABLE_MOVIE_DIRECTOR + " ON " + DatabaseHandler.TABLE_MOVIE + "." + DatabaseHandler.COLUMN_MOVIE_ID + " = " + DatabaseHandler.TABLE_MOVIE_DIRECTOR + "." + DatabaseHandler.COLUMN_MOVIE_ID + " WHERE " + DatabaseHandler.TABLE_MOVIE_DIRECTOR + "." + DatabaseHandler.COLUMN_PERSON_ID + " = '" + personId + "'";
        List<Movie> list = getListFromQuery(actorQuery);
        list.addAll(getListFromQuery(directorQuery));
        return list.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<Movie> getAll() {
        String sql = "SELECT * FROM " + DatabaseHandler.TABLE_MOVIE;
        return getListFromQuery(sql);
    }

    @Override
    public boolean add(Movie movie) {
        if (movie == null) {
            return false;
        }
        String sql = String.format("INSERT INTO " + DatabaseHandler.TABLE_SHOW + " VALUES ('%s', '%s', '%s', '%s', '%s', %d)", movie.getId(), movie.getName(), movie.getDescription(), _sqlHandler.formatDateString(movie.getReleaseDate()), movie.getGenre(), movie.getRunTimeMinutes());
        return _sqlHandler.executeSql(sql);
    }

    @Override
    public boolean update(Movie movie) {
        if (movie == null) {
            return false;
        }

        String template =
                "UPDATE " + DatabaseHandler.TABLE_MOVIE + " SET " +
                        DatabaseHandler.COLUMN_MOVIE_NAME + " = '%s' SET " +
                        DatabaseHandler.COLUMN_MOVIE_DESCRIPTION + " = '%s' SET " +
                        DatabaseHandler.COLUMN_MOVIE_DATE + " = '%s' SET " +
                        DatabaseHandler.COLUMN_MOVIE_GENRE + " = '%s' SET " +
                        DatabaseHandler.COLUMN_MOVIE_RUNTIME + " = %d WHERE " +
                        DatabaseHandler.COLUMN_MOVIE_ID + " = '%s'";

        String sql = String.format(template, movie.getName(), movie.getDescription(), _sqlHandler.formatDateString(movie.getReleaseDate()), movie.getGenre(), movie.getRunTimeMinutes());
        return _sqlHandler.executeSql(sql);
    }

    @Override
    public boolean delete(Movie movie) {
        if (movie == null) {
            return false;
        }

        String sql = "DELETE FROM " + DatabaseHandler.TABLE_MOVIE + " WHERE " + DatabaseHandler.COLUMN_MOVIE_ID + " = " + movie.getId();
        return _sqlHandler.executeSql(sql);
    }

    /**
     * Returns a Movie object created from the data at the current row in the ResultSet
     *
     * @param rs ResultSet with cursor placed at desired read position
     * @return Show generated with ResultSet values
     * @throws SQLException if ResultSet is malformed or closed
     */
    private Movie getFromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString(1);
        String name = rs.getString(2);
        String description;

        Clob descriptionClob = rs.getClob(3);
        StringBuffer buffer = new StringBuffer();
        try (Reader r = descriptionClob.getCharacterStream()) {
            int ch;
            while ((ch = r.read()) != -1) {
                buffer.append((char) ch);
            }
            description = buffer.toString();
        } catch (IOException e) {
            description = "Error: could not load description";
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        LocalDate date = rs.getDate(4).toLocalDate();
        String genre = rs.getString(5);
        int runtime = rs.getInt(6);
        return new Movie(id, name, description, date, runtime, genre);
    }

    /**
     * Makes a list of Movie objects from a ResultList returned from the given query
     *
     * @param query sql query to perform
     * @return list of shows from the ResultSet generated by the query
     */
    private List<Movie> getListFromQuery(String query) {
        Map<String, Movie> movies = new HashMap<>();
        try {
            ResultSet rs = _sqlHandler.executeQuery(query);
            while (rs.next()) {
                Movie m = getFromResultSet(rs);
                movies.put(m.getId(), m);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>(movies.values());
    }
}
