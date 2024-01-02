package com.walkary.repository;

import com.walkary.models.entity.PointMap;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface PointMapRepository extends CrudRepository<PointMap, Long> {

    List<PointMap> findAllByUserIdAndDate(String userId, LocalDate date, Sort sort);

    List<PointMap> findAllByUserId(String userId, Sort sort);

    @Query("SELECT DAY(p.date) FROM PointMap p " +
            "WHERE YEAR(p.date) = :year " +
            "AND MONTH(p.date) = :month " +
            "AND user_id=:userId")
    List<Integer> findByMonth(int year, int month, String userId);
}
