package com.vlad.scooterrental.application.scooter.command;

import com.vlad.scooterrental.application.audit.AuditAction;
import com.vlad.scooterrental.application.audit.AuditMessage;
import com.vlad.scooterrental.application.audit.AuditRecorder;
import com.vlad.scooterrental.domain.exception.ValidationException;
import com.vlad.scooterrental.domain.factory.ScooterFactory;
import com.vlad.scooterrental.domain.model.ScooterStatus;
import com.vlad.scooterrental.domain.repository.ScooterRepository;
import java.time.Instant;
import java.util.UUID;

public class CreateScooterCommandHandler {
  private final ScooterFactory scooterFactory;
  private final ScooterRepository scooterRepository;
  private final AuditRecorder auditRecorder;

  public CreateScooterCommandHandler(
      ScooterFactory scooterFactory, ScooterRepository scooterRepository, AuditRecorder auditRecorder) {
    this.scooterFactory = scooterFactory;
    this.scooterRepository = scooterRepository;
    this.auditRecorder = auditRecorder;
  }

  public UUID handle(CreateScooterCommand command) {
    var scooter =
        scooterFactory.create(
            command.code(),
            command.model(),
            parseStatus(command.status()),
            command.batteryLevel(),
            command.pricePerMinute());
    var savedScooter = scooterRepository.save(scooter);
    auditRecorder.record(
        new AuditMessage(
            UUID.randomUUID(),
            AuditAction.SCOOTER_CREATED,
            "system",
            "Scooter",
            savedScooter.id().toString(),
            Instant.now(),
            "Created scooter " + savedScooter.code()));
    return savedScooter.id();
  }

  private ScooterStatus parseStatus(String statusName) {
    if (statusName == null || statusName.isBlank()) {
      throw new ValidationException("Scooter status must be provided");
    }
    try {
      return ScooterStatus.valueOf(statusName.trim().toUpperCase());
    } catch (IllegalArgumentException exception) {
      throw new ValidationException("Unsupported scooter status: " + statusName);
    }
  }
}
