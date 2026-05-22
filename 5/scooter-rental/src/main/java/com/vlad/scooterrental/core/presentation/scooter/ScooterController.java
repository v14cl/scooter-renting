package com.vlad.scooterrental.core.presentation.scooter;

import com.vlad.scooterrental.core.application.scooter.command.CreateScooterCommand;
import com.vlad.scooterrental.core.application.scooter.command.CreateScooterCommandHandler;
import com.vlad.scooterrental.core.application.scooter.command.DeleteScooterCommand;
import com.vlad.scooterrental.core.application.scooter.command.DeleteScooterCommandHandler;
import com.vlad.scooterrental.core.application.scooter.command.UpdateScooterCommand;
import com.vlad.scooterrental.core.application.scooter.command.UpdateScooterCommandHandler;
import com.vlad.scooterrental.core.application.scooter.query.GetScooterByIdQuery;
import com.vlad.scooterrental.core.application.scooter.query.GetScooterByIdQueryHandler;
import com.vlad.scooterrental.core.application.scooter.query.ListScootersQuery;
import com.vlad.scooterrental.core.application.scooter.query.ListScootersQueryHandler;
import com.vlad.scooterrental.core.application.scooter.query.ScooterView;
import com.vlad.scooterrental.core.presentation.dto.CreatedIdResponse;
import com.vlad.scooterrental.core.presentation.dto.ScooterRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
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
  public ResponseEntity<CreatedIdResponse> create(@Valid @RequestBody ScooterRequest request) {
    UUID scooterId =
        createScooterCommandHandler.handle(
            new CreateScooterCommand(
                request.code(),
                request.model(),
                request.status(),
                request.batteryLevel(),
                request.pricePerMinute()));
    return ResponseEntity.status(HttpStatus.CREATED).body(new CreatedIdResponse(scooterId));
  }

  @GetMapping
  public List<ScooterView> findAll(@RequestParam(required = false) String status) {
    return listScootersQueryHandler.handle(new ListScootersQuery(status));
  }

  @GetMapping("/{scooterId}")
  public ScooterView findById(@PathVariable UUID scooterId) {
    return getScooterByIdQueryHandler.handle(new GetScooterByIdQuery(scooterId));
  }

  @PutMapping("/{scooterId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> update(
      @PathVariable UUID scooterId, @Valid @RequestBody ScooterRequest request) {
    updateScooterCommandHandler.handle(
        new UpdateScooterCommand(
            scooterId,
            request.code(),
            request.model(),
            request.status(),
            request.batteryLevel(),
            request.pricePerMinute()));
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{scooterId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> delete(@PathVariable UUID scooterId) {
    deleteScooterCommandHandler.handle(new DeleteScooterCommand(scooterId));
    return ResponseEntity.noContent().build();
  }
}
