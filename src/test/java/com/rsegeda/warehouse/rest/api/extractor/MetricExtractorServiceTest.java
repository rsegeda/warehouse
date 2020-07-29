/*
 * Created by rsegeda.dev@gmail.com on 07/29/2020.
 */

package com.rsegeda.warehouse.rest.api.extractor;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rsegeda.warehouse.domain.Metric;
import com.rsegeda.warehouse.domain.MetricService;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MetricExtractorServiceTest {

  private static final String CORRECT_URL =
    "http%3A%2F%2Fadverity-challenge.s3-website-eu-west-1.amazonaws" + ".com%2FPIxSyyrIKFORrCXfMYqZBI.csv";

  private MetricExtractorService metricExtractorService;
  @Mock private MetricService metricServiceMock;
  @Mock private MetricExtractorUtils metricExtractorUtilsMock;

  private URL correctUrl;
  InputStream inputStreamMock;

  @Captor ArgumentCaptor<List<Metric>> metricsCaptor;

  @BeforeEach
  public void setup() throws IOException {
    metricExtractorService = new MetricExtractorService(metricServiceMock, metricExtractorUtilsMock);
    correctUrl = new URL(URLDecoder.decode(CORRECT_URL, Charset.defaultCharset()));
    ClassLoader classLoader = getClass().getClassLoader();
    inputStreamMock = Objects.requireNonNull(classLoader.getResource("data-test.csv")).openStream();
  }

  @Test
  public void initiatesTheCreationOfEntities() throws IOException {
    when(metricExtractorUtilsMock.parseUrl(CORRECT_URL)).thenReturn(correctUrl);
    when(metricExtractorUtilsMock.urlToInputStream(any())).thenReturn(inputStreamMock);
    metricExtractorService.populateDb(CORRECT_URL);
    verify(metricServiceMock).addAllAsync(metricsCaptor.capture());
    Assertions.assertEquals(9, metricsCaptor.getValue().size());
  }

  private List<Metric> generateMetrics(int number) {
    Random random = new Random();
    return IntStream
             .range(0, number)
             .mapToObj(i -> Metric
                              .builder()
                              .campaign("campaign" + i)
                              .clicks(new BigDecimal(random.nextInt()))
                              .daily(Date.from(Instant.now().plus(Duration.ofHours(random.nextInt()))))
                              .build())
             .collect(Collectors.toList());
  }
}
