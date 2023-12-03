package com.walkary.service;

import com.walkary.models.dto.request.DiaryEdit;
import com.walkary.models.entity.Diary;
import com.walkary.repository.DiaryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DiaryServiceTest {
    @Autowired
    private DiaryService diaryService;

    @Autowired
    private DiaryRepository diaryRepository;

    @BeforeEach
    void clean() {
        diaryRepository.deleteAll();
    }

    @Test
    @DisplayName("글 내용 수정")
    public void test1() throws Exception {
        //given
        Diary diary = Diary.builder()
                .date(LocalDate.of(2023, 10, 22))
                .content("푸르지오")
                .build();

        diaryRepository.save(diary);

        //클라이언트가 수정할 값 안 보내면.
        DiaryEdit diaryEdit = DiaryEdit.builder()
                .date(LocalDate.of(2023, 10, 22))
                .content("반포자이")
                .build();

        //when
        diaryService.edit(diary.getId(), diaryEdit);

        //then
        Diary changeDiary = diaryRepository.findById(diary.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id =" + diary.getId()));
        assertEquals("반포자이", changeDiary.getContent());
    }

    @Test
    @DisplayName("글 삭제")
    public void test2() throws Exception {
        //given
        Diary diary = Diary.builder()
                .date(LocalDate.of(2023, 10, 22))
                .content("푸르지오")
                .build();

        diaryRepository.save(diary);

        // when
        diaryService.delete(diary.getId());

        // then
        Assertions.assertEquals(0, diaryRepository.count());
    }
}