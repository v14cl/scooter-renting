package com.vlad.scooterrental.infrastructure.audit;

import com.vlad.scooterrental.application.audit.AuditMessage;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Component;

@Component
public class InMemoryAuditStore {
  private final Set<UUID> processedEventIds = ConcurrentHashMap.newKeySet();
  private final List<AuditMessage> records = new CopyOnWriteArrayList<>();

  public boolean appendIfNew(AuditMessage message) {
    if (!processedEventIds.add(message.eventId())) {
      return false;
    }
    records.add(message);
    return true;
  }

  public List<AuditMessage> records() {
    return List.copyOf(records);
  }
}
