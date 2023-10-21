package com.walkary.controller;

import com.walkary.models.dto.request.DiaryCreate;
import com.walkary.models.dto.request.DiaryEdit;
import com.walkary.models.dto.response.DiaryResponse;
import com.walkary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    //일기 작성
    @PostMapping("/write")
    public void write(@RequestBody DiaryCreate request) {
        log.info("request={}" + request.toString());
        diaryService.write(request);
    }

    //일기 모아보기
    @GetMapping("/{userId}")
    public List<DiaryResponse> findListByMemberId(@PageableDefault(size = 5) Pageable pageable, @PathVariable(name = "userId") Long userId) {
        return diaryService.findListByMemberId(pageable, userId);
    }

    //일기 수정
    @PatchMapping("/{diaryId}")
    public void edit(@PathVariable Long diaryId, @RequestBody DiaryEdit request) {
        diaryService.edit(diaryId, request);
    }

}
