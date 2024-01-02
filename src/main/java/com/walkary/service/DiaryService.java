package com.walkary.service;

import com.walkary.models.dto.DiaryWithAttachmentDTO;
import com.walkary.models.dto.request.diary.DiaryCreate;
import com.walkary.models.dto.request.diary.DiaryEdit;
import com.walkary.models.dto.request.diary.DiaryEditor;
import com.walkary.models.dto.response.diary.DiaryListResponse;
import com.walkary.models.dto.response.diary.DiaryResponse;
import com.walkary.models.entity.Diary;
import com.walkary.models.entity.DiaryMedia;
import com.walkary.models.entity.UserEntity;
import com.walkary.repository.DiaryMediaRepository;
import com.walkary.repository.DiaryRepository;
import com.walkary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
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

    //일기 작성
    @Transactional
    public void write(DiaryCreate diaryCreate) {
        UserEntity user = userRepository.findById(diaryCreate.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다"));

        Diary diary = Diary.builder()
                .title(diaryCreate.getTitle())
                .content(diaryCreate.getContent())
                .date(diaryCreate.getDate() != null ? diaryCreate.getDate() : LocalDate.now())
                .user(user)
                .build();
        diaryRepository.save(diary);

        DiaryMedia diaryMedia = DiaryMedia.builder()
                .attachment(diaryCreate.getImage().getBytes(StandardCharsets.UTF_8))
                .diary(diary)
                .build();
        diaryMediaRepository.save(diaryMedia);
    }

    //일기 모아보기
    @Transactional
    public List<DiaryListResponse> findListByUserId(Pageable pageable, String userId) {
        Page<DiaryWithAttachmentDTO> diaryPage = diaryRepository.findDiariesWithMediaByUserId(pageable, userId);

        return diaryPage
                .getContent()
                .stream()
                .map(diaryDto -> DiaryListResponse.builder()
                        .id(diaryDto.getId())
                        .date(diaryDto.getDate())
                        .title(diaryDto.getTitle())
                        .content(diaryDto.getContent())
                        .image(diaryDto.getAttachment())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public DiaryResponse edit(Long diaryId, DiaryEdit diaryEdit) {
        //다이어리 정보 수정
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다"));

        DiaryEditor.DiaryEditorBuilder editBuilder = diary.toEditor();

        if (diaryEdit.getDate() != null && !diaryEdit.getDate().equals("")) {
            editBuilder.date(diaryEdit.getDate());
        }

        if (diaryEdit.getTitle() != null && !diaryEdit.getTitle().equals("")) {
            editBuilder.title(diaryEdit.getTitle());
        }

        if (diaryEdit.getContent() != null && !diaryEdit.getContent().equals("")) {
            editBuilder.content(diaryEdit.getContent());
        }


        //사진 정보 수정
        DiaryMedia diaryMedia = diaryMediaRepository.findByDiaryId(diaryId).orElseThrow(() -> new IllegalArgumentException("글 또는 사진이 존재하지 않습니다"));

        if (diaryEdit.getImage() != null && !diaryEdit.getImage().equals("")) {
            diaryMedia.edit(diaryEdit.getImage());
        }

        diary.edit(editBuilder.build());

        return new DiaryResponse(diary, diaryEdit.getImage());
    }

    public List<DiaryResponse> getList(Pageable pageable) {

        return diaryRepository.findAll(pageable).stream()
                .map(DiaryResponse::new)
                .collect(Collectors.toList());
    }

    public DiaryResponse findDiaryByDate(String userId, LocalDate date) {
        Diary diary = diaryRepository.findByDateAndUserId(userId, date)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다"));

        if (diary == null) {
            return DiaryResponse.builder().build();
        }

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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다"));

        diaryMediaRepository.deleteByDiaryId(diary.getId());
        diaryRepository.delete(diary);
    }
}
