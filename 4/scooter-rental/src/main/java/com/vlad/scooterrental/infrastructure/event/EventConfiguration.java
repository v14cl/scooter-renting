package com.vlad.scooterrental.infrastructure.event;

import com.vlad.scooterrental.application.event.EventHandler;
import com.vlad.scooterrental.application.event.EventPublisher;
import com.vlad.scooterrental.application.event.IntegrationEvent;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfiguration {
  @Bean(destroyMethod = "shutdown")
  public ExecutorService integrationEventExecutor() {
    return Executors.newCachedThreadPool();
  }

  @Bean
  public EventPublisher eventPublisher(
      List<EventHandler<? extends IntegrationEvent>> handlers, ExecutorService executor) {
    return new AsyncInMemoryEventBus(handlers, executor);
  }
}
