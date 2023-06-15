package com.cosain.trilo.trip.presentation.day.command.dto;

import lombok.Getter;

@Getter
public class DayColorUpdateResponse {

    private Long dayId;

    public DayColorUpdateResponse (Long dayId) {
        this.dayId = dayId;
    }
}
