/*
 * Created by rsegeda.dev@gmail.com on 07/29/2020.
 */

package com.rsegeda.warehouse.rest.api.extractor;


import com.rsegeda.warehouse.rest.api.exception.MetricsExtractorException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.stereotype.Component;

@Component
public class MetricExtractorUtils {

  /**
   * Parse String url to URL instance
   * @param urlOfCsvFile - url as a string
   * @return url
   * @throws MetricsExtractorException when parsing fails
   */
  public URL parseUrl(String urlOfCsvFile) throws MetricsExtractorException {
    try {
      return new URL(urlOfCsvFile);
    } catch (MalformedURLException e) {
      throw new MetricsExtractorException("Parsing URL of csv file failed." + urlOfCsvFile, e);
    }
  }

  /**
   * Opens an input stream and returns it.
   * @param url - url to be streamed
   * @return InputStream instance
   * @throws IOException when fails
   */
  InputStream urlToInputStream(URL url) throws IOException {
    return url.openStream();
  }
}
