package com.vlad.scooterrental.core.infrastructure.persistence.jpa;

import com.vlad.scooterrental.core.infrastructure.persistence.entity.RentalEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RentalJpaRepository extends JpaRepository<RentalEntity, UUID> {

  List<RentalEntity> findAllByRenterIdOrderByStartTimeAsc(UUID renterId);

  List<RentalEntity> findAllByOrderByStartTimeAsc();

  @Query(
      """
            select case when count(r) > 0 then true else false end
            from RentalEntity r
            where r.scooterId = :scooterId
              and (:ignoredRentalId is null or r.id <> :ignoredRentalId)
              and r.startTime < :endTime
              and r.endTime > :startTime
            """)
  boolean hasOverlap(
      @Param("scooterId") UUID scooterId,
      @Param("startTime") java.time.LocalDateTime startTime,
      @Param("endTime") java.time.LocalDateTime endTime,
      @Param("ignoredRentalId") UUID ignoredRentalId);
}
