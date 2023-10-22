package com.walkary.service;

import com.walkary.models.dto.request.DiaryCreate;
import com.walkary.models.dto.request.DiaryEdit;
import com.walkary.models.dto.request.DiaryEditor;
import com.walkary.models.dto.response.DiaryResponse;
import com.walkary.models.entity.Diary;
import com.walkary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public DiaryResponse edit(Long id, DiaryEdit diaryEdit) {
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다"));

        DiaryEditor.DiaryEditorBuilder editBuilder = diary.toEditor();

        if (diaryEdit.getDate() != null) {
            editBuilder.date(diaryEdit.getDate());
        }

        if (diaryEdit.getContent() != null) {
            editBuilder.content(diaryEdit.getContent());
        }

        diary.edit(editBuilder.build());

        return new DiaryResponse(diary);
    }

    public List<DiaryResponse> getList(Pageable pageable) {

        return diaryRepository.findAll(pageable).stream()
                .map(DiaryResponse::new)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않은 글입니다"));

        diaryRepository.delete(diary);
    }
}
