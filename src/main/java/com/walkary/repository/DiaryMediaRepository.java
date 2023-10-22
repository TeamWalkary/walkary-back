package com.walkary.repository;

import com.walkary.models.entity.DiaryMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DiaryMediaRepository extends JpaRepository<DiaryMedia, Long> {

    @Query("select dm from DiaryMedia dm where diary_id=:diaryId")
    Optional<DiaryMedia> findByDiaryId(Long diaryId);

    @Modifying
    @Query("DELETE FROM DiaryMedia dm WHERE dm.diary.id = :diaryId")
    void deleteByDiaryId(Long diaryId);
}
