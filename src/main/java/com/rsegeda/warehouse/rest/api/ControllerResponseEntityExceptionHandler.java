/*
    Created by Roman Segeda on 04 February 2020
*/

package com.rsegeda.warehouse.rest.api;


import com.rsegeda.warehouse.rest.api.exception.MetricsExtractorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Handles known issues.
   * @param ex - an exception from the resource/controller.
   * @param request - a request that processing failed for.
   * @return GenericApiResponseDto instance with the details
   */
  @ExceptionHandler(value = {MetricsExtractorException.class})
  protected ResponseEntity<Object> handleServiceException(RuntimeException ex, WebRequest request) {
    String errorMsg = "There was a problem with the request's data processing. " + ex.getMessage();
    GenericApiResponseDto responseDto = GenericApiResponseDto.builder().result(false).build();
    responseDto.addMessage(errorMsg);
    log.error(errorMsg, ex);
    return handleExceptionInternal(ex, responseDto, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  /**
   * Handles unknown issues.
   * @param ex - an exception from the resource/controller.
   * @param request - a request that processing failed for.
   * @return GenericApiResponseDto instance with the details
   */
  @ExceptionHandler(value = {RuntimeException.class})
  protected ResponseEntity<Object> handleGeneralIssue(
    RuntimeException ex, WebRequest request) {
    String errorMsg = "This should be application specific. " + ex.getMessage();
    GenericApiResponseDto responseDto = GenericApiResponseDto.builder().result(false).build();
    responseDto.addMessage(errorMsg);
    log.error(errorMsg, ex);
    return handleExceptionInternal(ex, responseDto, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }
}
