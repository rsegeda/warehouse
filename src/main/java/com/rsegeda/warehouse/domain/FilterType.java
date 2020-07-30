/*
 * Created by rsegeda.dev@gmail.com on 07/29/2020.
 */

package com.rsegeda.warehouse.domain;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FilterType {
  LIKE("LIKE"), BETWEEN("BETWEEN"), EQUAL("="), LT("<"), GT(">"), LTE("<="), GTE(">=");

  @JsonValue private final String filter;
}
