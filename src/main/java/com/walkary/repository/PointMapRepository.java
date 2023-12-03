package com.walkary.repository;

import com.walkary.models.entity.PointMap;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface PointMapRepository extends CrudRepository<PointMap, Long> {

    List<PointMap> findAllByUserIdAndDate(String userId, LocalDate date, Sort sort);

}
