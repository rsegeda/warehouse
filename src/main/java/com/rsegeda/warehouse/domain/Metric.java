/*
 * Created by rsegeda.dev@gmail.com on 07/29/2020.
 */

package com.rsegeda.warehouse.domain;


import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table
public class Metric {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private long id;

  @NotBlank
  @CsvBindByName(column = "Datasource")
  @Column
  private String dataSource;

  @NotBlank
  @CsvBindByName(column = "Campaign")
  @Column
  private String campaign;

  @CsvBindByName(column = "Daily")
  @CsvDate(value = "MM/dd/yy")
  @Temporal(TemporalType.DATE)
  @NotNull
  private Date daily;

  @PositiveOrZero
  @CsvBindByName(column = "Clicks")
  @Column
  private BigDecimal clicks;

  @PositiveOrZero
  @CsvBindByName(column = "Impressions")
  @Column
  private BigDecimal impressions;

  private void setDataSource(String dataSource) {
    this.dataSource = dataSource.trim();
  }
  private void setCampaign(String campaign) {
    this.campaign = campaign.trim();
  }
}
