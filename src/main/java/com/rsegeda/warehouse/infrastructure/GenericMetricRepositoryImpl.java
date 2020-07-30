/*
 * Created by rsegeda.dev@gmail.com on 07/29/2020.
 */

package com.rsegeda.warehouse.infrastructure;


import com.rsegeda.warehouse.domain.FilterCondition;
import com.rsegeda.warehouse.domain.Metric;
import com.rsegeda.warehouse.domain.MetricDimension;
import com.rsegeda.warehouse.domain.MetricDimensionFilter;
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
public class GenericMetricRepositoryImpl implements GenericMetricRepository {

  private final EntityManager entityManager;

  @Transactional
  @Override
  public Page<Metric> genericQuery(
    List<MetricDimension> aggregators,
    List<MetricDimensionFilter> filters,
    List<MetricDimension> groupAggregators,
    Pageable pageable) {
    String query = buildMultiSelectQuery(aggregators, filters, groupAggregators);

    @SuppressWarnings("unchecked")
    List<Metric> result = (List<Metric>) entityManager
                                           .createQuery(query)
                                           .setMaxResults(pageable.getPageSize())
                                           .setFirstResult((int) pageable.getOffset())
                                           .getResultList();

    Query queryTotal = entityManager.createQuery(String.format("SELECT COUNT(*) FROM (%s)", query));
    long total = (long) queryTotal.getSingleResult();
    return new PageImpl<>(result, PageRequest.of(pageable.getPageNumber() + 1, pageable.getPageSize()), total);
  }

  private String buildMultiSelectQuery(
    List<MetricDimension> aggregators, List<MetricDimensionFilter> filters, List<MetricDimension> groupAggregators) {
    String keyword = "SELECT ";
    String multiSelectFieldParts = keyword.concat(
      "m.".concat(String.join(", m.", aggregators.stream().map(MetricDimension::getDimension).toArray(String[]::new))));

    if (filters == null || filters.isEmpty()) {
      return multiSelectFieldParts.concat(" FROM Metric m");
    }

    String withWhere = multiSelectFieldParts.concat(" FROM Metric m WHERE ");

    String conditions =
      String.join(" AND ", filters.stream().map(this::buildConditionsForDimension).toArray(String[]::new));

    String groupBy = " ORDER BY ".concat(
      String.join(",", groupAggregators.stream().map(MetricDimension::getDimension).toArray(String[]::new)));

    return withWhere.concat(conditions).concat(groupBy);
  }

  private String buildConditionsForDimension(MetricDimensionFilter metricDimensionFilter) {
    return String.join(",", metricDimensionFilter
                              .getFilterConditions()
                              .stream()
                              .map(filterCondition -> buildSingleConditionForDimension(filterCondition,
                                                                                       metricDimensionFilter))
                              .toArray(String[]::new));
  }

  private String buildSingleConditionForDimension(
    FilterCondition filterCondition, MetricDimensionFilter metricDimensionFilter) {
    return "m.".concat(metricDimensionFilter
                         .getDimensionName()
                         .concat(" ")
                         .concat(filterCondition.getOperation())
                         .concat(String
                                   .valueOf(filterCondition.getFirstValue())
                                   .concat(buildSecondValueIfExist(filterCondition))));
  }

  private String buildSecondValueIfExist(FilterCondition filterCondition) {
    return filterCondition.getSecondValue() == null ? ""
                                                    : " AND ".concat(String.valueOf(filterCondition.getSecondValue()));
  }
}
