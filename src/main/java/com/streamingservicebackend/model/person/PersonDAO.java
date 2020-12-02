package com.streamingservicebackend.model.person;

import com.streamingservicebackend.database.DatabaseHandler;
import com.streamingservicebackend.database.ISqlHandler;
import com.streamingservicebackend.model.BaseMedia;
import com.streamingservicebackend.model.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class PersonDAO implements Dao<Person> {

    private final ISqlHandler _sqlHandler;

    public PersonDAO(ISqlHandler _sqlHandler) {
        this._sqlHandler = _sqlHandler;
    }

    @Override
    public Optional<Person> get(String id) {
        String query = "SELECT * FROM " + DatabaseHandler.TABLE_PERSON + " WHERE " + DatabaseHandler.COLUMN_PERSON_ID + " = '" + id + "'";
        List<Person> list = getListFromQuery(query);

        if (list.size() > 0) {
            Person p = list.get(0);
            if (p != null) {
                return Optional.of(p);
            }
        }

        return Optional.empty();
    }

    public List<Person> getAllFromMedia(String mediaId) {
        if (mediaId == null) {
            return new ArrayList<>();
        }

        List<Person> people = getDirectorsFromMedia(mediaId);
        people.addAll(getActorsFromMedia(mediaId));
        return people.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Returns a list of directors for the given Media object
     *
     * @param media
     * @return
     */
    public List<Person> getDirectorsFromMedia(BaseMedia media) {
        if (media == null) {
            return new ArrayList<>();
        }

        return getDirectorsFromMedia(media.getId());
    }

    public List<Person> getDirectorsFromMedia(String mediaId) {
        List<Person> directors = new ArrayList<>();

        if (mediaId != null) {
            // 1 =  Spine Table Name, 2 = Media Id Column, 3 = Media ID
            String sqlTemplate = "SELECT * FROM " + DatabaseHandler.TABLE_PERSON + " INNER JOIN %1$s ON " + DatabaseHandler.TABLE_PERSON + "." + DatabaseHandler.COLUMN_PERSON_ID +
                    " = %1$s." + DatabaseHandler.COLUMN_PERSON_ID + " WHERE %1$s.%2$s = '%3$s'";
            String showQuery = String.format(sqlTemplate, DatabaseHandler.TABLE_SHOW_DIRECTOR, DatabaseHandler.COLUMN_SHOW_ID, mediaId);
            String movieQuery = String.format(sqlTemplate, DatabaseHandler.TABLE_MOVIE_DIRECTOR, DatabaseHandler.COLUMN_MOVIE_ID, mediaId);

            directors = getListFromQuery(showQuery + " UNION " + movieQuery).stream().distinct().collect(Collectors.toList());
        }

        return directors;
    }

    /**
     * Returns a list of actresses/actors for the given Media object
     *
     * @param media
     * @return
     */
    public List<Person> getActorsFromMedia(BaseMedia media) {
        if (media == null) {
            return new ArrayList<>();
        }

        return getActorsFromMedia(media.getId());
    }

    public List<Person> getActorsFromMedia(String mediaId) {
        List<Person> actors = new ArrayList<>();

        if (mediaId != null) {
            // 1 =  Spine Table Name, 2 = Media Id Column, 3 = Media ID
            String sqlTemplate = "SELECT * FROM " + DatabaseHandler.TABLE_PERSON + " INNER JOIN %1$s ON " + DatabaseHandler.TABLE_PERSON + "." + DatabaseHandler.COLUMN_PERSON_ID +
                    " = %1$s." + DatabaseHandler.COLUMN_PERSON_ID + " WHERE %1$s.%2$s = '%3$s'";
            String showQuery = String.format(sqlTemplate, DatabaseHandler.TABLE_SHOW_ACTOR, DatabaseHandler.COLUMN_SHOW_ID, mediaId);
            String movieQuery = String.format(sqlTemplate, DatabaseHandler.TABLE_MOVIE_ACTOR, DatabaseHandler.COLUMN_MOVIE_ID, mediaId);

            actors = getListFromQuery(showQuery + " UNION " + movieQuery).stream().distinct().collect(Collectors.toList());
        }

        return actors;
    }


    @Override
    public List<Person> getAll() {
        String query = "SELECT * FROM " + DatabaseHandler.TABLE_PERSON;
        return getListFromQuery(query);
    }

    @Override
    public boolean add(Person person) {
        if (person == null) {
            return false;
        }

        String template = "INSERT INTO " + DatabaseHandler.TABLE_PERSON +
                " VALUES ('%s', '%s', '%s', '%s', '%s', '%s')";

        String sql = String.format(template, person.getPersonId(), person.getFirstName(), person.getMiddleName(), person.getLastName(), person.getStageName(), _sqlHandler.formatDateString(person.getDateOfBirth()));
        return _sqlHandler.executeSql(sql);
    }

    @Override
    public boolean update(Person person) {
        if (person == null) {
            return false;
        }

        String template = "UPDATE " + DatabaseHandler.TABLE_PERSON + " SET " +
                DatabaseHandler.COLUMN_PERSON_FNAME + " = '%s', " +
                DatabaseHandler.COLUMN_PERSON_MNAME + " = '%s', " +
                DatabaseHandler.COLUMN_PERSON_LNAME + " = '%s', " +
                DatabaseHandler.COLUMN_PERSON_STAGE_NAME + " = '%s', " +
                DatabaseHandler.COLUMN_PERSON_DOB + " = '%s' WHERE " +
                DatabaseHandler.COLUMN_PERSON_ID + " = '%s'";

        String sql = String.format(template, person.getFirstName(), person.getMiddleName(), person.getLastName(), person.getStageName(), _sqlHandler.formatDateString(person.getDateOfBirth()), person.getPersonId());
        return _sqlHandler.executeSql(sql);
    }

    @Override
    public boolean delete(Person person) {
        if (person == null) {
            return false;
        }

        String sql = "DELETE FROM " + DatabaseHandler.TABLE_PERSON + " WHERE " + DatabaseHandler.COLUMN_PERSON_ID + " = '" + person.getPersonId() + "'";
        return _sqlHandler.executeSql(sql);
    }

    /**
     * Returns a Person object created from the data at the current row in the ResultSet
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private Person getFromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString(1);
        String first = rs.getString(2);
        String middle = rs.getString(3);
        String last = rs.getString(4);
        String stageName = rs.getString(5);
        LocalDate dob = rs.getDate(6).toLocalDate();
        return new Person(id, first, middle, last, stageName, dob);
    }

    /**
     * Returns a list of Person objects from an SQL query
     *
     * @param sql
     * @return
     */
    private List<Person> getListFromQuery(String sql) {
        Map<String, Person> people = new HashMap<>();
        try {
            ResultSet rs = _sqlHandler.executeQuery(sql);
            while (rs != null && rs.next()) {
                Person p = getFromResultSet(rs);
                people.put(p.getPersonId(), p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>(people.values());
    }
}
