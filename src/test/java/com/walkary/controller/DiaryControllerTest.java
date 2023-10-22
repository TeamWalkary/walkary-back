package com.walkary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walkary.models.dto.request.DiaryCreate;
import com.walkary.models.dto.request.DiaryEdit;
import com.walkary.models.entity.Diary;
import com.walkary.repository.DiaryRepository;
import org.junit.jupiter.api.BeforeEach;
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

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DiaryControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DiaryRepository diaryRepository;


    @BeforeEach
    void clean() {
        diaryRepository.deleteAll();
    }

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
        List<Diary> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Diary.builder()
                        .date(LocalDate.now())
                        .content("bar" + i)
                        .build())
                .collect(Collectors.toList());

        diaryRepository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/diary/list")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 내용 수정")
    public void edit() throws Exception {
        //given
        Diary diary = Diary.builder()
                .date(LocalDate.of(2023, 10, 22))
                .content("푸르지오")
                .build();
        diaryRepository.save(diary);

        //클라이언트가 수정할 값 안 보내면.
        DiaryEdit diaryEdit = DiaryEdit.builder()
                .date(null)
                .content("반포자이")
                .build();


        //expected
        mockMvc.perform(patch("/diary/{diaryId}", diary.getId()) //PATCH /diary/{postId}
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(diaryEdit)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 삭제")
    void deleteTest() throws Exception {
        //given
        Diary diary = Diary.builder()
                .date(LocalDate.of(2023, 10, 22))
                .content("푸르지오")
                .build();

        diaryRepository.save(diary);

        // when
        mockMvc.perform(delete("/diary/{diaryId}", diary.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}