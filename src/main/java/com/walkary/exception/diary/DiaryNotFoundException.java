package com.walkary.exception.diary;

public class DiaryNotFoundException extends RuntimeException {

    public DiaryNotFoundException() {
        super("존재하지 않는 글입니다");
    }
}
