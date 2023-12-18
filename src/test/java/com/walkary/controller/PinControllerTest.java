//package com.walkary.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.walkary.models.entity.PointMap;
//import com.walkary.models.entity.UserEntity;
//import com.walkary.repository.PointMapRepository;
//import com.walkary.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.geo.Point;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.sql.Timestamp;
//import java.time.Instant;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class PinControllerTest {
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private PointMapRepository pointMapRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    @DisplayName("글 삭제")
//    public void deleteTest() throws Exception {
//        //given
//
//        final Timestamp now = Timestamp.from(Instant.now());
//
//        Point point = new Point(36, 80);
//        UserEntity user = userRepository.findById("zzz").orElseThrow(() ->
//                new IllegalArgumentException("없는 유저입니다")
//        );
//
//        final PointMap pointMap = PointMap.builder()
//                .point(point)
//                .user(user)
//                .content("삭제용 컨텐츠222")
//                .date(now.toLocalDateTime().toLocalDate())
//                .createdAt(now)
//                .updatedAt(now)
//                .build();
//        pointMapRepository.save(pointMap);
//
//        //expected
//        mockMvc.perform(delete("/apis/pin/{pinId}", pointMap.getId())                        )
//                .andExpect(status().isOk())
//                .andDo(print());
//
//    }
//}
