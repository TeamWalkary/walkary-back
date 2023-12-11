package com.walkary.models.dto.request.pin;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.geo.Point;

@Getter
public class PinEditor {
    String content;
    Point point;

    @Builder
    public PinEditor(String content, Point point) {
        this.content = content;
        this.point = point;
    }
}
