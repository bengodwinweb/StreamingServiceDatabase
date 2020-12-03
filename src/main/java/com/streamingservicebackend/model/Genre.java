package com.streamingservicebackend.model;

public enum Genre {
    NONE(0, "", null),
    DRAMA(1, "Drama", "drama"),
    COMEDY(2, "Comedy", "com"),
    FANTASY(3, "Fantasy", "fantasy"),
    ACTION(4, "Action", "action"),
    DOCUMENTARY(5, "Documentary", "doc"),
    THRILLER(6, "Thriller", "thriller"),
    SPORTS(7, "Sports", "sport"),
    CRIME(8, "Crime", "crime");

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
