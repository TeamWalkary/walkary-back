package com.walkary.controller;

import com.walkary.config.security.security.jwt.JwtProvider;
import com.walkary.models.SortType;
import com.walkary.models.dto.MainPinsResponse;
import com.walkary.service.PointMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apis/main")
public class MainUiController {

    private final PointMapService pointMapService;


    @GetMapping("/maps-pin")
    @PreAuthorize("hasRole(ROLE_USER)")
    public ResponseEntity<MainPinsResponse> getMapPins(
            HttpServletRequest httpRequest,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "sortBy", defaultValue = "OLDEST") SortType sortType
    ) {
        final String userId = JwtProvider.extractUserId(httpRequest);
        try {
            final LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
            return ResponseEntity.ok(
                    new MainPinsResponse(pointMapService.getMapList(userId, parsedDate, sortType))
            );
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("잘못된 날짜형식입니다");
        }
    }

}
