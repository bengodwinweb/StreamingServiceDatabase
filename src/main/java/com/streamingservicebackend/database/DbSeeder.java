package com.streamingservicebackend.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

/**
 * This class is only present to seed the db with the files in /db_seed_files/
 */
public class DbSeeder {

    ISqlHandler _sqlHandler;

    public DbSeeder(ISqlHandler sqlHandler) {
        this._sqlHandler = sqlHandler;
    }

    private String getDbSeedFilesDir() {
        return System.getProperty("user.dir") + File.separator + "db_seed_files";
    }

    /**
     * Seeds the db with the values in /db_seed_files/
     */
    public void seed() {
//        seedStreamingServices();
        seedShows();
        seedMovies();
        seedPerson();
        seedServicesShows();
        seedServiceMovies();
        seedShowActor();
        seedShowDirector();
        seedMovieActor();
        seedMovieDirector();
        _sqlHandler.commit();
    }

    /**
     * Reads streaming_service.csv and inserts all values into the db
     */
    private void seedStreamingServices() {
        String streamingServicesFile = getDbSeedFilesDir() + File.separator + "streaming_service.csv";
        String sqlInsertTemplate = "INSERT INTO " + DatabaseHandler.TABLE_SERVICE + " VALUES ('%s', '%s', %s, %s, %s)";
        readFileAndExecute(streamingServicesFile, sqlInsertTemplate);
    }

    /**
     * Reads show.csv and inserts all values into the db
     */
    private void seedShows() {
        String showsFile = getDbSeedFilesDir() + File.separator + "show.csv";
        String sqlInsertTemplate = "INSERT INTO " + DatabaseHandler.TABLE_SHOW + " VALUES ('%s', '%s', '%s', '%s', '%s', %s, %s)";
        readFileAndExecute(showsFile, sqlInsertTemplate);
    }

    /**
     * Reads movie.csv and inserts all values into the db
     */
    private void seedMovies() {
        String moviesFile = getDbSeedFilesDir() + File.separator + "movie.csv";
        String sqlInsertTemplate = "INSERT INTO " + DatabaseHandler.TABLE_MOVIE + " VALUES ('%s', '%s', '%s', '%s', '%s', %s)";
        readFileAndExecute(moviesFile, sqlInsertTemplate);
    }

    /**
     * Reads service_show.csv and inserts all values into the db
     */
    private void seedServicesShows() {
        String servicesShowsFile = getDbSeedFilesDir() + File.separator + "service_show.csv";
        String sqlInsertTemplate = "INSERT INTO " + DatabaseHandler.TABLE_SERVICE_SHOW + " VALUES ('%s', '%s')";
        readFileAndExecute(servicesShowsFile, sqlInsertTemplate);
    }

    private void seedPerson() {
        String personFile = getDbSeedFilesDir() + File.separator + "person.csv";
        String sqlInsertTemplate = "INSERT INTO " + DatabaseHandler.TABLE_PERSON + " VALUES ('%s', '%s', '%s', '%s', '%s', '%s')";
        readFileAndExecute(personFile, sqlInsertTemplate);
    }

    private void seedServiceMovies() {
        String serviceMoviesFile = getDbSeedFilesDir() + File.separator + "service_movie.csv";
        String sqlInsertTemplate = "INSERT INTO " + DatabaseHandler.TABLE_SERVICE_MOVIE + " VALUES ('%s', '%s', %s)";
        readFileAndExecute(serviceMoviesFile, sqlInsertTemplate);
    }

    private void seedShowActor() {
        String showActorFile = getDbSeedFilesDir() + File.separator + "show_actor.csv";
        String sqlInsertTemplate = "INSERT INTO " + DatabaseHandler.TABLE_SHOW_ACTOR + " VALUES ('%s', '%s')";
        readFileAndExecute(showActorFile, sqlInsertTemplate);
    }

    private void seedShowDirector() {
        String showDirectorFile = getDbSeedFilesDir() + File.separator + "show_director.csv";
        String sqlInsertTemplate = "INSERT INTO " + DatabaseHandler.TABLE_SHOW_DIRECTOR + " VALUES ('%s', '%s')";
        readFileAndExecute(showDirectorFile, sqlInsertTemplate);
    }

    private void seedMovieActor() {
        String movieActorFile = getDbSeedFilesDir() + File.separator + "movie_actor.csv";
        String sqlInsertTemplate = "INSERT INTO " + DatabaseHandler.TABLE_MOVIE_ACTOR + " VALUES ('%s', '%s')";
        readFileAndExecute(movieActorFile, sqlInsertTemplate);
    }

    private void seedMovieDirector() {
        String movieDirectorFile = getDbSeedFilesDir() + File.separator + "movie_director.csv";
        String sqlInsertTemplate = "INSERT INTO " + DatabaseHandler.TABLE_MOVIE_DIRECTOR + " VALUES ('%s', '%s')";
        readFileAndExecute(movieDirectorFile, sqlInsertTemplate);
    }

    /**
     * Reads the file at the given path and inserts the read values into the db using the given template to format the INSERT command.
     * @param file absolute path to the file
     * @param commandTemplate template to be called with String.format(template, String[] valuesFromARowInTheCsv);
     */
    private void readFileAndExecute(String file, String commandTemplate) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                try {
                    _sqlHandler.execute(String.format(commandTemplate, values));
                } catch (SQLException e) {
                    String message = e.getMessage();
                    // don't need to print out all the unique key constraints, we know there are duplicates if this isn't the first "seed"
                    if (!message.contains("unique constraint")) {
                        System.out.println(message);
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
