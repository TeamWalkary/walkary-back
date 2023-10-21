package com.walkary.service;

import com.walkary.models.dto.request.DiaryCreate;
import com.walkary.models.dto.request.DiaryEdit;
import com.walkary.models.dto.response.DiaryResponse;
import com.walkary.models.entity.Diary;
import com.walkary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

    //일기 작성
    public void write(DiaryCreate diaryCreate) {
        Diary diary = Diary.builder()
                .content(diaryCreate.getContent())
                .date(diaryCreate.getDate())
                .build();

        diaryRepository.save(diary);
    }

    //일기 모아보기
    public List<DiaryResponse> findListByMemberId(Pageable pageable, Long userId) {
        return diaryRepository.findListByMemberId(pageable, userId).stream()
                .map(DiaryResponse::new)
                .collect(Collectors.toList());
    }

    public void edit(Long id, DiaryEdit diaryEdit) {
        Diary diary = diaryRepository.findById(id)
                .orElse(null);

        DiaryEdit.DiaryEditBuilder editBuilder = diary.toEditor();

        DiaryEdit diaryEditor = editBuilder
                .date(diaryEdit.getDate())
                .content(diaryEdit.getContent())
                .build();

        System.out.println("diaryEditor = " + diaryEditor.toString());

        diary.edit(diaryEditor);
    }
}
