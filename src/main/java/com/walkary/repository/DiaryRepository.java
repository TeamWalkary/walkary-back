package com.walkary.repository;

import com.walkary.models.dto.DiaryWithAttachmentDTO;
import com.walkary.models.entity.Diary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;


@EnableJpaRepositories
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query("select d from Diary d where date=:date and user_id=:userId")
    Diary findByDate(@Param("userId") String userId, LocalDate date);

    //일기모아보기(검색 날짜 없음)
    @Query("SELECT NEW com.walkary.models.dto.DiaryWithAttachmentDTO(d.id, d.date, d.title, d.content, dm.attachment) " +
            "FROM Diary d LEFT JOIN d.diaryMedia dm " +
            "WHERE d.user.id = :userId ")
    Page<DiaryWithAttachmentDTO> findDiariesWithMediaByUserId(Pageable pageable, @Param("userId") String userId);

    @Query("SELECT NEW com.walkary.models.dto.DiaryWithAttachmentDTO(d.id, d.date, d.title, d.content, dm.attachment) " +
            "FROM Diary d LEFT JOIN d.diaryMedia dm " +
            "WHERE d.user.id = :userId " +
            "AND (:startDate IS NULL OR d.date >= :startDate) " +
            "AND (:endDate IS NULL OR d.date <= :endDate)")
    Page<DiaryWithAttachmentDTO> findDiariesWithMediaByUserIdAndDate(Pageable pageable, @Param("userId") String userId, LocalDate startDate, LocalDate endDate);
}
