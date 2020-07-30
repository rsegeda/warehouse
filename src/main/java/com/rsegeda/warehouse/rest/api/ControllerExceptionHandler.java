/*
    Created by Roman Segeda on 04 February 2020
*/

package com.rsegeda.warehouse.rest.api;


import com.rsegeda.warehouse.rest.api.exception.MetricsExtractorException;
import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Handles known issues.
   * @param ex - an exception from the resource/controller.
   * @param request - a request that processing failed for.
   * @return GenericApiResponseDto instance with the details
   */
  @ExceptionHandler(MetricsExtractorException.class)
  protected ResponseEntity<Object> handleServiceException(MetricsExtractorException ex, WebRequest request) {
    String errorMsg = "There was a problem with the request's data processing. " + ex.getMessage();
    GenericApiResponseDto responseDto = GenericApiResponseDto.builder().successful(false).build();
    responseDto.addMessage(errorMsg);
    log.error(errorMsg, ex);
    return handleExceptionInternal(ex, responseDto, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(ValidationException.class)
  protected ResponseEntity<Object> handleBadRequest(
    ValidationException ex, WebRequest request) {
    String errorMsg = "This should be a request validation specific. " + ex.getMessage();
    GenericApiResponseDto responseDto = GenericApiResponseDto.builder().successful(false).build();
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
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<Object> handleGeneralIssue(
    Exception ex, WebRequest request) {
    String errorMsg = "This should be application specific. " + ex.getMessage();
    GenericApiResponseDto responseDto = GenericApiResponseDto.builder().successful(false).build();
    responseDto.addMessage(errorMsg);
    log.error(errorMsg, ex);
    return handleExceptionInternal(ex, responseDto, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

}
