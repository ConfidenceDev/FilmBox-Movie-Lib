package com.filmbox.MovieService.controller;

import com.filmbox.MovieService.model.*;
import com.filmbox.MovieService.service.MovieService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
@Log4j2
@Tag(name = "Users and Movies", description = "Endpoints for managing user authorization with JWT and movies CRUD operations")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping()
    public String hello() {
        return "Hello World!";
    }

    //============ Anonymous Authentication =========================
    @PostMapping("/signIn")
    @Operation(summary = "User signin", description = "Sign in User with JWT session managed in memory")
    public ResponseEntity<UserResponse> signIn(@RequestBody String username) {
        UserResponse userResponse = movieService.signIn(username);
        log.info("User signed in: {}", userResponse.token());
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/signOut")
    @Operation(summary = "User signout", description = "Sign out User and blacklist JWT token")
    public ResponseEntity<UserResponse> signOut(@RequestHeader("Authorization") String authHeader) {
        UserResponse userResponse = movieService.signOut(authHeader);
        log.info("User signed out: {}", userResponse.token());
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    //=========== Main Endpoints ==========================
    @GetMapping("/movies")
    @Operation(summary = "Fetch movies", description = "Retrieve all movies, search by movie title, movie summary, actor, genre or tag. Results are paginated and can be sorted")
    public ResponseEntity<List<MovieResponse>> getAllMovies(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "5") int size,
                                                            @RequestParam(defaultValue = "id") String sortBy,
                                                            @RequestParam(defaultValue = "asc") String sortDir,
                                                            @RequestParam(defaultValue = "all") String searchBy) {
        List<MovieResponse> response = movieService.getAllMovies(page, size, sortBy, sortDir, searchBy);
        log.info("Retrieved movies for page: {} and size: {}", Optional.of(page), Optional.of(size));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/movies/{id}")
    @Operation(summary = "Fetch a movie", description = "Retrieve a movie with its details")
    public ResponseEntity<MovieResponse> getMovieById(@PathVariable Long id) {
        MovieResponse response = movieService.getMovieById(id);
        log.info("Retrieved one movie with id: {}", id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/movies")
    @Operation(summary = "Add a new movie", description = "Add a new movie to the library")
    public ResponseEntity<MovieHandler> addMovie(@RequestBody MovieRequest movieRequest) {
        MovieHandler movieHandler = movieService.addMovie(movieRequest);
        log.info("Movie Added: {}", movieHandler.id());
        return new ResponseEntity<>(movieHandler, HttpStatus.CREATED);
    }

    @PutMapping("/movies/{id}")
    @Operation(summary = "Update a movie", description = "Update details of an existing movie in the library")
    public ResponseEntity<String> updateMovie(@PathVariable Long id, @RequestBody MovieRequest movieRequest) {
        String companyId = movieService.updateMovie(id, movieRequest);
        log.info("Movie updated Id: {}", companyId);
        return new ResponseEntity<>(companyId, HttpStatus.OK);
    }

    @DeleteMapping("/movies/{id}")
    @Operation(summary = "Remove a movie", description = "Remove a move from the library with its id")
    public ResponseEntity<MovieHandler> deleteMovie(@RequestBody Long id) {
        String message = movieService.deleteMovie(id);
        MovieHandler movieHandler = new MovieHandler(id, message);
        log.info("Movie removed: {}", id);
        return new ResponseEntity<>(movieHandler, HttpStatus.OK);
    }

}
