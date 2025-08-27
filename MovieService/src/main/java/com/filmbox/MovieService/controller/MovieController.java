package com.filmbox.MovieService.controller;

import com.filmbox.MovieService.model.*;
import com.filmbox.MovieService.service.MovieService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/movies")
@Log4j2
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping()
    public String hello(){
        return "Hello World!";
    }

    //============ Anonymous Authentication =========================
    @PostMapping("/signIn")
    public ResponseEntity<UserResponse> signIn(@RequestBody String id){
        UserResponse userResponse = movieService.signIn(id);
        log.info("User signed in: {}", userResponse.id());
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/signOut")
    public ResponseEntity<UserResponse> signOut(@RequestParam String id){
        UserResponse userResponse = movieService.signOut(id);
        log.info("User signed out: {}", userResponse.id());
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    //=========== Main Endpoints ==========================
    @GetMapping("/")
    public ResponseEntity<List<MovieResponse>> getAllMovies(@RequestParam int page,
                                                                 @RequestParam int size,
                                                                 @RequestParam String searchBy){
        List<MovieResponse> response = movieService.getAllMovies(page, size, searchBy);
        log.info("Retrieved movies for page: {} and size: {}", page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<MovieHandler> addMovie(@RequestBody MovieRequest movieRequest){
        MovieHandler movieHandler = movieService.addMovie(movieRequest);
        log.info("Movie Added: {}", movieHandler.id());
        return new ResponseEntity<>(movieHandler, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateMovie(@RequestBody MovieRequest movieRequest){
        String companyId = movieService.updateMovie(movieRequest);
        log.info("Movie updated Id: {}", companyId);
        return new ResponseEntity<>(companyId, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MovieHandler> deleteMovie(@RequestBody Long id){
        String message = movieService.deleteMovie(id);
        MovieHandler movieHandler = new MovieHandler(id, message);
        log.info("Movie removed: {}", id);
        return new ResponseEntity<>(movieHandler, HttpStatus.OK);
    }

}
