/*
 * Created by rsegeda.dev@gmail.com on 07/29/2020.
 */

package com.rsegeda.warehouse.domain;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {

}

