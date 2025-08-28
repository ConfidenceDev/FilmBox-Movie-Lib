package com.filmbox.MovieService.service;

import com.filmbox.MovieService.config.JwtUtil;
import com.filmbox.MovieService.config.TokenBlacklist;
import com.filmbox.MovieService.entity.*;
import com.filmbox.MovieService.exception.MovieException;
import com.filmbox.MovieService.model.*;
import com.filmbox.MovieService.respository.*;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceImplTest {

    @InjectMocks
    private MovieServiceImpl movieService;

    @Mock private MovieRepository movieRepository;
    @Mock private ActorRepository actorRepository;
    @Mock private GenreRepository genreRepository;
    @Mock private TagRepository tagRepository;
    @Mock private JwtUtil jwtUtil;
    @Mock private TokenBlacklist tokenBlacklist;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Signin user - Success Scenario")
    @Test
    public void signIn_shouldReturnUserResponse() {
        UserResponse response = movieService.signIn("user");
        assertEquals("user", response.username());
        assertNotNull(response.token());
    }

    @DisplayName("Signout user - Success Scenario")
    @Test
    public void signOut_shouldReturnUserResponse() {
        when(jwtUtil.validate(anyString())).thenReturn(Jwts.parser().parseClaimsJws("dummy"));
        UserResponse response = movieService.signOut("Bearer token");
        assertNotNull(response.username());
    }

    @DisplayName("Get all movies - Success Scenario")
    @Test
    public void getAllMovies_shouldReturnList() {
        Movie movie = Movie.builder().id(1L).posterId("user").title("Test").year(2020).build();
        Page<Movie> page = new PageImpl<>(List.of(movie));
        when(movieRepository.findAll(any(Pageable.class))).thenReturn(page);

        List<MovieResponse> result = movieService.getAllMovies(0, 5, "id", "asc", "all");
        assertFalse(result.isEmpty());
    }

    @DisplayName("Get a movie by Id - Success Scenario")
    @Test
    public void getMovieById_shouldReturnMovieResponse() {
        Movie movie = Movie.builder().id(1L).posterId("user").title("Test").year(2020).build();
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        MovieResponse response = movieService.getMovieById(1L);
        assertEquals(1L, response.getId());
    }

    @DisplayName("Add a movie - Success Scenario")
    @Test
    public void addMovie_shouldReturnMovieHandler() {
        MovieRequest req = MovieRequest.builder().posterId("user").title("Test").year(2020).build();
        Genre genre = Genre.builder().genre("Action").build();
        when(genreRepository.findByGenreIgnoreCase(anyString())).thenReturn(Optional.of(genre));
        when(movieRepository.save(any(Movie.class))).thenAnswer(i -> i.getArgument(0));

        MovieHandler handler = movieService.addMovie(req);
        assertNotNull(handler);
    }

    @DisplayName("Update a movie - Success Scenario")
    @Test
    public void updateMovie_shouldReturnString() {
        Movie movie = Movie.builder().id(1L).posterId("user").title("Old").year(2020).build();
        MovieRequest req = MovieRequest.builder().posterId("user").title("New").year(2021).build();
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(genreRepository.findByGenreIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(genreRepository.save(any(Genre.class))).thenReturn(Genre.builder().genre("New").build());
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        String result = movieService.updateMovie(1L, req);
        assertTrue(result.contains("Movie updated successfully"));
    }

    @DisplayName("Delete a movie - Success Scenario")
    @Test
    public void deleteMovie_shouldReturnString() {
        Movie movie = Movie.builder().id(1L).posterId("user").build();
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(jwtUtil.validate(anyString())).thenReturn(Jwts.parser().parseClaimsJws("dummy"));
        doNothing().when(movieRepository).delete(movie);

        String result = movieService.deleteMovie(1L, "Bearer token");
        assertEquals("Movie deleted successfully", result);
    }
}