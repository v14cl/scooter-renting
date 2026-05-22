package com.vlad.scooterrental.application.rental.query;

import com.vlad.scooterrental.application.common.Actor;
import java.util.List;

public class ListRentalsQueryHandler {
  private final RentalReadRepository rentalReadRepository;

  public ListRentalsQueryHandler(RentalReadRepository rentalReadRepository) {
    this.rentalReadRepository = rentalReadRepository;
  }

  public List<RentalView> handle(Actor actor) {
    return rentalReadRepository.findAllVisibleFor(actor);
  }
}
