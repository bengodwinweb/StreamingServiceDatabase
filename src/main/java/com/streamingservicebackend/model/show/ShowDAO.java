package com.streamingservicebackend.model.show;

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

public class ShowDAO implements Dao<Show> {

    private final ISqlHandler _sqlHandler;

    public ShowDAO(ISqlHandler sqlHandler) {
        this._sqlHandler = sqlHandler;
    }

    @Override
    public Optional<Show> get(String id) {
        String query = "SELECT * FROM " + DatabaseHandler.TABLE_SHOW + " WHERE " + DatabaseHandler.COLUMN_SHOW_ID + " = '" + id + "'";
        List<Show> list = getListFromQuery(query);

        if (list.size() > 0) {
            Show s = list.get(0);
            if (s != null) {
                return Optional.of(s);
            }
        }

        return Optional.empty();
    }

    /**
     * Call this with one or more service_id's to get a list of shows on that/those services
     * Ex: getAllFromStreamingService("DISN", "HULU") will return a list of all shows on either/both Disney+ or Hulu
     *
     * @param serviceIds ID of services to include in search
     * @return list of matching shows
     */
    public List<Show> getAllFromStreamingService(String... serviceIds) {
        if (serviceIds.length == 0) {
            return new ArrayList<>();
        }

        List<String> whereConditions = new ArrayList<>();
        for (String service : serviceIds) {
            whereConditions.add(DatabaseHandler.TABLE_SERVICE_SHOW + "." + DatabaseHandler.COLUMN_SERVICE_ID + " = '" + service + "'");
        }
        String whereClause = String.join(" OR ", whereConditions);

        String query = "SELECT * FROM " + DatabaseHandler.TABLE_SHOW + " INNER JOIN " + DatabaseHandler.TABLE_SERVICE_SHOW + " ON " + DatabaseHandler.TABLE_SHOW + "." + DatabaseHandler.COLUMN_SHOW_ID + " = " + DatabaseHandler.TABLE_SERVICE_SHOW + "." + DatabaseHandler.COLUMN_SHOW_ID + " WHERE " + whereClause;
        return getListFromQuery(query);
    }

