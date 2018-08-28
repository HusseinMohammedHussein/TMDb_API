package com.my_tmdbapi.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.my_tmdbapi.Interfaces.OnMoviesClickCallback;
import com.my_tmdbapi.Models.Genres;
import com.my_tmdbapi.Models.Movie;
import com.my_tmdbapi.R;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    //    Get image URL...
    private String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";
    //    List of Movie Class...
    private List<Movie> movies;
    //    List of Genres Class...
    private List<Genres> allGenres;
    //    Object of OnMovieClickCallback class...
    private OnMoviesClickCallback callback;

    /*    MoviesAdapter Constructor,
     *       with List<Movie> as parameter  +
     *       List<Genres> as parameter      +
     *       OnMoviesClickCallback as parameter
     */
    public MoviesAdapter(List<Movie> movies, List<Genres> allGenres, OnMoviesClickCallback callback) {
        this.movies = movies;
        this.allGenres = allGenres;
        this.callback = callback;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_items, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void appendMovies(List<Movie> moviesToAppend) {
        movies.addAll(moviesToAppend);
        notifyDataSetChanged();
    }

    public void clearMovies() {
        movies.clear();
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        Movie movie;
        ImageView poster;
        TextView releaseDate, title, genres, rating;

        public MovieViewHolder(View itemView) {
            super(itemView);
            releaseDate = itemView.findViewById(R.id.item_movie_release_date);
            title = itemView.findViewById(R.id.item_movie_title);
            genres = itemView.findViewById(R.id.item_movie_genre);
            rating = itemView.findViewById(R.id.item_movie_rating);
            poster = itemView.findViewById(R.id.item_image_poster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(movie);
                }
            });
        }

        public void bind(Movie movie) {
            releaseDate.setText(movie.getReleaseDate().split("-")[0]);
            title.setText(movie.getTitle());
            rating.setText(String.valueOf(movie.getRating()));
            genres.setText(getGenres(movie.getGenreIds()));
            Glide.with(itemView)
                    .load(IMAGE_BASE_URL + movie.getPosterPath())
                    .apply(RequestOptions.placeholderOf(R.mipmap.ic_launcher))
                    .into(poster);
            this.movie = movie;
        }

        private String getGenres(List<Integer> genreIds) {
            List<String> movieGenres = new ArrayList<>();
            for (Integer genreId : genreIds) {
                for (Genres genres : allGenres) {
                    if (genres.getId() == genreId) {
                        movieGenres.add(genres.getName());
                        break;
                    }
                }
            }
            return TextUtils.join(", ", movieGenres);
        }
    }

}
