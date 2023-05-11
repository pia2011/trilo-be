package com.cosain.trilo.trip.query.presentation.trip.dto;

import com.cosain.trilo.trip.query.application.dto.TripResult;
import com.cosain.trilo.trip.query.domain.dto.TripDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TripDetailResponse {
    private final Long tripId;
    private final String title;
    private final String status;
    private LocalDate startDate;
    private LocalDate endDate;
    public static TripDetailResponse from(TripResult tripResult){
        return TripDetailResponse.builder()
                .tripId(tripResult.getId())
                .title(tripResult.getTitle())
                .status(tripResult.getStatus())
                .startDate(tripResult.getStartDate())
                .endDate(tripResult.getEndDate())
                .build();
    }

    @Builder(access = AccessLevel.PRIVATE)
    private TripDetailResponse(final Long tripId, final String title,final String status, LocalDate startDate, LocalDate endDate) {
        this.tripId = tripId;
        this.title = title;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}