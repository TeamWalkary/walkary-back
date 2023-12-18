package com.walkary.controller;

import com.walkary.config.security.security.jwt.JwtProvider;
import com.walkary.models.SortType;
import com.walkary.models.dto.response.pin.MainPinsResponse;
import com.walkary.service.PointMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.DateTimeException;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apis/main")
public class MainUiController {

    private final PointMapService pointMapService;


    @GetMapping("/maps-pin")
    @PreAuthorize("hasRole(ROLE_USER)")
    public ResponseEntity<MainPinsResponse> getMapPins(
            HttpServletRequest httpRequest,
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyyMMdd") LocalDate date,
            @RequestParam(value = "sortBy", defaultValue = "OLDEST") SortType sortType
    ) {
        final String userId = JwtProvider.extractUserId(httpRequest);
        try {
            date = (date != null) ? date : LocalDate.now();
            return ResponseEntity.ok(
                    new MainPinsResponse(pointMapService.getMapList(userId, date, sortType))
            );
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("잘못된 날짜형식입니다");
        }
    }

}
