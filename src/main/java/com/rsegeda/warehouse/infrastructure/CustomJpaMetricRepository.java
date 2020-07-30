/*
 * Created by rsegeda.dev@gmail.com on 07/29/2020.
 */

package com.rsegeda.warehouse.infrastructure;


import com.rsegeda.warehouse.domain.Metric;
import com.rsegeda.warehouse.domain.MetricDimension;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomJpaMetricRepository implements GenericMetricRepository {

  private final EntityManager entityManager;

  @Transactional
  @Override
  public Page<Metric> genericQuery(List<MetricDimension> aggregators, Pageable pageable) {
    String keyword = "SELECT ";
    String multiSelectFieldParts = keyword.concat(
      "m.".concat(String.join(", m.", aggregators.stream().map(MetricDimension::getDimension).toArray(String[]::new))));

    String multiselectQuery = multiSelectFieldParts.concat(" FROM Metric m");

    @SuppressWarnings("unchecked")
    List<Metric> result = (List<Metric>) entityManager
                                           .createQuery(multiselectQuery)
                                           .setMaxResults(pageable.getPageSize())
                                           .setFirstResult((int) pageable.getOffset())
                                           .getResultList();

    Query queryTotal = entityManager.createQuery("SELECT COUNT(m.id) FROM Metric m");
    long total = (long) queryTotal.getSingleResult();
    return new PageImpl<>(result, PageRequest.of(pageable.getPageNumber() + 1, pageable.getPageSize()), total);
  }
}
