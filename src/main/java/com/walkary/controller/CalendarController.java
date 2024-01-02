package com.walkary.controller;

import com.walkary.config.security.security.jwt.JwtProvider;
import com.walkary.models.dto.MessageResponse;
import com.walkary.models.dto.response.calendar.CalendarResponse;
import com.walkary.service.CalendarService;
import com.walkary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/apis/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/check")
    @PreAuthorize("hasRole(ROLE_USER)")
    public ResponseEntity<?> checkCalendar(HttpServletRequest httpRequest,
                                           @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyyMMdd") LocalDate date) {
        final String userId = JwtProvider.extractUserId(httpRequest);

        if (date == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("해당하는 연도와 월을 보내주세요"));
        }

        CalendarResponse response = calendarService.check(date, userId);

        return ResponseEntity.ok().body(response);
    }
}
