package com.my_tmdbapi.Interfaces;

import com.my_tmdbapi.Models.GenresResponse;
import com.my_tmdbapi.Models.Movie;
import com.my_tmdbapi.Models.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/*
 *  TMDbApi interface java class, role this class is call paths for @Query Movies of the server...
 * */
public interface TMDbApi {
    // Get/Popular Movies
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    // Get/Top_Rated Movies
    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    // Get/Upcoming Movies
    @GET("movie/upcoming")
    Call<MovieResponse> getUpcomingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    // Get/Genres Movies
    @GET("genre/movie/list")
    Call<GenresResponse> getGenresMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );
}
