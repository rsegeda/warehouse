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
import org.apache.commons.lang3.StringUtils;
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

    Query queryTotal = entityManager.createQuery(query.replace("SELECT", "SELECT COUNT(m),").concat(" GROUP BY m.id"));
    long total = queryTotal.getResultList().size();
    return new PageImpl<>(result, PageRequest.of(pageable.getPageNumber() + 1, pageable.getPageSize()), total);
  }

  private String buildMultiSelectQuery(
    List<MetricDimension> aggregators, List<MetricDimensionFilter> filters, List<MetricDimension> groupAggregators) {
    String keyword = "SELECT ";
    String multiSelectFieldParts = aggregators == null ? "*" : keyword.concat(
      "m.".concat(String.join(", m.", aggregators.stream().map(MetricDimension::getDimension).toArray(String[]::new))));

    String withWhere = filters == null ? "" : multiSelectFieldParts.concat(" FROM Metric m WHERE ");

    String conditions = filters == null ? "" : String.join(" AND ", filters
                                                                      .stream()
                                                                      .map(this::buildConditionsForDimension)
                                                                      .toArray(String[]::new));

    String groupBy = groupAggregators == null ? "" : " ORDER BY ".concat(
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
                         .concat("'"
                                   .concat(escapeSqlChars(String.valueOf(filterCondition.getFirstValue())))
                                   .concat("'")
                                   .concat(buildSecondValueIfExist(filterCondition))));
  }

  private String escapeSqlChars(String val) {
    return StringUtils.replace(val, "'", "''");
  }

  private String buildSecondValueIfExist(FilterCondition filterCondition) {
    return filterCondition.getSecondValue() == null ? "" : " AND ".concat(
      escapeSqlChars(String.valueOf(filterCondition.getSecondValue())));
  }
}
