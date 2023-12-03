package com.walkary.service;

import com.walkary.models.SortType;
import com.walkary.models.dto.PinResponse;
import com.walkary.models.entity.PointMap;
import com.walkary.models.entity.UserEntity;
import com.walkary.repository.PointMapRepository;
import com.walkary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointMapService {

    private final PointMapRepository repository;
    private final UserRepository userRepository;


    public void create(final String userId, final String content, final Point point) {
        final UserEntity user = userRepository.findById(userId).orElseThrow(
                () ->  new IllegalArgumentException("잘못된 userId입니다 다시 확인해주세요.")
        );
        final Timestamp now = Timestamp.from(Instant.now());
        final PointMap pointMap = PointMap.builder()
                        .point(point)
                        .user(user)
                        .content(content)
                        .date(now.toLocalDateTime().toLocalDate())
                        .createdAt(now)
                        .updatedAt(now)
                        .build();

        repository.save(pointMap);
    }

    public List<PinResponse> getMapList(final String userId, final LocalDate parsedDate, final SortType sortType) {
        Sort.Direction direction = switch (sortType) {
            case LATEST -> Sort.Direction.ASC;
            case OLDEST -> Sort.Direction.DESC;
        };
        return repository.findAllByUserIdAndDate(userId, parsedDate, Sort.by(direction, "id")).stream().map(point ->
                    new PinResponse(
                        point.getId(),
                        point.getContent(),
                        point.getPoint().getX(),
                        point.getPoint().getY(),
                        point.getCreatedAt().toLocalDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                )
        ).collect(Collectors.toList());
    }
}
