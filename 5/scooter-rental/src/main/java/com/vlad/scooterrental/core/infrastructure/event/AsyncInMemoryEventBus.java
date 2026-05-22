package com.vlad.scooterrental.core.infrastructure.event;

import com.vlad.scooterrental.shared.event.EventHandler;
import com.vlad.scooterrental.shared.event.EventPublisher;
import com.vlad.scooterrental.shared.event.IntegrationEvent;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncInMemoryEventBus implements EventPublisher {
  private static final Logger LOGGER = LoggerFactory.getLogger(AsyncInMemoryEventBus.class);

  private final List<EventHandler<? extends IntegrationEvent>> handlers;
  private final Executor executor;

  public AsyncInMemoryEventBus(
      List<EventHandler<? extends IntegrationEvent>> handlers, Executor executor) {
    this.handlers = List.copyOf(handlers);
    this.executor = executor;
  }

  @Override
  public void publish(IntegrationEvent event) {
    for (EventHandler<? extends IntegrationEvent> handler : handlers) {
      if (handler.eventType().isAssignableFrom(event.getClass())) {
        CompletableFuture.runAsync(() -> handle(handler, event), executor)
            .exceptionally(
                exception -> {
                  LOGGER.error("Failed to handle integration event {}", event.eventId(), exception);
                  return null;
                });
      }
    }
  }

  @SuppressWarnings("unchecked")
  private <T extends IntegrationEvent> void handle(
      EventHandler<T> handler, IntegrationEvent event) {
    handler.handle((T) event);
  }
}
