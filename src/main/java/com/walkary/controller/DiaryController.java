package com.walkary.controller;

import com.walkary.models.dto.MessageResponse;
import com.walkary.models.dto.request.DiaryCreate;
import com.walkary.models.dto.request.DiaryEdit;
import com.walkary.models.dto.response.DiaryResponse;
import com.walkary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

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
    public ResponseEntity<DiaryResponse> findDiaryByDate(@RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyyMMdd") LocalDate date, HttpServletRequest request) {
        String userId = extractUserId(request);
        DiaryResponse diaryResponse = diaryService.findDiaryByDate(userId, date != null ? date : LocalDate.now());
        System.out.println("diaryResponse = " + diaryResponse);
        try {
            return ResponseEntity.ok(diaryResponse);
        } catch (Exception e) {
            log.info("error={}", e.getMessage());
            throw new IllegalArgumentException("일기를 조회할 수 없습니다.");
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
