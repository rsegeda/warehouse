/*
 * Created by rsegeda.dev@gmail.com on 07/29/2020.
 */

package com.rsegeda.warehouse.service;


import com.rsegeda.warehouse.domain.Metric;
import com.rsegeda.warehouse.domain.MetricRepository;
import com.rsegeda.warehouse.infrastructure.GenericMetricRepository;
import com.rsegeda.warehouse.rest.api.metrics.MetricsGenericQueryPostDto;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MetricService {

  private final MetricRepository metricRepository;
  private final GenericMetricRepository genericMetricRepository;

  @Value("${extractor.safety.token}") private String safetyToken;

  /**
   * Saves metrics in the repository.
   * @param metrics - a list of metrics
   * @return saved metrics
   */
  public List<Metric> addAll(List<Metric> metrics) {
    return metricRepository.saveAll(metrics);
  }

  /**
   * Removes all metrics from the repository.
   * @param token - safety token EXTRACTOR_SAFETY_TOKEN
   */
  public void removeAll(String token) {
    if (safetyToken.equals(token)) {
      metricRepository.deleteAll();
      log.warn("All metrics have been removed");
    } else {
      log.warn("Missing safety token. Cannot delete all metrics");
    }
  }

  /**
   * Saves metrics in the repository (async).
   * @param metrics - a list of metrics
   */
  public void addAllAsync(List<Metric> metrics) {
    CompletableFuture.runAsync(() -> {
      List<Metric> savedMetrics = addAll(metrics);

      if (savedMetrics != null) {
        log.info("{} new savedMetrics stored in the db.", savedMetrics.size());
      }
    });
  }

  public MetricPageDto genericQuery(MetricsGenericQueryPostDto postDto) {
    Pageable userRequestedPage = PageRequest.of(postDto.getPage(), postDto.getPageSize());
    return getMetricPageDto(genericMetricRepository.genericQuery(postDto.getAggregators(), postDto.getFilters(),
                                                                 postDto.getGroupAggregators(),
                                                                 userRequestedPage));
  }

  private MetricPageDto getMetricPageDto(Page<Metric> page) {
    return MetricPageDto
             .builder()
             .content(page.getContent())
             .pageSize(page.getSize())
             .pageNumber(page.getNumber())
             .totalPages(page.getTotalPages())
             .totalElements(page.getTotalElements())
             .build();
  }
}
