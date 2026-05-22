package com.vlad.scooterrental.application.event;

public interface EventPublisher {
  void publish(IntegrationEvent event);
}
