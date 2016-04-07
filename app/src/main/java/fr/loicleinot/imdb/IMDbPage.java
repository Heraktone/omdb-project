package fr.loicleinot.imdb;

import android.graphics.Bitmap;

/**
 * Created by Heraktone on 29/03/2016.
 */
public class IMDbPage {
    private String Title;
    private String Plot;
    private Bitmap Poster;
    private String Year;
    private String Time;
    private String Actors;
    private String Directors;
    private String Genre;

    public IMDbPage(String title, String plot, Bitmap poster, String year, String time, String actors, String directors, String genre) {
        Title = title;
        Plot = plot;
        Poster = poster;
        Year = year;
        Time = time;
        Actors = actors;
        Directors = directors;
        Genre = genre;
    }

    public String getGenre() {
        return Genre;
    }

    public Bitmap getPoster() {
        return Poster;
    }

    public String getPlot() {
        return Plot;
    }

    public String getTitle() {
        return Title;
    }

    public String getYear() {
        return Year;
    }

    public String getTime() {
        return Time;
    }

    public String getActors() {
        return Actors;
    }

    public String getDirectors() {
        return Directors;
    }

}
