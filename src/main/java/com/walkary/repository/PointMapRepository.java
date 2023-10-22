package com.walkary.repository;

import com.walkary.models.entity.PointMap;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PointMapRepository extends CrudRepository<PointMap, Long> {

}
