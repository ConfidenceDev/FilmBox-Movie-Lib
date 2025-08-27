package com.filmbox.MovieService.service;

import com.filmbox.MovieService.config.JwtUtil;
import com.filmbox.MovieService.config.TokenBlacklist;
import com.filmbox.MovieService.entity.*;
import com.filmbox.MovieService.exception.MovieException;
import com.filmbox.MovieService.model.MovieHandler;
import com.filmbox.MovieService.model.MovieRequest;
import com.filmbox.MovieService.model.MovieResponse;
import com.filmbox.MovieService.model.UserResponse;
import com.filmbox.MovieService.respository.ActorRepository;
import com.filmbox.MovieService.respository.GenreRepository;
import com.filmbox.MovieService.respository.MovieRepository;
import com.filmbox.MovieService.respository.TagRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.Instant;

import java.util.List;

@Service
@Log4j2
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklist tokenBlacklist;

    //========= User Logic ======================
    @Override
    public UserResponse signIn(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new MovieException("Username required", "UNAUTHORIZED", 204);
        }
        String token = jwtUtil.generateToken(username);
        return new UserResponse(username, token, "Signed in successfully");
    }

    @Override
    public UserResponse signOut(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
           throw new MovieException("Invalid Authorization Header", "UNAUTHORIZED", 401);
        }
        String token = authHeader.substring(7);
        String username = null;

        try {
            var claims = jwtUtil.validate(token).getBody();
            username = claims.getSubject(); // extract username
            Instant exp = claims.getExpiration().toInstant();
            tokenBlacklist.blacklist(token, exp);
            log.info("Signing out user: {}, token exp: {}", username, exp);
        } catch (Exception e) {
            // token invalid or expired"
            log.warn("Invalid/expired token during sign out: {}", e.getMessage());
        }

        return new UserResponse(
                username != null ? username : "anonymous",
                null, // remove token on sign-out
                "Signed out successfully"
        );
    }

    //========= Movie Logic ======================
    @Override
    public List<MovieResponse> getAllMovies(int page, int size, String sortBy, String sortDir, String searchBy) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        if ("all".equalsIgnoreCase(searchBy)) {
            Page<Movie> allMovies = movieRepository.findAll(pageable);
            if (allMovies.hasContent()) {
                return mapToResponse(allMovies.getContent());
            } else {
                return List.of(
                        MovieResponse.builder()
                                .message("No movie Found")
                                .build()
                );
            }
        }

        // 1. Search in Movies table
        Page<Movie> movies = movieRepository
                .findByTitleContainingIgnoreCaseOrSummaryContainingIgnoreCase(searchBy, searchBy, pageable);

        if (movies.hasContent()) {
            return mapToResponse(movies.getContent());
        }

        // 2. Search in Actors table
        movies = actorRepository.findMoviesByActorName(searchBy, pageable);
        if (movies.hasContent()) {
            return mapToResponse(movies.getContent());
        }

        // 3. Search in Genres table
        movies = genreRepository.findMoviesByGenre(searchBy, pageable);
        if (movies.hasContent()) {
            return mapToResponse(movies.getContent());
        }

        // 4. Search in Tags table
        movies = tagRepository.findMoviesByTag(searchBy, pageable);
        if (movies.hasContent()) {
            return mapToResponse(movies.getContent());
        }

        // Otherwise - Nothing found
        return List.of(MovieResponse.builder()
                .message("No movie Found")
                .build());
    }

    @Override
    public MovieResponse getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieException("Movie not found with ID: " + id, "NOT_FOUND", 404));

        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .summary(movie.getSummary())
                .year(movie.getYear())
                .genre(movie.getGenre() != null ? movie.getGenre().getGenre() : null)
                .actors(movie.getActors().stream().map(Actor::getName).toList())
                .tags(movie.getTags().stream().map(Tag::getTag).toList())
                .build();
    }

    private List<MovieResponse> mapToResponse(List<Movie> movies) {
        return movies.stream().map(movie -> MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .summary(movie.getSummary())
                .year(movie.getYear())
                .genre(movie.getGenre() != null ? movie.getGenre().getGenre() : null)
                .actors(movie.getActors().stream().map(Actor::getName).toList())
                .tags(movie.getTags().stream().map(Tag::getTag).toList())
                .build()).toList();
    }

    @Override
    public MovieHandler addMovie(MovieRequest movieRequest) {
        // Find or create Genre
        Genre genre = genreRepository.findByGenreIgnoreCase(movieRequest.getGenre())
                .orElseGet(() -> genreRepository.save(Genre.builder().genre(movieRequest.getGenre()).build()));

        // Create Movie
        Movie movie = Movie.builder()
                .title(movieRequest.getTitle())
                .summary(movieRequest.getSummary())
                .year(movieRequest.getYear())
                .genre(genre)
                .build();

        // Add Actors
        if (movieRequest.getActors() != null) {
            for (String actorName : movieRequest.getActors()) {
                Actor actor = Actor.builder().name(actorName).movie(movie).build();
                movie.getActors().add(actor);
            }
        }

        // Add Tags
        if (movieRequest.getTags() != null) {
            for (String tagName : movieRequest.getTags()) {
                Tag tag = tagRepository.findByTagIgnoreCase(tagName)
                        .orElseGet(() -> tagRepository.save(Tag.builder().tag(tagName).build()));
                movie.getTags().add(tag);
            }
        }

        Movie saved = movieRepository.save(movie);
        return new MovieHandler(saved.getId(), "Movie Added Successfully");
    }

    @Override
    public String updateMovie(Long id, MovieRequest movieRequest) {
        // Find movie
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieException("Movie not found", "NOT_FOUND", 404));

        // Update fields
        movie.setTitle(movieRequest.getTitle());
        movie.setSummary(movieRequest.getSummary());
        movie.setYear(movieRequest.getYear());

        // Update genre
        if (movieRequest.getGenre() != null) {
            Genre genre = genreRepository.findByGenreIgnoreCase(movieRequest.getGenre())
                    .orElseGet(() -> genreRepository.save(Genre.builder().genre(movieRequest.getGenre()).build()));
            movie.setGenre(genre);
        }

        // Update actors
        // Since it is not a patch request, all actors will be cleared and re-saved
        movie.getActors().clear();
        if (movieRequest.getActors() != null) {
            for (String actorName : movieRequest.getActors()) {
                Actor actor = Actor.builder().name(actorName).movie(movie).build();
                movie.getActors().add(actor);
            }
        }

        // Update tags
        // Same logic with actors
        movie.getTags().clear();
        if (movieRequest.getTags() != null) {
            for (String tagName : movieRequest.getTags()) {
                Tag tag = tagRepository.findByTagIgnoreCase(tagName)
                        .orElseGet(() -> tagRepository.save(Tag.builder().tag(tagName).build()));
                movie.getTags().add(tag);
            }
        }

        movieRepository.save(movie);
        return "Movie updated successfully with ID " + movie.getId();
    }

    @Override
    public String deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieException("Movie not found", "NOT_FOUND", 404));
        movieRepository.delete(movie);
        return "Movie deleted successfully";
    }
}
