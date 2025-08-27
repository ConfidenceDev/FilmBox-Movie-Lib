package com.filmbox.MovieService.service;

import com.filmbox.MovieService.model.MovieHandler;
import com.filmbox.MovieService.model.MovieRequest;
import com.filmbox.MovieService.model.MovieResponse;
import com.filmbox.MovieService.model.UserResponse;
import com.filmbox.MovieService.respository.MovieRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class MovieServiceImpl implements MovieService {

    @Autowired
    MovieRepository movieRepository;

    //========= User Logic ======================
    @Override
    public UserResponse signIn(String id) {
        return null;
    }

    @Override
    public UserResponse signOut(String id) {
        return null;
    }

    //========= Movie Logic ======================
    @Override
    public List<MovieResponse> getAllMovies(int page, int size, String searchBy) {
        return List.of();
    }

    @Override
    public MovieHandler addMovie(MovieRequest movieRequest) {
        return null;
    }

    @Override
    public String updateMovie(MovieRequest movieRequest) {
        return "";
    }

    @Override
    public String deleteMovie(Long id) {
        return "";
    }
}
