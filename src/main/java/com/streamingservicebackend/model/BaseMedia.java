package com.streamingservicebackend.model;

import java.time.LocalDate;
import java.util.Objects;

public class BaseMedia {

    private String id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private String genre;

    public BaseMedia(String id, String name, String description, LocalDate releaseDate, String genre) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.genre = genre;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseMedia baseMedia = (BaseMedia) o;
        return Objects.equals(id, baseMedia.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
