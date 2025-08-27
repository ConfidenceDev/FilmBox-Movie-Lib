package com.filmbox.MovieService.respository;


import com.filmbox.MovieService.entity.Movie;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, Long>{
}
