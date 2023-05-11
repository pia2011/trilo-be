package com.cosain.trilo.trip.command.application.command;

import lombok.Getter;

@Getter
public class ScheduleMoveCommand {

    private Long targetDayId;
    private int targetOrder;

    public static ScheduleMoveCommand of(Long targetDayId, int targetOrder) {
        return new ScheduleMoveCommand(targetDayId, targetOrder);
    }

    private ScheduleMoveCommand(Long targetDayId, int targetOrder) {
        this.targetDayId = targetDayId;
        this.targetOrder = targetOrder;
    }
}