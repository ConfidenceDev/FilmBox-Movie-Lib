package com.filmbox.MovieService.service;

import com.filmbox.MovieService.model.MovieHandler;
import com.filmbox.MovieService.model.MovieRequest;
import com.filmbox.MovieService.model.MovieResponse;
import com.filmbox.MovieService.model.UserResponse;

import java.util.List;

public interface MovieService {

    UserResponse signIn(String username);

    UserResponse signOut(String authHeader);

    List<MovieResponse> getAllMovies(int page, int size, String sortBy, String sortDir, String searchBy);

    MovieResponse getMovieById(Long id);

    MovieHandler addMovie(MovieRequest movieRequest);

    String updateMovie(Long id, MovieRequest movieRequest);

    String deleteMovie(Long id);
}
