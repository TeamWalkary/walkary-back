package com.walkary.service;

import com.walkary.models.dto.request.DiaryCreate;
import com.walkary.models.dto.request.DiaryEdit;
import com.walkary.models.dto.request.DiaryEditor;
import com.walkary.models.dto.response.DiaryResponse;
import com.walkary.models.entity.Diary;
import com.walkary.models.entity.DiaryMedia;
import com.walkary.models.entity.UserEntity;
import com.walkary.repository.DiaryMediaRepository;
import com.walkary.repository.DiaryRepository;
import com.walkary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

    private final DiaryMediaRepository diaryMediaRepository;

    private final UserRepository userRepository;

    @Transactional
    //일기 작성
    public void write(DiaryCreate diaryCreate) {
        UserEntity user = userRepository.findById(diaryCreate.getUserId()).orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다"));

        Diary diary = Diary.builder()
                .title(diaryCreate.getTitle())
                .content(diaryCreate.getContent())
                .date(diaryCreate.getDate() != null ? diaryCreate.getDate() : LocalDate.now())
                .user(user)
                .build();
        diaryRepository.save(diary);

        DiaryMedia diaryMedia = DiaryMedia.builder()
                .attachment(diaryCreate.getImage())
                .diary(diary)
                .build();
        diaryMediaRepository.save(diaryMedia);
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

        if (diaryEdit.getTitle() != null) {
            editBuilder.title(diaryEdit.getTitle());
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

    public DiaryResponse findDiaryByDate(String userId, LocalDate date) {
        Diary diary = diaryRepository.findByDate(userId, date);
        DiaryMedia diaryMedia = diaryMediaRepository.findByDiaryId(diary.getId()).orElse(null);

        return DiaryResponse.builder()
                .id(diary.getId())
                .title(diary.getTitle())
                .content(diary.getContent())
                .image(diaryMedia != null ? diaryMedia.getAttachment() : null)
                .build();
    }

    @Transactional
    public void delete(Long id) {
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 글입니다"));

        diaryMediaRepository.deleteByDiaryId(diary.getId());
        diaryRepository.delete(diary);
    }
}
