/*
 * Created by rsegeda.dev@gmail.com on 07/29/2020.
 */

package com.rsegeda.warehouse.rest.api.extractor;


import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.rsegeda.warehouse.domain.Metric;
import com.rsegeda.warehouse.service.MetricService;
import com.rsegeda.warehouse.rest.api.exception.MetricsExtractorException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MetricExtractorService {

  private final MetricService metricService;
  private final MetricExtractorUtils metricExtractorUtils;

  /**
   * Retrieves the csv file from the given url and stores it in a db. Runs in background.
   * @param urlOfCsvFile - url to a csv file
   * @throws MetricsExtractorException when extraction fails by any reason
   */
  public void populateDb(String urlOfCsvFile) throws MetricsExtractorException {
    URL url = metricExtractorUtils.parseUrl(urlOfCsvFile);
    List<Metric> metrics = extractMetricsFromUrl(url);
    log.info("{} new metrics extracted from csv file: {}", metrics.size(), url);
    metricService.addAllAsync(metrics);
  }

  /**
   * Extracts metrics file and transforms it to the list of defined Metric model
   * @param url - url to a csv file
   * @return list of metrics
   * @throws MetricsExtractorException when conversion fails
   */
  public List<Metric> extractMetricsFromUrl(URL url) throws MetricsExtractorException {
    try (BufferedReader reader = new BufferedReader(
      new InputStreamReader(metricExtractorUtils.urlToInputStream(url)))) {

      CsvToBean<Metric> csvParser = new CsvToBeanBuilder<Metric>(reader).withType(Metric.class).build();
      return csvParser.parse();
    } catch (Exception e) {
      throw new MetricsExtractorException("Extracting metrics from URL failed. " + url, e);
    }
  }
}
