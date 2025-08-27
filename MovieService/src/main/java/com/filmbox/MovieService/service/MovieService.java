package com.filmbox.MovieService.service;

import com.filmbox.MovieService.model.MovieHandler;
import com.filmbox.MovieService.model.MovieRequest;
import com.filmbox.MovieService.model.MovieResponse;
import com.filmbox.MovieService.model.UserResponse;

import java.util.List;

public interface MovieService {

    UserResponse signIn(String id);

    UserResponse signOut(String id);

    List<MovieResponse> getAllMovies(int page, int size, String searchBy);

    MovieHandler addMovie(MovieRequest movieRequest);

    String updateMovie(MovieRequest movieRequest);

    String deleteMovie(Long id);
}
