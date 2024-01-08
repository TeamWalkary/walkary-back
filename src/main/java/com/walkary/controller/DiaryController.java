package com.walkary.controller;

import com.walkary.models.dto.MessageResponse;
import com.walkary.models.dto.request.diary.DiaryCreate;
import com.walkary.models.dto.request.diary.DiaryEdit;
import com.walkary.models.dto.response.diary.DiaryListResponse;
import com.walkary.models.dto.response.diary.DiaryListWrapper;
import com.walkary.models.dto.response.diary.DiaryResponse;
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
import java.time.format.DateTimeFormatter;
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
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
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
    public ResponseEntity<DiaryListWrapper> findListByUserIdAndDate(
            @RequestParam(name = "limit", required = false) String limit,
            @RequestParam(name = "offset", required = false) String offset,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "search", required = false) String searchParam,
            HttpServletRequest request) {

        String userId = extractUserId(request);

        //페이지 세팅
        int page = (offset != null) ? Integer.parseInt(offset) : 0;
        int size = (limit != null) ? Integer.parseInt(limit) : 5;

        Sort sort = (sortBy != null && sortBy.equals("latest")) ? Sort.by(Sort.Order.desc("date")) : Sort.by(Sort.Order.asc("date"));
        Pageable pageable = PageRequest.of(page, size, sort);

        try {
            DiaryListWrapper diaryListWrapper;

            if (searchParam != null && !searchParam.trim().isEmpty()) {
                //검색 조건이 있을때

                // searchParam 시작날짜, 종료날짜 추출
                String cleanedString = searchParam.trim().replace("\n", "");
                int dashIndex = cleanedString.indexOf("-");
                String startDateStr = cleanedString.substring(0, dashIndex);
                String endDateStr = cleanedString.substring(dashIndex + 1);

                //날짜 형식 변환
                LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.BASIC_ISO_DATE);
                LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.BASIC_ISO_DATE);

                diaryListWrapper = diaryService.findListByUserIdAndDate(pageable, userId, startDate, endDate);
            } else {
                //검색 조건이 없을 때
                diaryListWrapper = diaryService.findListByUserId(pageable, userId);
            }

            return ResponseEntity.ok(diaryListWrapper);
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
