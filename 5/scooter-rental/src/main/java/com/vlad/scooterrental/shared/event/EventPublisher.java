package com.vlad.scooterrental.shared.event;

public interface EventPublisher {
  void publish(IntegrationEvent event);
}
