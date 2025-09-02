package com.filmbox.MovieService.service;

import com.filmbox.MovieService.config.JwtUtil;
import com.filmbox.MovieService.config.TokenBlacklist;
import com.filmbox.MovieService.entity.*;
import com.filmbox.MovieService.exception.MovieException;
import com.filmbox.MovieService.model.*;
import com.filmbox.MovieService.respository.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceImplTest {

    @InjectMocks
    private MovieServiceImpl movieService;

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private ActorRepository actorRepository;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private TokenBlacklist tokenBlacklist;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Get all movies - Success Scenario")
    @Test
    public void getAllMovies_shouldReturnList() {
        Movie movie = Movie.builder()
                .id(1L)
                .posterId("test123")
                .title("Test movie")
                .summary("This is a test movie")
                .year(2022)
                .actors(new HashSet<>()) // initialize to avoid NPE
                .tags(new HashSet<>())
                .build();

        Page<Movie> page = new PageImpl<>(List.of(movie));
        when(movieRepository.findAll(any(Pageable.class))).thenReturn(page);
        List<MovieResponse> result = movieService.getAllMovies(0, 2, "id", "asc", "all");
        assertFalse(result.isEmpty());
    }

    @DisplayName("Get a movie by Id - Success Scenario")
    @Test
    public void getMovieById_shouldReturnMovieResponse() {
        Movie movie = Movie.builder().id(1L).posterId("test123").title("Test movie")
                .actors(new HashSet<>())
                .tags(new HashSet<>())
                .year(2022)
                .build();

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        MovieResponse response = movieService.getMovieById(1L);
        assertEquals(1L, response.getId());
    }

    @DisplayName("Sign in - Success Scenario")
    @Test
    void signIn_shouldReturnUserResponse() {
        UserRequest req = new UserRequest("test123");
        when(jwtUtil.generateToken("test123")).thenReturn("testToken");
        UserResponse res = movieService.signIn(req);
        assertEquals("test123", res.username());
        assertEquals("testToken", res.token());
        assertEquals("Signed in successfully", res.message());
    }

    @DisplayName("Add movie - Success Scenario")
    @Test
    void addMovie_shouldReturnMovieHandler() {
        MovieRequest req = MovieRequest.builder()
                .posterId("test123")
                .title("Test Movie")
                .summary("Summary")
                .year(2022)
                .genre("Action")
                .actors(List.of("Actor1"))
                .tags(List.of("Tag1"))
                .build();
        Genre genre = Genre.builder().genre("Action").build();
        when(genreRepository.findByGenreIgnoreCase("Action")).thenReturn(Optional.of(genre));
        when(movieRepository.save(any(Movie.class))).thenAnswer(inv -> {
            Movie m = inv.getArgument(0);
            m.setId(1L);
            return m;
        });
        when(tagRepository.findByTagIgnoreCase(anyString())).thenReturn(Optional.empty());
        MovieHandler handler = movieService.addMovie(req);
        assertEquals(1L, handler.id());
        assertEquals("Movie Added Successfully", handler.message());
    }

    @DisplayName("Delete movie - Success")
    @Test
    void deleteMovie_shouldDeleteIfPoster() {
        Movie movie = Movie.builder().id(1L).posterId("test123").build();
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(jwtUtil.getUsername(anyString())).thenReturn("test123");
        doNothing().when(movieRepository).delete(movie);
        String msg = movieService.deleteMovie(1L, "Bearer testToken");
        assertEquals("Movie deleted successfully", msg);
    }
}