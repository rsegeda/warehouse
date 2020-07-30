/*
 * Created by rsegeda.dev@gmail.com on 07/29/2020.
 */

package com.rsegeda.warehouse.rest.api.metrics;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rsegeda.warehouse.domain.MetricDimension;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetricsGenericQueryPostDto {

  private List<MetricDimension> aggregators;
  private Integer page;
  private Integer pageSize;
}
