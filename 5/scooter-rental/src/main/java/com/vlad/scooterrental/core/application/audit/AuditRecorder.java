package com.vlad.scooterrental.core.application.audit;

public interface AuditRecorder {
  void record(AuditMessage message);
}
