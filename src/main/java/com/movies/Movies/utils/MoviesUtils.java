package com.movies.Movies.utils;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class MoviesUtils {
    private static final String API_URL = "http://www.omdbapi.com/";
    private static final String API_KEY = "?apikey=3711542b";

    public HttpResponse<String> fetchMovieById(String id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("accept", "application/json")
                .uri(URI.create(API_URL + API_KEY + "&i=" + id))
                .GET()
                .build();

        return client.send(request,HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> fetchMovieByYear(String year, String page) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("accept", "application/json")
                .uri(URI.create(API_URL + API_KEY + "&s=one&type=movie&y=" + year + page))
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
