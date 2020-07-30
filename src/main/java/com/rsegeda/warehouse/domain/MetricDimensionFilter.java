/*
 * Created by rsegeda.dev@gmail.com on 07/29/2020.
 */

package com.rsegeda.warehouse.domain;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MetricDimensionFilter {

  private MetricDimension dimension;
  private List<FilterCondition> filterConditions;

  public String getDimensionName() {
    return dimension.getDimension();
  }
}
