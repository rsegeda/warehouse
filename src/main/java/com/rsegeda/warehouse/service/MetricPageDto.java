/*
 * Created by rsegeda.dev@gmail.com on 07/29/2020.
 */

package com.rsegeda.warehouse.service;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rsegeda.warehouse.domain.Metric;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetricPageDto {

  private List<Metric> content;

  private Integer pageSize;
  private Integer pageNumber;
  private Integer totalPages;
  private Long totalElements;
}
