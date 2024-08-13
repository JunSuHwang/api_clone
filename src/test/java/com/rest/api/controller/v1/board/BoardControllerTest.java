package com.rest.api.controller.v1.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.api.entity.User;
import com.rest.api.model.board.ParamsPost;
import com.rest.api.repo.UserJpaRepo;
import com.rest.api.repo.board.BoardJpaRepo;
import com.rest.api.repo.board.PostJpaRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("local")
@AutoConfigureMockMvc
@Transactional
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserJpaRepo userJpaRepo;

    @Autowired
    private BoardJpaRepo boardJpaRepo;

    @Autowired
    private PostJpaRepo postJpaRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    public void setUp() throws Exception {
        userJpaRepo.save(
                User.builder()
                        .uid("wnstn1234@naver.com")
                        .name("wnstn")
                        .password(passwordEncoder.encode("1234"))
                        .roles(Collections.singletonList("ROLE_USER"))
                        .build()
        );
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", "wnstn1234@naver.com");
        params.add("password", "1234");
        MvcResult result = mockMvc.perform(post("/v1/signin").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data").exists())
                .andReturn();
        String resultString = result.getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        token = jsonParser.parseMap(resultString).get("data").toString();
    }

    @Test
    void postTest() throws Exception{
        String boardName = "test";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("author", "testauthor");
        params.add("title", "testtitle");
        params.add("content", "aaaaaabbbbbcccccccc");
        params.add("boardName", boardName);
        mockMvc.perform(post("/v1/board/"+boardName)
                .header("X-AUTH-TOKEN", token)
                                .params(params)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}