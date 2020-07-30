/*
 * Created by rsegeda.dev@gmail.com on 07/30/2020.
 */

package com.rsegeda.warehouse.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FilterCondition {

  private FilterType filterType;
  private Object firstValue;
  private Object secondValue;

  public String getOperation(){
    return filterType.getFilter();
  }
}
