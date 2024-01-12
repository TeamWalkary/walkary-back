package com.walkary.repository;

import com.walkary.models.dto.DiaryWithAttachmentDTO;
import com.walkary.models.entity.Diary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@EnableJpaRepositories
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query("select d from Diary d where date=:date and user_id=:userId")
    Optional<Diary> findByDateAndUserId(@Param("userId") String userId, LocalDate date);

    //일기모아보기(검색 날짜 없음)
    @Query("SELECT NEW com.walkary.models.dto.DiaryWithAttachmentDTO(d.id, d.date, d.title, d.content, dm.attachment) " +
            "FROM Diary d LEFT JOIN d.diaryMedia dm " +
            "WHERE d.user.id = :userId ")
    Page<DiaryWithAttachmentDTO> findDiariesWithMediaByUserId(Pageable pageable, @Param("userId") String userId);

    //일기모아보기(검색 날짜 X)용 일기 Count 쿼리
    long countDiariesByUserId(String userId);

    //일기모아보기(검색 날짜 있음)
    @Query("SELECT NEW com.walkary.models.dto.DiaryWithAttachmentDTO(d.id, d.date, d.title, d.content, dm.attachment) " +
            "FROM Diary d LEFT JOIN d.diaryMedia dm " +
            "WHERE d.user.id = :userId " +
            "AND (:startDate IS NULL OR d.date >= :startDate) " +
            "AND (:endDate IS NULL OR d.date <= :endDate)")
    Page<DiaryWithAttachmentDTO> findDiariesWithMediaByUserIdAndDate(Pageable pageable, @Param("userId") String userId, LocalDate startDate, LocalDate endDate);

    //그 날짜에 일기 데이터 있는지 확인
    boolean existsByDateAndUserId(LocalDate date, String userId);

    //일기모아보기(검색 날짜 O)용 일기 Count 쿼리
    @Query("SELECT COUNT(d) " +
            "FROM Diary d " +
            "WHERE d.user.id = :userId " +
            "AND (:startDate IS NULL OR d.date >= :startDate) " +
            "AND (:endDate IS NULL OR d.date <= :endDate)")
    long countDiariesByUserIdAndDate(@Param("userId") String userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    //달력 핀 유무 확인용
    @Query("SELECT DAY(d.date) FROM Diary d " +
            "WHERE YEAR(d.date) = :year " +
            "AND MONTH(d.date) = :month " +
            "AND user_id=:userId")
    List<Integer> findByMonth(@Param("year") int year, @Param("month") int month, String userId);
}
