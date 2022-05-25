package com.movies.Movies.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"title", "year", "rank", "genre", "plot", "actors"})
public class Movie {
    private String title;
    private String year;
    private double rank;
    private String genre;
    private String plot;
    private String actors;

    public Movie(String title, String year, double rank, String genre, String plot, String actors) {
        this.title = title;
        this.year = year;
        this.rank = rank;
        this.genre = genre;
        this.plot = plot;
        this.actors = actors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }
}
