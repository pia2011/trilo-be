package com.cosain.trilo.trip.query.infra.repository.trip.jpa;

import com.cosain.trilo.trip.query.infra.dto.TripDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface TripQueryJpaRepositoryCustom {
    Optional<TripDetail> findTripDetailById(Long tripId);

    Slice<TripDetail> findTripDetailListByTripperId(Long tripperId, Pageable pageable);
}