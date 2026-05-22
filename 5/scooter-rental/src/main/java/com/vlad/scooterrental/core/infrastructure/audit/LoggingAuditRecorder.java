package com.vlad.scooterrental.core.infrastructure.audit;

import com.vlad.scooterrental.core.application.audit.AuditMessage;
import com.vlad.scooterrental.core.application.audit.AuditRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingAuditRecorder implements AuditRecorder {
  private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAuditRecorder.class);

  private final InMemoryAuditStore auditStore;

  public LoggingAuditRecorder(InMemoryAuditStore auditStore) {
    this.auditStore = auditStore;
  }

  @Override
  public void record(AuditMessage message) {
    if (!auditStore.appendIfNew(message)) {
      LOGGER.debug("Duplicate audit event ignored: {}", message.eventId());
      return;
    }
    LOGGER.info(
        "Audit action={} actorId={} entityType={} entityId={} details={}",
        message.action(),
        message.actorId(),
        message.entityType(),
        message.entityId(),
        message.details());
  }
}
