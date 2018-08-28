package com.my_tmdbapi.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
* Genres_Response Class
*/
public class GenresResponse {

    @SerializedName("genres")
    @Expose
    private List<Genres> genres;


    public List<Genres> getGenres() {
        return genres;
    }

    public void setGenres(List<Genres> genres) {
        this.genres = genres;
    }
}
