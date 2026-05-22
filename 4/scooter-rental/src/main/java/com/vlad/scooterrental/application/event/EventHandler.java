package com.vlad.scooterrental.application.event;

public interface EventHandler<T extends IntegrationEvent> {
  void handle(T event);

  Class<T> eventType();
}
