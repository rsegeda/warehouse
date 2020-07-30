/*
 * Created by rsegeda.dev@gmail.com on 07/29/2020.
 */

package com.rsegeda.warehouse.infrastructure;


import com.rsegeda.warehouse.domain.Metric;
import com.rsegeda.warehouse.domain.MetricDimension;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface GenericMetricRepository {

  Page<Metric> genericQuery(List<MetricDimension> aggregators, Pageable pageable);

}
