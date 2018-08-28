package com.my_tmdbapi.Interfaces;

import com.my_tmdbapi.Models.Movie;

import java.util.List;

/*
*   OnGetMoviesCallback interface java class...
*/
public interface OnGetMoviesCallback {
    void onSuccess(int page, List<Movie> movies);

    void onError();
}
