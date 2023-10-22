package com.walkary.service;

import com.walkary.models.entity.PointMap;
import com.walkary.models.entity.UserEntity;
import com.walkary.repository.PointMapRepository;
import com.walkary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PointMapService {

    private final PointMapRepository repository;
    private final UserRepository userRepository;


    public void create(final String userId, final String content, final Point point) {
        final UserEntity user = userRepository.findById(userId).orElseThrow(
                IllegalArgumentException::new
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
}
