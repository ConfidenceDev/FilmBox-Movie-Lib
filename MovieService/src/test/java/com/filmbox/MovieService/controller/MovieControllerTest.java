package com.filmbox.MovieService.controller;

import com.filmbox.MovieService.model.MovieRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Get all creators - Success Scenario")
    @Test
    public void getAllMovies_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/v1/movies"))
                .andExpect(status().isOk());
    }

    @DisplayName("Get all creators - Success Scenario")
    @Test
    public void addMovie_shouldReturnCreated() throws Exception {
        MovieRequest req = MovieRequest.builder()
                .posterId("user")
                .title("Integration Movie")
                .year(2022)
                .genre("Drama")
                .build();

        mockMvc.perform(post("/api/v1/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }
}