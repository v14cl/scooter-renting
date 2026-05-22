package com.vlad.scooterrental.application.rental.query;

import com.vlad.scooterrental.domain.exception.NotFoundException;

public class GetRentalByIdQueryHandler {
  private final RentalReadRepository rentalReadRepository;

  public GetRentalByIdQueryHandler(RentalReadRepository rentalReadRepository) {
    this.rentalReadRepository = rentalReadRepository;
  }

  public RentalView handle(GetRentalByIdQuery query) {
    return rentalReadRepository
        .findVisibleById(query.rentalId(), query.actor())
        .orElseThrow(() -> new NotFoundException("Rental not found"));
  }
}
