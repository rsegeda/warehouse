/*
 * Created by rsegeda.dev@gmail.com on 07/29/2020.
 */

package com.rsegeda.warehouse.domain;


import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MetricService {

  private final MetricRepository metricRepository;

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
   */
  public void removeAll() {
    metricRepository.deleteAll();
  }
  public void addAllAsync(List<Metric> metrics) {
    CompletableFuture.runAsync(() -> {
      List<Metric> savedMetrics = addAll(metrics);

      if (savedMetrics != null) {
        log.info("{} new savedMetrics stored in the db.", savedMetrics.size());
      }
    });
  }
}
