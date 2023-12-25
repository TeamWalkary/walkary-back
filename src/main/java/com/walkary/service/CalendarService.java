package com.walkary.service;

import com.walkary.models.dto.response.calendar.CalendarResponse;
import com.walkary.repository.DiaryRepository;
import com.walkary.repository.PointMapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarService {
    private final DiaryRepository diaryRepository;
    private final PointMapRepository pointMapRepository;

    @Transactional
    public CalendarResponse check(LocalDate date, String userId) {
        int year = date.getYear();
        int month = date.getMonthValue();

        //달력 일기·핀 유무 체크
        List<Integer> diaryDayList = diaryRepository.findByMonth(year, month, userId);
        List<Integer> pointMapDayList = pointMapRepository.findByMonth(year, month, userId);

        if (diaryDayList == null && pointMapDayList == null) {
            throw new IllegalArgumentException("해당 월엔 일기 또는 핀을 작성하지 않으셨습니다");
        }

        String diaryDays = diaryDayList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        String pointMapDays = pointMapDayList.stream()
                .map(String::valueOf)
                .distinct()
                .collect(Collectors.joining(", "));

        return CalendarResponse.builder().diaryDay(diaryDays).pinDay(pointMapDays).build();
    }
}
