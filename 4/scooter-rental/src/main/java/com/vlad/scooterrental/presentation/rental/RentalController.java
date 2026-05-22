package com.vlad.scooterrental.presentation.rental;

import com.vlad.scooterrental.application.rental.command.CreateRentalCommand;
import com.vlad.scooterrental.application.rental.command.CreateRentalCommandHandler;
import com.vlad.scooterrental.application.rental.command.DeleteRentalCommand;
import com.vlad.scooterrental.application.rental.command.DeleteRentalCommandHandler;
import com.vlad.scooterrental.application.rental.command.UpdateRentalCommand;
import com.vlad.scooterrental.application.rental.command.UpdateRentalCommandHandler;
import com.vlad.scooterrental.application.rental.query.GetRentalByIdQuery;
import com.vlad.scooterrental.application.rental.query.GetRentalByIdQueryHandler;
import com.vlad.scooterrental.application.rental.query.ListRentalsQuery;
import com.vlad.scooterrental.application.rental.query.ListRentalsQueryHandler;
import com.vlad.scooterrental.application.rental.query.RentalView;
import com.vlad.scooterrental.presentation.dto.CreatedIdResponse;
import com.vlad.scooterrental.presentation.dto.RentalRequest;
import com.vlad.scooterrental.presentation.dto.SecurityUtils;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

  private final CreateRentalCommandHandler createRentalCommandHandler;
  private final UpdateRentalCommandHandler updateRentalCommandHandler;
  private final DeleteRentalCommandHandler deleteRentalCommandHandler;
  private final ListRentalsQueryHandler listRentalsQueryHandler;
  private final GetRentalByIdQueryHandler getRentalByIdQueryHandler;

  public RentalController(
      CreateRentalCommandHandler createRentalCommandHandler,
      UpdateRentalCommandHandler updateRentalCommandHandler,
      DeleteRentalCommandHandler deleteRentalCommandHandler,
      ListRentalsQueryHandler listRentalsQueryHandler,
      GetRentalByIdQueryHandler getRentalByIdQueryHandler) {
    this.createRentalCommandHandler = createRentalCommandHandler;
    this.updateRentalCommandHandler = updateRentalCommandHandler;
    this.deleteRentalCommandHandler = deleteRentalCommandHandler;
    this.listRentalsQueryHandler = listRentalsQueryHandler;
    this.getRentalByIdQueryHandler = getRentalByIdQueryHandler;
  }

  @PostMapping
  public ResponseEntity<CreatedIdResponse> create(
      @Valid @RequestBody RentalRequest request, Authentication authentication) {
    UUID rentalId =
        createRentalCommandHandler.handle(
            new CreateRentalCommand(
                SecurityUtils.actor(authentication),
                request.scooterId(),
                request.startTime(),
                request.endTime()));
    return ResponseEntity.status(HttpStatus.CREATED).body(new CreatedIdResponse(rentalId));
  }

  @GetMapping
  public List<RentalView> findAll(Authentication authentication) {
    return listRentalsQueryHandler.handle(new ListRentalsQuery(SecurityUtils.actor(authentication)));
  }

  @GetMapping("/{rentalId}")
  public RentalView findById(@PathVariable UUID rentalId, Authentication authentication) {
    return getRentalByIdQueryHandler.handle(
        new GetRentalByIdQuery(rentalId, SecurityUtils.actor(authentication)));
  }

  @PutMapping("/{rentalId}")
  public ResponseEntity<Void> update(
      @PathVariable UUID rentalId,
      @Valid @RequestBody RentalRequest request,
      Authentication authentication) {
    updateRentalCommandHandler.handle(
        new UpdateRentalCommand(
            SecurityUtils.actor(authentication),
            rentalId,
            request.scooterId(),
            request.startTime(),
            request.endTime()));
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{rentalId}")
  public ResponseEntity<Void> delete(@PathVariable UUID rentalId, Authentication authentication) {
    deleteRentalCommandHandler.handle(
        new DeleteRentalCommand(SecurityUtils.actor(authentication), rentalId));
    return ResponseEntity.noContent().build();
  }
}
