/*
 * Created by rsegeda.dev@gmail.com on 07/29/2020.
 */

package com.rsegeda.warehouse.domain;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MetricDimension {
  DATA_SOURCE("dataSource"), CLICKS("clicks"), DAILY("daily"), IMPRESSIONS("impressions"), CAMPAIGN("campaign");

  @JsonValue
  private final String dimension;
}
