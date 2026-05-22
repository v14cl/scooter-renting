package com.vlad.scooterrental.core.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "scooters")
public class ScooterEntity {

  @Id private UUID id;

  @Column(nullable = false, unique = true)
  private String code;

  @Column(nullable = false)
  private String model;

  @Column(nullable = false)
  private String status;

  @Column(nullable = false)
  private int batteryLevel;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal pricePerMinute;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public int getBatteryLevel() {
    return batteryLevel;
  }

  public void setBatteryLevel(int batteryLevel) {
    this.batteryLevel = batteryLevel;
  }

  public BigDecimal getPricePerMinute() {
    return pricePerMinute;
  }

  public void setPricePerMinute(BigDecimal pricePerMinute) {
    this.pricePerMinute = pricePerMinute;
  }
}
