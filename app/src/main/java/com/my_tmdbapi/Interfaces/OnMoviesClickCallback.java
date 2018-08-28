package com.my_tmdbapi.Interfaces;

import com.my_tmdbapi.Models.Movie;

/*
 *   OnMovieClickCallback interface java class, will use to oneClick on a Movie to open details in webView...
 */
public interface OnMoviesClickCallback {
    void onClick(Movie movie);
}
