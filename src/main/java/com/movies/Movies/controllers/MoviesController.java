package com.movies.Movies.controllers;

import com.movies.Movies.services.MoviesService;
import org.json.JSONException;
import com.movies.Movies.models.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/movies")

public class MoviesController {

    private final MoviesService movieService;

    @RequestMapping(value="/getmovies", method = RequestMethod.GET)
    public List<Movie> getMovies() throws IOException, InterruptedException, JSONException {

        var movies = movieService.allMovies("2019", "2008", "2005");
        return movies;

    }

    @Autowired
    public MoviesController(MoviesService movieService) {
        this.movieService = movieService;
    }

}
