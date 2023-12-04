package com.walkary.controller;

import com.walkary.models.dto.MessageResponse;
import com.walkary.models.dto.request.DiaryCreate;
import com.walkary.models.dto.request.DiaryEdit;
import com.walkary.models.dto.response.DiaryListResponse;
import com.walkary.models.dto.response.DiaryResponse;
import com.walkary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.walkary.config.security.security.jwt.JwtProvider.extractUserId;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/apis")
public class DiaryController {

    private final DiaryService diaryService;

    //일기 작성
    @PostMapping("/diary")
    @PreAuthorize("hasRole(ROLE_USER)")
    public ResponseEntity<MessageResponse> write(@RequestBody DiaryCreate request, HttpServletRequest servletRequest) {
        String userId = extractUserId(servletRequest);
        request.setUserId(userId);
        try {
            diaryService.write(request);
            return ResponseEntity.ok(new MessageResponse("일기가 작성되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("일기 작성에 실패하였습니다."));
        }
    }

    //오늘의 일기 조회하기
    @GetMapping("/main/diary")
    @PreAuthorize("hasRole(ROLE_USER)")
    public ResponseEntity<?> findDiaryByDate(@RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyyMMdd") LocalDate date, HttpServletRequest request) {
        String userId = extractUserId(request);
        DiaryResponse diaryResponse = diaryService.findDiaryByDate(userId, date != null ? date : LocalDate.now());

        if (diaryResponse.getId() == null) {
            return ResponseEntity.ok(Arrays.copyOf(new int[0], 0));
        }


        try {
            return ResponseEntity.ok(diaryResponse);
        } catch (Exception e) {
            throw new IllegalArgumentException("일기를 조회할 수 없습니다.");
        }
    }

    //일기 모아보기
    @GetMapping("/collect/diary")
    @PreAuthorize("hasRole(ROLE_USER)")
    public ResponseEntity<List<DiaryListResponse>> findListByUserId(
            @RequestParam(name = "limit") String limit,
            @RequestParam(name = "offset") String offset,
            @RequestParam(name = "sortBy") String sortBy,
            HttpServletRequest request) {

        String userId = extractUserId(request);
        int page = (offset != null) ? Integer.parseInt(offset) : 0;
        int size = (limit != null) ? Integer.parseInt(limit) : 5;

        Sort sort = (sortBy.equals("latest")) ? Sort.by(Sort.Order.desc("id")) : Sort.by(Sort.Order.asc("id"));
        Pageable pageable = PageRequest.of(page, size, sort);

        List<DiaryListResponse> diaryList = diaryService.findListByUserId(pageable, userId);
        try {
            return ResponseEntity.ok(diaryList);
        } catch (Exception e) {
            throw new IllegalArgumentException("일기를 조회할 수 없습니다");
        }
    }

    //일기 수정
    @PatchMapping("/diary/{diaryId}")
    public ResponseEntity<MessageResponse> edit(@PathVariable Long diaryId, @RequestBody DiaryEdit request) {
        try {
            diaryService.edit(diaryId, request);
            return ResponseEntity.ok(new MessageResponse("수정되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("일기 수정에 실패하였습니다."));
        }
    }

    //일기 삭제
    @DeleteMapping("/main/diary/{diaryId}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long diaryId) {
        try {
            diaryService.delete(diaryId);
            return ResponseEntity.ok(new MessageResponse("일기가 삭제되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("일기 삭제하기에 실패하였습니다."));
        }
    }
}
