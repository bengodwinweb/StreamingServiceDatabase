package com.streamingservicebackend.model.show;

import com.streamingservicebackend.database.DatabaseHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class ShowDAOTest {

    private static final String testShowId = "SWREBE14";
    private static final String testShowName = "Star Wars Rebels";
    private static final String testShowDescription = "Star Wars Rebels, set five years before the events of Star Wars: A New Hope, tells the story of the Rebellions beginnings while the Empire spreads tyranny through the galaxy.";
    private static final LocalDate testShowDebutDate = LocalDate.of(2013, 10, 3);
    private static final String testShowGenre = "Sci-Fi Fantasy";
    private static final int testShowNumSeasons = 4;
    private static final int testShowNumEpisodes = 75;

    private ShowDAO dao;
    private Show testShow;

    @BeforeEach
    void setUp() {
        dao = new ShowDAO(new DatabaseHandler());
        testShow = new Show(testShowId, testShowName, testShowDescription, testShowDebutDate, testShowNumSeasons, testShowNumEpisodes, testShowGenre);
    }

    @org.junit.jupiter.api.Test
    void get() {
        var show = dao.get(testShow.getId());
        System.out.println(show.isPresent() ? show.get().toString() : "Show not found");
    }

    @org.junit.jupiter.api.Test
    void add() {
        dao.add(testShow);
        get();
    }

    @org.junit.jupiter.api.Test
    void update() {
        testShow.setReleaseDate(LocalDate.of(2014, 10,3));
        dao.update(testShow);
        get();
    }

    @org.junit.jupiter.api.Test
    void delete() {
        dao.delete(testShow);
        get();
    }

    @Test
    void testMinEpisodes() {
        var list = dao.getMinEpisodes();
        if (list.size() == 0) {
            System.out.println("No shows in db");
        } else {
            System.out.printf("Smallest number of total episodes = %,d\n", list.get(0).getNumEpisodes());
            for (var show : list) {
                System.out.println(show.toString());
            }
        }
    }

    @Test
    void testMaxEpisodes() {
        var list = dao.getMaxEpisodes();
        if (list.size() == 0) {
            System.out.println("No shows in db");
        } else {
            System.out.printf("Largest number of total episodes = %,d\n", list.get(0).getNumEpisodes());
            for (var show : list) {
                System.out.println(show.toString());
            }
        }
    }
}