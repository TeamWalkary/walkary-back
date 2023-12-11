package com.walkary.service;

import com.walkary.models.SortType;
import com.walkary.models.dto.response.pin.PinResponse;
import com.walkary.models.entity.PointMap;
import com.walkary.models.entity.UserEntity;
import com.walkary.repository.PointMapRepository;
import com.walkary.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class PinServiceTest {
    @Autowired
    private PointMapService pointMapService;

    @Autowired
    private PointMapRepository pointMapRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("핀 수정")
    public void test1() throws Exception {
        //given
        final Timestamp now = Timestamp.from(Instant.now());

        Point point = new Point(123.1233, 23.22232);
        UserEntity user = userRepository.findById("zzz").orElseThrow(() ->
                new IllegalArgumentException("없는 유저입니다")
        );

        final PointMap pointMap = PointMap.builder()
                .point(point)
                .user(user)
                .content("컨텐츠")
                .date(now.toLocalDateTime().toLocalDate())
                .createdAt(now)
                .updatedAt(now)
                .build();
        pointMapRepository.save(pointMap);

        //when
        Point editedPoint = new Point(36, 36);
        pointMapService.edit(pointMap.getId(), "바뀐 컨텐츠", editedPoint);

        //then
        List<PinResponse> mapList = pointMapService.getMapList(user.getId(), LocalDate.now(), SortType.LATEST);
        Assertions.assertThat(mapList.get(0).contents()).isEqualTo("바뀐 컨텐츠");
        Assertions.assertThat(mapList.get(0).latitude()).isEqualTo(36);
        Assertions.assertThat(mapList.get(0).longitude()).isEqualTo(36);
    }
}
