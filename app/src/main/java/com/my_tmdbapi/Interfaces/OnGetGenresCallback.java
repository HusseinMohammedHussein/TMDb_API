package com.my_tmdbapi.Interfaces;

import com.my_tmdbapi.Models.Genres;

import java.util.List;

/*
* OnGetGenresCallback interface java class...
*/
public interface OnGetGenresCallback {
    void onSuccess(List<Genres> genres);

    void onError();
}
