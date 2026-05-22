package com.vlad.scooterrental.application.audit;

public class SafeAuditRecorder implements AuditRecorder {
  private static final System.Logger LOGGER = System.getLogger(SafeAuditRecorder.class.getName());

  private final AuditRecorder delegate;

  public SafeAuditRecorder(AuditRecorder delegate) {
    this.delegate = delegate;
  }

  @Override
  public void record(AuditMessage message) {
    try {
      delegate.record(message);
    } catch (RuntimeException exception) {
      LOGGER.log(
          System.Logger.Level.WARNING, "Audit recording failed: " + exception.getMessage());
    }
  }
}
