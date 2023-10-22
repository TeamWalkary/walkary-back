package com.walkary.repository;

import com.walkary.models.entity.Diary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query("select d from Diary d where user_id=:userId")
    Page<Diary> findListByMemberId(Pageable pageable, @Param("userId") Long userId);

    @Query("select d from Diary d where date=:date and user_id=:userId")
    Diary findByDate(@Param("userId") String userId, LocalDate date);
}
