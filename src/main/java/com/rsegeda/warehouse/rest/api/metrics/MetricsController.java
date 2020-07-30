/*
 * Created by rsegeda.dev@gmail.com on 07/29/2020.
 */

package com.rsegeda.warehouse.rest.api.metrics;


import com.rsegeda.warehouse.rest.api.GenericApiResponseDto;
import com.rsegeda.warehouse.rest.api.exception.MetricsExtractorException;
import com.rsegeda.warehouse.service.MetricService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/metrics")
public class MetricsController {

  private final MetricService metricService;

  @PostMapping(value = "/query")
  public GenericApiResponseDto getTotalClicks(
    @RequestBody
    @Valid
    final MetricsGenericQueryPostDto postDto) throws MetricsExtractorException {
    return GenericApiResponseDto.builder().result(metricService.genericQuery(postDto)).build();
  }
}
