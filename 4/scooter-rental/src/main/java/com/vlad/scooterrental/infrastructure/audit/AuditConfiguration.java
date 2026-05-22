package com.vlad.scooterrental.infrastructure.audit;

import com.vlad.scooterrental.application.audit.AuditRecorder;
import com.vlad.scooterrental.application.audit.SafeAuditRecorder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AuditConfiguration {
  @Bean
  @Primary
  public AuditRecorder safeAuditRecorder(LoggingAuditRecorder delegate) {
    return new SafeAuditRecorder(delegate);
  }
}
