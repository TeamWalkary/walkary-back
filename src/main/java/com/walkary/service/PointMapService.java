package com.walkary.service;

import com.walkary.models.SortType;
import com.walkary.models.dto.request.pin.PinEditRequest;
import com.walkary.models.dto.request.pin.PinEditRequests;
import com.walkary.models.dto.request.pin.PinEditor;
import com.walkary.models.dto.response.pin.AllDatePinResponse;
import com.walkary.models.dto.response.pin.AllMainPinsResponse;
import com.walkary.models.dto.response.pin.PinResponse;
import com.walkary.models.entity.PointMap;
import com.walkary.models.entity.UserEntity;
import com.walkary.repository.PointMapRepository;
import com.walkary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.walkary.models.SortType.LATEST;

@Service
@RequiredArgsConstructor
public class PointMapService {

    private final PointMapRepository repository;
    private final UserRepository userRepository;


    public void create(final String userId, final String content, final Point point) {
        final UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("잘못된 userId입니다 다시 확인해주세요.")
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

    @Transactional
    public void edit(final PinEditRequests pinEditRequests) {
        for (PinEditRequest request : pinEditRequests.pinList()){
            //핀 유무 확인
            PointMap pointMap = repository.findById(request.id()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 핀입니다"));

            // 변경이 필요한 경우에만 수정
            if (request.contents() != null && !request.contents().equals("") && !request.contents().equals(pointMap.getContent())) {
                PinEditor.PinEditorBuilder editBuilder = pointMap.toEditor();
                editBuilder.content(request.contents());
                pointMap.edit(editBuilder.build());
            }
        }
    }

    @Transactional
    public void delete(String userId, Long pinId) {
        //핀 존재하는지 확인
        PointMap pointMap = repository.findById(pinId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 핀입니다"));

        //현재 사용자와 핀 작성자가 일치하는지 확인
        if (!pointMap.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("핀 작성자와 현 사용자의 아이디가 동일하지 않습니다");
        }

        repository.delete(pointMap);
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

    public LinkedHashMap<String, List<AllDatePinResponse>> getAllDateMaps(final String userId, final SortType sortType) {
        List<AllDatePinResponse> allDatePinResponses = repository.findAllByUserId(userId, Sort.by(Sort.Direction.DESC, "id")).stream().map(point ->
                new AllDatePinResponse(
                        point.getId(),
                        point.getContent(),
                        point.getPoint().getX(),
                        point.getPoint().getY(),
                        point.getCreatedAt().toLocalDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        point.getCreatedAt().toLocalDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                )
        ).collect(Collectors.toList());

        // 날짜별로 리스트 정렬
        allDatePinResponses.sort(sortType.equals(LATEST)
                ? Comparator.comparing(AllDatePinResponse::date).reversed()
                : Comparator.comparing(AllDatePinResponse::date));

        // 날짜별로 그룹화
        return allDatePinResponses.stream()
                .collect(Collectors.groupingBy(AllDatePinResponse::date, LinkedHashMap::new, Collectors.toList()));
    }
}
