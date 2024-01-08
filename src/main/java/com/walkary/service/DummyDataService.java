package com.walkary.service;

import com.walkary.models.entity.Diary;
import com.walkary.models.entity.UserEntity;
import com.walkary.repository.DiaryRepository;
import com.walkary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DummyDataService {
    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;

    public void insertDummyData() {
        LocalDate startDate = LocalDate.of(2023, 12, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        UserEntity test1234 = userRepository.findById("test1234")
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        List<Diary> diaries = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            Diary diary = Diary.builder()
                    .date(date)
                    .content("일기 " + date.getMonthValue() + date.getDayOfMonth())
                    .user(test1234)
                    .build();
            diaries.add(diary);
        }
        diaryRepository.saveAll(diaries);
    }
}
