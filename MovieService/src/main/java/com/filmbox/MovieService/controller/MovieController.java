package com.filmbox.MovieService.controller;

import com.filmbox.MovieService.model.*;
import com.filmbox.MovieService.service.MovieService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@Tag(name = "Users and Movies", description = "Endpoints for managing user authorization with JWT and movies CRUD operations")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/test")
    public String hello() {
        log.info("HELLO TEST");
        return "Hello World!";
    }

    //============ Anonymous Authentication =========================
    @PostMapping("/signIn")
    @Operation(summary = "User signin", description = "Sign in User with JWT session managed in memory",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful signin",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"username\": \"test123\",\n" +
                                                    "  \"token\": \"eyJhbGciOiJIUzI1NiIsInR...\",\n" +
                                                    "  \"message\": \"Signed in successfully\"\n" +
                                                    "}"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", ref = "bad_request")
            }
    )
    public ResponseEntity<UserResponse> signIn(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = movieService.signIn(userRequest);
        log.info("User signed in: {}", userResponse.token());
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/signOut")
    @Operation(summary = "User signout", description = "Sign out User and blacklist JWT token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful signout",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"username\": \"test123\",\n" +
                                                    "  \"token\": \"null\",\n" +
                                                    "  \"message\": Signed out successfully\n" +
                                                    "}"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    public ResponseEntity<UserResponse> signOut(@RequestHeader("Authorization") String authHeader) {
        UserResponse userResponse = movieService.signOut(authHeader);
        log.info("User signed out: {}", userResponse.token());
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    //=========== Main Endpoints ==========================
    @GetMapping("/movies")
    @Operation(summary = "Fetch movies", description = "Retrieve all movies, search by movie title, movie summary, actor, genre or tag. Results are paginated and can be sorted",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "[\n" +
                                                            "  {\n" +
                                                            "    \"id\": 2,\n" +
                                                            "    \"posterId\": \"test123\",\n" +
                                                            "    \"title\": \"Test Movie\",\n" +
                                                            "    \"summary\": \"This is a test movie\",\n" +
                                                            "    \"year\": 2022,\n" +
                                                            "    \"genre\": \"Action\",\n" +
                                                            "    \"actors\": [\"Joe\", \"Niel\"],\n" +
                                                            "    \"tags\": [\"Comedy\", \"Classic\"],\n" +
                                                            "    \"message\": null,\n" +
                                                            "    \"createdAt\": \"2025-08-28T08:55:26.448581Z\"\n" +
                                                            "  }\n" +
                                                            "]"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "400", ref = "bad_request")
            })
    public ResponseEntity<List<MovieResponse>> getAllMovies(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "5") int size,
                                                            @RequestParam(defaultValue = "id") String sortBy,
                                                            @RequestParam(defaultValue = "asc") String sortDir,
                                                            @RequestParam(defaultValue = "all") String searchBy) {
        List<MovieResponse> response = movieService.getAllMovies(page, size, sortBy, sortDir, searchBy);
        log.info("Retrieved movies for page: {} and size: {}", Optional.of(page), Optional.of(size));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/movies/{movieId}")
    @Operation(summary = "Fetch a movie", description = "Retrieve a movie with its details",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Movie details retrieved",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"id\": 2,\n" +
                                                    "  \"posterId\": \"test123\",\n" +
                                                    "  \"title\": \"Test Movie\",\n" +
                                                    "  \"summary\": \"This is a test movie\",\n" +
                                                    "  \"year\": 2022,\n" +
                                                    "  \"genre\": \"Action\",\n" +
                                                    "  \"actors\": [\"Joe\", \"Niel\"],\n" +
                                                    "  \"tags\": [\"Comedy\", \"Classic\"],\n" +
                                                    "  \"message\": null,\n" +
                                                    "  \"createdAt\": \"2025-08-28T08:55:26.448581Z\"\n" +
                                                    "}"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Movie not found")
            }
    )
    public ResponseEntity<MovieResponse> getMovieById(@PathVariable Long movieId) {
        MovieResponse response = movieService.getMovieById(movieId);
        log.info("Retrieved one movie with id: {}", movieId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/movies")
    @Operation(summary = "Add a new movie", description = "Add a new movie to the library",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Movie added successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"id\": 2,\n" +
                                                    "  \"message\": \"Movie Added Successfully\"\n" +
                                                    "}"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            }
    )
    public ResponseEntity<MovieHandler> addMovie(@RequestBody MovieRequest movieRequest) {
        MovieHandler movieHandler = movieService.addMovie(movieRequest);
        log.info("Movie Added: {}", movieHandler.id());
        return new ResponseEntity<>(movieHandler, HttpStatus.CREATED);
    }

    @PutMapping("/movies/{movieId}")
    @Operation(summary = "Update a movie", description = "Update details of an existing movie in the library",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Movie updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "\"Movie updated successfully with id 2\""
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Movie not found")
            }
    )
    public ResponseEntity<String> updateMovie(@PathVariable Long movieId, @RequestBody MovieRequest movieRequest) {
        String companyId = movieService.updateMovie(movieId, movieRequest);
        log.info("Movie updated Id: {}", companyId);
        return new ResponseEntity<>(companyId, HttpStatus.OK);
    }

    @DeleteMapping("/movies/{movieId}")
    @Operation(summary = "Remove a movie", description = "Remove a move from the library with its id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Movie deleted successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"id\": 2,\n" +
                                                    "  \"message\": \"Movie deleted successfully\"\n" +
                                                    "}"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Movie not found")
            })
    public ResponseEntity<MovieHandler> deleteMovie(@PathVariable Long movieId,
                                                    @RequestHeader("Authorization") String authHeader) {
        String message = movieService.deleteMovie(movieId, authHeader);
        MovieHandler movieHandler = new MovieHandler(movieId, message);
        log.info("Movie removed: {}", movieId);
        return new ResponseEntity<>(movieHandler, HttpStatus.OK);
    }

}
