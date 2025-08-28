package com.filmbox.MovieService.respository;

import com.filmbox.MovieService.entity.Actor;
import com.filmbox.MovieService.entity.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    @Query("SELECT DISTINCT a.movie FROM Actor a " + "WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Movie> findMoviesByActorName(@Param("name") String name);
}