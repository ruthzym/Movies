package com.movies.Movies.services;

import com.movies.Movies.models.Movie;
import com.movies.Movies.utils.MoviesUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MoviesService {

    public static final String API_URL = "http://www.omdbapi.com/";
    public static final String API_KEY = "?apikey=3711542b";
    private final MoviesUtils moviesUtils;

    //Method return list of sorted movies list of all 3 years
    public List<Movie> allMovies(String year1, String year2, String year3) throws InterruptedException, JSONException, IOException {
        List<Movie> movies = getMovies(year1);
        movies.addAll(getMovies(year2));
        movies.addAll(getMovies(year3));

        List<Movie> moviesAboveAverage = moviesAboveAverage(movies);
        moviesAboveAverage.sort(Comparator.comparing(Movie::getRank).thenComparing(Movie::getTitle));

        return moviesAboveAverage;
    }

    //Method checks if the list of movies by year has more than 50 movies, sets pagination and returns list of movies.
    private List<Movie> getMovies(String year) throws IOException, InterruptedException, JSONException {
        List<Movie> movies = new ArrayList<Movie>();
        var jsonMovies = getMovieByYear(year, "&page=1");
        movies.addAll(parseMoviesJSon(jsonMovies));

        var totalResults = jsonMovies.getInt("totalResults");
        int page = (totalResults > 50) ? 5 : (int) Math.ceil(totalResults / 10);

        for (int i = 2; i <= page; i++) {
            String pagination = "&page=" + i;
            JSONObject obj = getMovieByYear(year, pagination);
            movies.addAll(parseMoviesJSon(obj));
        }

        return movies;
    }

    //Method parses JSON object to get array of movies. Then it calls method to fetch specific movie data by id. Method returns list of movies.
    private List<Movie> parseMoviesJSon(JSONObject jobj) throws JSONException, IOException, InterruptedException {
        List<Movie> movies = new ArrayList<>();
        JSONArray array = jobj.getJSONArray("Search");

        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonMovie = array.getJSONObject(i);
            String id = jsonMovie.getString("imdbID");
            Movie movie = getMovieById(id);
            movies.add(movie);
        }

        return movies;
    }

    //Method calculates average of movies ranks and returns list of movies that have higher ranks than average
    private List<Movie> moviesAboveAverage(List<Movie> movies) {
        double average = average(movies);
        return movies.stream().filter(a -> a.getRank() > average).collect(Collectors.toList());
    }
    private Double average(List<Movie> movies){
        return movies.stream()
                .mapToDouble(Movie::getRank)
                .average().getAsDouble();
    }

    //Method fetches movie data of selected year from api and returns JSON object
    private JSONObject getMovieByYear(String year, String page) throws IOException, InterruptedException, JSONException {
        return new JSONObject(moviesUtils.fetchMovieByYear(year, page).body());
    }

    //Method fetches movie by id from api and returns Movie object
    private Movie getMovieById(String id) throws IOException, InterruptedException, JSONException {
        HttpResponse<String> response = moviesUtils.fetchMovieById(id);

        JSONObject movieJson = new JSONObject(response.body());
        String title = movieJson.getString("Title");
        String year = movieJson.getString("Year");

        double rank;
        if (movieJson.getString("imdbRating").equals("N/A")) {
            rank = 0;
        } else {
            rank = movieJson.getDouble("imdbRating");
        }

        String genre = movieJson.getString("Genre");
        String plot = movieJson.getString("Plot");
        String actors = movieJson.getString("Actors");

        return new Movie(title, year, rank, genre, plot, actors);
    }

    @Autowired
    public MoviesService(MoviesUtils moviesUtils) {
        this.moviesUtils = moviesUtils;
    }
}
