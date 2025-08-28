package com.filmbox.MovieService.service;

import com.filmbox.MovieService.model.*;

import java.util.List;

public interface MovieService {

    UserResponse signIn(UserRequest userRequest);

    UserResponse signOut(String authHeader);

    List<MovieResponse> getAllMovies(int page, int size, String sortBy, String sortDir, String searchBy);

    MovieResponse getMovieById(Long movieId);

    MovieHandler addMovie(MovieRequest movieRequest);

    String updateMovie(Long movieId, MovieRequest movieRequest);

    String deleteMovie(Long movieId, String authHeader);
}
