package com.vlad.scooterrental.application.audit;

public interface AuditRecorder {
  void record(AuditMessage message);
}
