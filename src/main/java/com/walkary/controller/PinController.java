package com.walkary.controller;

import com.walkary.config.security.security.jwt.JwtProvider;
import com.walkary.models.SortType;
import com.walkary.models.dto.MessageResponse;
import com.walkary.models.dto.request.pin.PinCreateRequest;
import com.walkary.models.dto.request.pin.PinEditRequests;
import com.walkary.models.dto.response.pin.AllMainPinsResponse;
import com.walkary.service.PointMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.DateTimeException;

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

    @PatchMapping
    @PreAuthorize("hasRole(ROLE_USER)")
    public ResponseEntity<MessageResponse> edit(
            HttpServletRequest httpRequest,
            @RequestBody PinEditRequests requests
    ) {
        pointMapService.edit(requests);


        return ResponseEntity.ok(
                new MessageResponse("핀이 수정되었습니다.")
        );
    }

    @DeleteMapping("/{pinId}")
    @PreAuthorize("hasRole(ROLE_USER)")
    public ResponseEntity<MessageResponse> delete(
            HttpServletRequest httpRequest,
            @PathVariable Long pinId
    ) {
        final String userId = JwtProvider.extractUserId(httpRequest);

        pointMapService.delete(userId, pinId);

        return ResponseEntity.ok(
                new MessageResponse("핀이 삭제되었습니다.")
        );

    }

    @GetMapping
    @PreAuthorize("hasRole(ROLE_USER)")
    public ResponseEntity<AllMainPinsResponse> getAllMapPins(
            HttpServletRequest httpRequest,
            @RequestParam(value = "sortBy", defaultValue = "OLDEST") SortType sortType
    ) {
        final String userId = JwtProvider.extractUserId(httpRequest);
        try {
            return ResponseEntity.ok(
                    new AllMainPinsResponse(pointMapService.getAllDateMaps(userId, sortType))
            );
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("잘못된 날짜형식입니다");
        }
    }
}
