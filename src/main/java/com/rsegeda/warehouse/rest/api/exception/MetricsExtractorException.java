/*
 * Created by rsegeda.dev@gmail.com on 07/29/2020.
 */

package com.rsegeda.warehouse.rest.api.exception;


public class MetricsExtractorException extends RuntimeException {

  public MetricsExtractorException(String message, Exception e) {
    super(message.concat(".. Details: ").concat(e.getMessage()));
  }
}
