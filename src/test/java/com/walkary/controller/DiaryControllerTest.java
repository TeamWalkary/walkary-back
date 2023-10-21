package com.walkary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walkary.models.dto.request.DiaryCreate;
import com.walkary.models.entity.Diary;
import com.walkary.models.entity.User;
import com.walkary.repository.DiaryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class DiaryControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DiaryRepository diaryRepository;


    //일기 작성 테스트
    @Test
    @DisplayName("일기작성 테스트")
    public void write() throws Exception {
        //given
        DiaryCreate request = DiaryCreate.builder()
                .date(LocalDate.now())
                .content("내용입니다")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/write")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("내용입니다"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 페이징 처리해서 조회")
    public void findListByMemberId() throws Exception {
        //given
        User user = User.builder()
                .password("1234")
                .nickname("test1")
                .build();

        List<Diary> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Diary.builder()
                        .content("bar" + i)
                        .build())
                .collect(Collectors.toList());

        diaryRepository.saveAll(requestPosts);


        //expected
        mockMvc.perform(get("/diary/list?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("foo19"))
                .andExpect(jsonPath("$[0].content").value("bar19"))
                .andDo(print());
    }
}