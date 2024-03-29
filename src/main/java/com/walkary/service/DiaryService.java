package com.walkary.service;

import com.walkary.exception.diary.DiaryNotFoundException;
import com.walkary.models.dto.DiaryWithAttachmentDTO;
import com.walkary.models.dto.request.diary.DiaryCreate;
import com.walkary.models.dto.request.diary.DiaryEdit;
import com.walkary.models.dto.request.diary.DiaryEditor;
import com.walkary.models.dto.response.diary.DiaryListResponse;
import com.walkary.models.dto.response.diary.DiaryListWrapper;
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

        if (!diaryRepository.existsByDateAndUserId(LocalDate.now(), diaryCreate.getUserId())) {
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
        } else {
            throw new IllegalArgumentException("일기는 하루에 하나만 작성할 수 있습니다");
        }
    }

    //일기 모아보기(검색 날짜 없음)
    @Transactional
    public DiaryListWrapper findListByUserId(Pageable pageable, String userId) {
        Page<DiaryWithAttachmentDTO> diaryPage = diaryRepository.findDiariesWithMediaByUserId(pageable, userId);

        // 다음 페이지가 있는지 확인(마지막 페이지인지 확인)
        long total = diaryRepository.countDiariesByUserId(userId);
        boolean hasNextPage = (long) pageable.getPageSize() * (pageable.getPageNumber() + 1) < total;

        List<DiaryListResponse> diaries = diaryPage
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

        return new DiaryListWrapper(hasNextPage, diaries);
    }

    //일기 모아보기(검색 날짜 유)
    @Transactional
    public DiaryListWrapper findListByUserIdAndDate(Pageable pageable, String userId, LocalDate startDate, LocalDate endDate) {
        Page<DiaryWithAttachmentDTO> diaryPage = diaryRepository.findDiariesWithMediaByUserIdAndDate(pageable, userId, startDate, endDate);

        // 다음 페이지가 있는지 확인(마지막 페이지인지 확인)
        long total = diaryRepository.countDiariesByUserIdAndDate(userId, startDate, endDate);
        boolean hasNextPage = (long) pageable.getPageSize() * (pageable.getPageNumber() + 1) < total;

        List<DiaryListResponse> diaries = diaryPage
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

        return new DiaryListWrapper(hasNextPage, diaries);
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
                .orElseThrow(() -> new DiaryNotFoundException());

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