    /**
     * Call this with the id of a person to get a list of shows that person acted in and/or directed
     *
     * @param personId ID of person to use in search
     * @return list of shows where this person is listed as an actor or director
     */
    public List<Show> getAllFromPerson(String personId) {
        if (personId == null) {
            return new ArrayList<>();
        }

        String actorQuery = "SELECT * FROM " + DatabaseHandler.TABLE_SHOW + " INNER JOIN " + DatabaseHandler.TABLE_SHOW_ACTOR + " ON " + DatabaseHandler.TABLE_SHOW + "." + DatabaseHandler.COLUMN_SHOW_ID + " = " + DatabaseHandler.TABLE_SHOW_ACTOR + "." + DatabaseHandler.COLUMN_SHOW_ID + " WHERE " + DatabaseHandler.TABLE_SHOW_ACTOR + "." + DatabaseHandler.COLUMN_PERSON_ID + " = '" + personId + "'";
        String directorQuery = "SELECT * FROM " + DatabaseHandler.TABLE_SHOW + " INNER JOIN " + DatabaseHandler.TABLE_SHOW_DIRECTOR + " ON " + DatabaseHandler.TABLE_SHOW + "." + DatabaseHandler.COLUMN_SHOW_ID + " = " + DatabaseHandler.TABLE_SHOW_DIRECTOR + "." + DatabaseHandler.COLUMN_SHOW_ID + " WHERE " + DatabaseHandler.TABLE_SHOW_DIRECTOR + "." + DatabaseHandler.COLUMN_PERSON_ID + " = '" + personId + "'";
        List<Show> list = getListFromQuery(actorQuery);
        list.addAll(getListFromQuery(directorQuery));
        return list.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<Show> getAll() {
        String query = "SELECT * FROM " + DatabaseHandler.TABLE_SHOW;
        return getListFromQuery(query);
    }

    @Override
    public boolean add(Show show) {
        if (show == null) {
            return false;
        }

        String sql = String.format("INSERT INTO " + DatabaseHandler.TABLE_SHOW + " VALUES ('%s', '%s', '%s', '%s', '%s', %s, %s)", show.getId(), show.getName(), show.getDescription(), _sqlHandler.formatDateString(show.getReleaseDate()), show.getGenre(), show.getNumSeasons(), show.getNumEpisodes());
        return _sqlHandler.executeSql(sql);
    }

    @Override
    public boolean update(Show show) {
        if (show == null) {
            return false;
        }

        String template =
                "UPDATE " + DatabaseHandler.TABLE_SHOW + " SET " +
                        DatabaseHandler.COLUMN_SHOW_NAME + " = '%s', " +
                        DatabaseHandler.COLUMN_SHOW_DESCRIPTION + " = '%s', " +
                        DatabaseHandler.COLUMN_SHOW_DATE + " = '%s', " +
                        DatabaseHandler.COLUMN_SHOW_GENRE + " = '%s', " +
                        DatabaseHandler.COLUMN_SHOW_NUM_SEASONS + " = %d, " +
                        DatabaseHandler.COLUMN_SHOW_NUM_EPISODES + " = %d WHERE " +
                        DatabaseHandler.COLUMN_SHOW_ID + " = '%s'";

        String sql = String.format(template, show.getName(), show.getDescription(), _sqlHandler.formatDateString(show.getReleaseDate()), show.getGenre(), show.getNumSeasons(), show.getNumEpisodes(), show.getId());
        return _sqlHandler.executeSql(sql);
    }

    @Override
    public boolean delete(Show show) {
        if (show == null) {
            return false;
        }

        String sql = "DELETE FROM " + DatabaseHandler.TABLE_SHOW + " WHERE " + DatabaseHandler.COLUMN_SHOW_ID + " = '" + show.getId() + "'";
        return _sqlHandler.executeSql(sql);
    }

    public List<Show> getMinEpisodes() {
        String query = "SELECT * FROM " + DatabaseHandler.TABLE_SHOW + " WHERE " +
                        DatabaseHandler.COLUMN_SHOW_NUM_SEASONS + " * " + DatabaseHandler.COLUMN_SHOW_NUM_EPISODES +
                        " = (SELECT MIN(" + DatabaseHandler.COLUMN_SHOW_NUM_SEASONS + " * " + DatabaseHandler.COLUMN_SHOW_NUM_EPISODES +
                        ") FROM " + DatabaseHandler.TABLE_SHOW + ")";
        return getListFromQuery(query);
    }

    public List<Show> getMaxEpisodes() {
        String query = "SELECT * FROM " + DatabaseHandler.TABLE_SHOW + " WHERE " +
                DatabaseHandler.COLUMN_SHOW_NUM_SEASONS + " * " + DatabaseHandler.COLUMN_SHOW_NUM_EPISODES +
                " = (SELECT MAX(" + DatabaseHandler.COLUMN_SHOW_NUM_SEASONS + " * " + DatabaseHandler.COLUMN_SHOW_NUM_EPISODES +
                ") FROM " + DatabaseHandler.TABLE_SHOW + ")";
        return getListFromQuery(query);
    }

    /**
     * Returns a Show object created from the data at the current row in the ResultSet
     *
     * @param rs ResultSet with cursor placed at desired read position
     * @return Show generated with ResultSet values
     * @throws SQLException if ResultSet is malformed or closed
     */
    private Show getFromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString(1);
        String name = rs.getString(2);
        String description;

        Clob descriptionClob = rs.getClob(3);
        StringBuffer buffer = new StringBuffer();
        try (Reader reader = descriptionClob.getCharacterStream()) {
            int ch;
            while ((ch = reader.read()) != -1) {
                buffer.append((char) ch);
            }
            description = buffer.toString();
        } catch (IOException e) {
            description = "Error: could not load description";
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        LocalDate firstShowDate = rs.getDate(4).toLocalDate();
        String genre = rs.getString(5);
        int seasons = rs.getInt(6);
        int episodes = rs.getInt(7);
        return new Show(id, name, description, firstShowDate, seasons, episodes, genre);
    }

    /**
     * Makes a list of Show objects from a ResultList returned from the given query
     *
     * @param query sql query to perform
     * @return list of shows from the ResultSet generated by the query
     */
    private List<Show> getListFromQuery(String query) {
        Map<String, Show> shows = new HashMap<String, Show>();
        try {
            ResultSet rs = _sqlHandler.executeQuery(query);
            while (rs != null && rs.next()) {
                Show show = getFromResultSet(rs);
                shows.put(show.getId(), show);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>(shows.values());
    }
}
