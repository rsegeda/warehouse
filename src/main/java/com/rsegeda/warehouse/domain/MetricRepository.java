/*
 * Created by rsegeda.dev@gmail.com on 07/29/2020.
 */

package com.rsegeda.warehouse.domain;


import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {

  @Query("SELECT m FROM Metric m WHERE m.dataSource = :dataSource AND m.daily BETWEEN :from AND :to")
  Page<Metric> getTotalClicksByDatasourceAndDataRange(
    @Param("dataSource")
      String dataSource,
    @Param("from")
      Date from,
    @Param("to")
      Date to, Pageable pageable);
}

