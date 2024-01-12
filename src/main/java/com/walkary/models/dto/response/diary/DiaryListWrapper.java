package com.walkary.models.dto.response.diary;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class DiaryListWrapper {
    //hasNextPage를 따로 빼기 위한 DTO 객체
    private boolean hasNextPage;
    private List<DiaryListResponse> diaries;

    @Builder
    public DiaryListWrapper(boolean hasNextPage, List<DiaryListResponse> diaries) {
        this.hasNextPage = hasNextPage;
        this.diaries = diaries;
    }
}
