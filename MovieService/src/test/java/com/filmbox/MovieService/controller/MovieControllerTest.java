package com.filmbox.MovieService.controller;

import com.filmbox.MovieService.config.JwtUtil;
import com.filmbox.MovieService.model.MovieRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private String token;

    @BeforeEach
    void setUp() {
        // generate a fresh token before each test
        token = jwtUtil.generateToken("test123");
    }


    @DisplayName("Get all movies with default request params - Success Scenario")
    @Test
    public void getAllMovies_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/v1/movies"))
                .andExpect(status().isOk());
    }

    @DisplayName("Add a new movie - Success Scenario")
    @Test
    public void addMovie_shouldReturnCreated() throws Exception {
        MovieRequest req = MovieRequest.builder()
                .posterId("test123")
                .title("Test Movie")
                .summary("This is a test movie")
                .year(2022)
                .genre("Action")
                .build();

        mockMvc.perform(post("/api/v1/movies")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }


    @DisplayName("Get a movie by id - Not Found")
    @Test
    void getMovieById_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/movies/9999"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Update a movie - Not Found")
    @Test
    void updateMovie_shouldReturnNotFound() throws Exception {
        MovieRequest req = MovieRequest.builder()
                .posterId("test123")
                .title("Test Movie")
                .summary("This is a test movie")
                .year(2022)
                .genre("Action")
                .build();

        mockMvc.perform(put("/api/v1/movies/9999")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Delete a movie - Not Found")
    @Test
    void deleteMovie_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/movies/9999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

}