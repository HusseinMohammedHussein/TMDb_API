package com.my_tmdbapi.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.my_tmdbapi.Adapters.MoviesAdapter;
import com.my_tmdbapi.Interfaces.OnGetGenresCallback;
import com.my_tmdbapi.Interfaces.OnGetMoviesCallback;
import com.my_tmdbapi.Interfaces.OnMoviesClickCallback;
import com.my_tmdbapi.Models.Genres;
import com.my_tmdbapi.Models.Movie;
import com.my_tmdbapi.R;
import com.my_tmdbapi.Repository.MoviesRepository;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    //  Initialization Elements...
    private RecyclerView rcView;
    private MoviesAdapter mAdapter;
    private MoviesRepository moviesRepository;
    private Toolbar toolbar;
    //  Object of List<Genres>
    private List<Genres> movieGenres;
    //  Variable boolean DataType
    private boolean isFeatchingMovies;
    //  Variable Integer DataType
    private int currentPage = 1;

    /*      Variable String DataType,
     *       loaded in URL to call website,
     *       when onClick on a Movie will call it with Movie_Id...
     * */
    private String PARS = "https://www.themoviedb.org/movie/";

    /*   Object of OnMovieClickCallback to set in MoviesAdapter as parameter
     * loaded in "Intent = WebSite_URL + Movie_Id"
     * */
    OnMoviesClickCallback callback = new OnMoviesClickCallback() {
        @Override
        public void onClick(Movie movie) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PARS + movie.getId()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    };
    //  SORT Popular MOVIES as Default...
    private String sortBy = MoviesRepository.POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Initialization RecyclerView...
        rcView = findViewById(R.id.movies_list);
//        Initialization ToolBar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        moviesRepository = MoviesRepository.getInstance();
//        Void Method...
        setupOnScrollListener();
//        Void Method
        getGenres();
    }

    private void setupOnScrollListener() {
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        rcView.setLayoutManager(manager);
        rcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();
                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFeatchingMovies) {
                        getMovies(currentPage + 1);
                    }
                }

            }
        });
    }

    public void getGenres() {
        moviesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genres> genres) {
                movieGenres = genres;
                getMovies(currentPage);
            }
            @Override
            public void onError() {
                showError();
            }
        });
    }

    public void getMovies(int page) {
        isFeatchingMovies = true;
        moviesRepository.getMovies(page, sortBy, new OnGetMoviesCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {
//                This will show in "LogCat"...
                Log.i("SORT_BY", "Page Of  = " + sortBy);
                if (mAdapter == null) {
                    mAdapter = new MoviesAdapter(movies, movieGenres, callback);
                    rcView.setAdapter(mAdapter);
                } else {
                    if (page == 1) {
                        mAdapter.clearMovies();
                    }
                    mAdapter.appendMovies(movies);
                }
                currentPage = page;
                isFeatchingMovies = false;
                setTitle();
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void setTitle() {
        switch (sortBy) {
            case MoviesRepository.POPULAR:
                setTitle(getString(R.string.popular));
                break;
            case MoviesRepository.UPCOMING:
                setTitle(getString(R.string.upcoming));
                break;
            case MoviesRepository.TOP_RATED:
                setTitle(getString(R.string.top_rated));
                break;
        }
    }

    //   Custom a Menu...
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movies, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void showSortMenu() {
        PopupMenu sortMenu = new PopupMenu(this, findViewById(R.id.sort));
        sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                currentPage = 1;
                switch (item.getItemId()) {
                    case R.id.popular:
                        sortBy = MoviesRepository.POPULAR;
                        getMovies(currentPage);
                        return true;
                    case R.id.top_rated:
                        sortBy = MoviesRepository.TOP_RATED;
                        getMovies(currentPage);
                        return true;
                    case R.id.upcoming:
                        sortBy = MoviesRepository.UPCOMING;
                        getMovies(currentPage);
                        return true;
                    default:
                        return false;
                }
            }
        });
        sortMenu.inflate(R.menu.menu_movies_sort);
        sortMenu.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort:
                showSortMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void showError() {
        Toast.makeText(MainActivity.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
    }
}
