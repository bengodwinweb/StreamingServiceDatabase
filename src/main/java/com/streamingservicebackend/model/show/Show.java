package com.streamingservicebackend.model.show;

import com.streamingservicebackend.model.BaseMedia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Show extends BaseMedia {

    private int numSeasons;
    private int numEpisodes;

    public Show(String id, String name, String description, LocalDate releaseDate, int numSeasons, int numEpisodes, String genre) {
        super(id, name, description, releaseDate, genre);
        this.numSeasons = numSeasons;
        this.numEpisodes = numEpisodes;
    }

    public int getNumSeasons() {
        return numSeasons;
    }

    public void setNumSeasons(int numSeasons) {
        this.numSeasons = numSeasons;
    }

    public int getNumEpisodes() {
        return numEpisodes;
    }

    public void setNumEpisodes(int numEpisodes) {
        this.numEpisodes = numEpisodes;
    }

    @Override
    public String toString() {
        DateTimeFormatter dt = DateTimeFormatter.ofPattern("MMM d, yyyy");
        return String.format("Show Name: %s, Id: %s, Release Date: %s, Genre: %s, Seasons: %d, Episodes: %d", getName(), getId(), dt.format(getReleaseDate()), getGenre(), getNumSeasons(), getNumEpisodes());
    }
}
