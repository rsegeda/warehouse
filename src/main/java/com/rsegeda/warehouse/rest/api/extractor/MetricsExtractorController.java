/*
 * Created by rsegeda.dev@gmail.com on 07/29/2020.
 */

package com.rsegeda.warehouse.rest.api.extractor;


import com.rsegeda.warehouse.rest.api.GenericApiResponseDto;
import com.rsegeda.warehouse.rest.api.exception.MetricsExtractorException;
import com.rsegeda.warehouse.service.MetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/extractor")
public class MetricsExtractorController {

  private final MetricExtractorService metricExtractorService;
  private final MetricService metricService;

  /**
   * Transforms csv metrics to the db records
   * @param url - The URL of a CSV file
   * @return a generic response object with the result (boolean flag) - true when successful
   * @throws MetricsExtractorException when something fails
   */
  @GetMapping("/extract")
  public GenericApiResponseDto extractAndStore(
    @RequestParam("url")
    final String url) throws MetricsExtractorException {

    metricExtractorService.populateDb(url);
    return GenericApiResponseDto.builder().build();
  }

  /**
   * Removes all metrics from db.
   * @return a generic response with the result (boolean flag) - true when successful
   * @throws MetricsExtractorException when something fails
   */
  @DeleteMapping("/tabularasa")
  public GenericApiResponseDto clearAll(
    @RequestParam
      String token) throws MetricsExtractorException {
    metricService.removeAll(token);
    return GenericApiResponseDto.builder().build();
  }
}
