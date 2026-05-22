package com.vlad.scooterrental.core.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "rentals")
public class RentalEntity {

  @Id private UUID id;

  @Column(nullable = false)
  private UUID renterId;

  @Column(nullable = false)
  private UUID scooterId;

  @Column(nullable = false)
  private LocalDateTime startTime;

  @Column(nullable = false)
  private LocalDateTime endTime;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getRenterId() {
    return renterId;
  }

  public void setRenterId(UUID renterId) {
    this.renterId = renterId;
  }

  public UUID getScooterId() {
    return scooterId;
  }

  public void setScooterId(UUID scooterId) {
    this.scooterId = scooterId;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }
}
