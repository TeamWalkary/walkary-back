package com.walkary.controller;

import com.walkary.config.security.security.jwt.JwtProvider;
import com.walkary.models.dto.MessageResponse;
import com.walkary.models.dto.PinCreateRequest;
import com.walkary.service.PointMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apis/pin")
public class PinController {

    private final PointMapService pointMapService;

    @PostMapping
    @PreAuthorize("hasRole(ROLE_USER)")
    public ResponseEntity<MessageResponse> create(
            HttpServletRequest httpRequest,
            @RequestBody PinCreateRequest request
    ) {
        final String userId = JwtProvider.extractUserId(httpRequest);
        pointMapService.create(userId, request.contents(), new Point(request.longitude(), request.latitude()));

        return ResponseEntity.ok(
                new MessageResponse("핀이 생성되었습니다.")
        );
    }
}
