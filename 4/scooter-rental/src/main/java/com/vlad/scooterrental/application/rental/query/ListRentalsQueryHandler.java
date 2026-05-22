package com.vlad.scooterrental.application.rental.query;

import java.util.List;

public class ListRentalsQueryHandler {
  private final RentalReadRepository rentalReadRepository;

  public ListRentalsQueryHandler(RentalReadRepository rentalReadRepository) {
    this.rentalReadRepository = rentalReadRepository;
  }

  public List<RentalView> handle(ListRentalsQuery query) {
    return rentalReadRepository.findAllVisibleFor(query.actor());
  }
}
