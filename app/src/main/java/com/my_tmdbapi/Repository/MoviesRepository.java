package com.my_tmdbapi.Repository;

import com.my_tmdbapi.Interfaces.OnGetGenresCallback;
import com.my_tmdbapi.Interfaces.OnGetMoviesCallback;
import com.my_tmdbapi.Interfaces.TMDbApi;
import com.my_tmdbapi.Models.GenresResponse;
import com.my_tmdbapi.Models.MovieResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesRepository {

    //    Sort Movies...
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UPCOMING = "upcoming";
    //    Required to get Movies...
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";
    private static final String API_KEY = "112d2d99cb1a29bdafde742aeb373107";
    //    Object of MoviesRepository Class...
    private static MoviesRepository repository;
    //    Object of TMDbApi Class...
    private TMDbApi api;


    /*
     *  MoviesRepository Class Constructor...
     * @param api
     **/
    public MoviesRepository(TMDbApi api) {
        this.api = api;
    }

    /*
     * getInstance
     * **/
    public static MoviesRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            repository = new MoviesRepository(retrofit.create(TMDbApi.class));
        }
        return repository;
    }

    /*
     *    getMovies function, with page as variable of dataType INTEGER as parameter,
     *    OnGetMoviesCallback interface java class as parameter...
     **/
    public void getMovies(int page, String sortBy, final OnGetMoviesCallback callback) {
        Callback<MovieResponse> call = new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null && movieResponse.getMovies() != null) {
                        callback.onSuccess(movieResponse.getPage(), movieResponse.getMovies());
                    } else {
                        callback.onError();
                    }
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                callback.onError();
            }
        };
        /*
        * Sort Movies as selector one of three...
        **/
        switch (sortBy) {
            case POPULAR:
                api.getPopularMovies(API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
            case TOP_RATED:
                api.getTopRatedMovies(API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
            case UPCOMING:
                api.getUpcomingMovies(API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
        }
    }


    //    getGenres function, with OnGetGenresCallback interface java class as parameter...
    public void getGenres(final OnGetGenresCallback callback) {
        api.getGenresMovies(API_KEY, LANGUAGE).enqueue(new Callback<GenresResponse>() {
            @Override
            public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {
                if (response.isSuccessful()) {
                    GenresResponse genresResponse = response.body();
                    if (genresResponse != null && genresResponse.getGenres() != null) {
                        callback.onSuccess(genresResponse.getGenres());
                    } else {
                        callback.onError();
                    }
                }
            }

            @Override
            public void onFailure(Call<GenresResponse> call, Throwable t) {
                callback.onError();
            }
        });
    }
}
