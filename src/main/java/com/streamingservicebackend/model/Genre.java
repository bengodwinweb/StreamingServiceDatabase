package com.streamingservicebackend.model;

public enum Genre {
    NONE(0, "Genre", null),
    ACTION(1, "Action", "action"),
    COMEDY(2, "Comedy", "com"),
    CRIME(3, "Crime", "crime"),
    DOCUMENTARY(4, "Documentary", "doc"),
    DRAMA(5, "Drama", "drama"),
    FANTASY(6, "Fantasy", "fantasy"),
    SPORTS(7, "Sports", "sport"),
    THRILLER(8, "Thriller", "thriller");

    private final String displayText;
    private final String queryText;
    private final int id;

    Genre(int id, String displayText, String queryText) {
        this.displayText = displayText;
        this.queryText = queryText;
        this.id = id;
    }

    public String getDisplayText() {
        return displayText;
    }

    public String getQueryText() {
        return queryText;
    }

    public int getId() {
        return id;
    }
}
