package com.vlad.scooterrental.shared.event;

public interface EventHandler<T extends IntegrationEvent> {
  void handle(T event);

  Class<T> eventType();
}
