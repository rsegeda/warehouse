/*
 * Created by rsegeda.dev@gmail.com on 07/29/2020.
 */

package com.rsegeda.warehouse.rest.api.extractor;


import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rsegeda.warehouse.service.MetricService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class MetricsExtractorIntegrationTest {

  private static final String CORRECT_URL =
    "http%3A%2F%2Fadverity-challenge.s3-website-eu-west-1.amazonaws" + ".com%2FPIxSyyrIKFORrCXfMYqZBI.csv";

  @Mock private MetricExtractorService metricExtractorServiceMock;
  @Mock private MetricService metricServiceMock;
  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    MetricsExtractorController metricsExtractorController =
      new MetricsExtractorController(metricExtractorServiceMock, metricServiceMock);

    this.mockMvc = MockMvcBuilders.standaloneSetup(metricsExtractorController).build();
  }

  @Test
  public void startsDataExtractionInTheBackground() throws Exception {
    doNothing().when(metricExtractorServiceMock).populateDb(CORRECT_URL);
    mockMvc
      .perform(get("/extractor/extract?url=" + CORRECT_URL))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.successful").value(true));
  }
}
