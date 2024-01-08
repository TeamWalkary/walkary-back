package com.walkary.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DiaryDummyTest {
    @Autowired
    private DummyDataService dummyDataService;

    @Test
    @DisplayName("더미 데이터 입력")
    public void dummyDataInsert() throws Exception {
        dummyDataService.insertDummyData();
    }
}
