package com.vlad.scooterrental.presentation.scooter;

import com.vlad.scooterrental.application.scooter.command.CreateScooterCommand;
import com.vlad.scooterrental.application.scooter.command.CreateScooterCommandHandler;
import com.vlad.scooterrental.application.scooter.command.DeleteScooterCommand;
import com.vlad.scooterrental.application.scooter.command.DeleteScooterCommandHandler;
import com.vlad.scooterrental.application.scooter.command.UpdateScooterCommand;
import com.vlad.scooterrental.application.scooter.command.UpdateScooterCommandHandler;
import com.vlad.scooterrental.application.scooter.query.GetScooterByIdQueryHandler;
import com.vlad.scooterrental.application.scooter.query.ListScootersQueryHandler;
import com.vlad.scooterrental.application.scooter.query.ScooterView;
import com.vlad.scooterrental.presentation.dto.ScooterRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scooters")
public class ScooterController {

  private final CreateScooterCommandHandler createScooterCommandHandler;
  private final UpdateScooterCommandHandler updateScooterCommandHandler;
  private final DeleteScooterCommandHandler deleteScooterCommandHandler;
  private final ListScootersQueryHandler listScootersQueryHandler;
  private final GetScooterByIdQueryHandler getScooterByIdQueryHandler;

  public ScooterController(
      CreateScooterCommandHandler createScooterCommandHandler,
      UpdateScooterCommandHandler updateScooterCommandHandler,
      DeleteScooterCommandHandler deleteScooterCommandHandler,
      ListScootersQueryHandler listScootersQueryHandler,
      GetScooterByIdQueryHandler getScooterByIdQueryHandler) {
    this.createScooterCommandHandler = createScooterCommandHandler;
    this.updateScooterCommandHandler = updateScooterCommandHandler;
    this.deleteScooterCommandHandler = deleteScooterCommandHandler;
    this.listScootersQueryHandler = listScootersQueryHandler;
    this.getScooterByIdQueryHandler = getScooterByIdQueryHandler;
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ScooterView> create(@Valid @RequestBody ScooterRequest request) {
    ScooterView createdScooter =
        createScooterCommandHandler.handle(
            CreateScooterCommand.of(
                request.code(),
                request.model(),
                request.status(),
                request.batteryLevel(),
                request.pricePerMinute()));
    return ResponseEntity.status(HttpStatus.CREATED).body(createdScooter);
  }

  @GetMapping
  public List<ScooterView> findAll() {
    return listScootersQueryHandler.handle();
  }

  @GetMapping("/{scooterId}")
  public ScooterView findById(@PathVariable UUID scooterId) {
    return getScooterByIdQueryHandler.handle(scooterId);
  }

  @PutMapping("/{scooterId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ScooterView update(
      @PathVariable UUID scooterId, @Valid @RequestBody ScooterRequest request) {
    return updateScooterCommandHandler.handle(
        UpdateScooterCommand.of(
            scooterId,
            request.code(),
            request.model(),
            request.status(),
            request.batteryLevel(),
            request.pricePerMinute()));
  }

  @DeleteMapping("/{scooterId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> delete(@PathVariable UUID scooterId) {
    deleteScooterCommandHandler.handle(new DeleteScooterCommand(scooterId));
    return ResponseEntity.noContent().build();
  }
}
