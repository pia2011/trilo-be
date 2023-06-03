package com.cosain.trilo.trip.application.trip.command.usecase.dto.factory;

import com.cosain.trilo.common.exception.CustomException;
import com.cosain.trilo.common.exception.CustomValidationException;
import com.cosain.trilo.trip.application.trip.command.usecase.dto.TripCreateCommand;
import com.cosain.trilo.trip.application.trip.command.usecase.dto.TripUpdateCommand;
import com.cosain.trilo.trip.domain.vo.TripPeriod;
import com.cosain.trilo.trip.domain.vo.TripTitle;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class TripUpdateCommandFactory {

    public TripUpdateCommand createCommand(String rawTitle, LocalDate startDate, LocalDate endDate) {
        List<CustomException> exceptions = new ArrayList<>();
        TripTitle tripTitle = makeTripTitle(rawTitle, exceptions);
        TripPeriod tripPeriod = makeTripPeriod(startDate, endDate, exceptions);

        if (!exceptions.isEmpty()) {
            throw new CustomValidationException(exceptions);
        }
        return new TripUpdateCommand(tripTitle, tripPeriod);
    }

    private TripTitle makeTripTitle(String rawTitle, List<CustomException> exceptions) {
        try {
            return TripTitle.of(rawTitle);
        } catch (CustomException e) {
            exceptions.add(e);
        }
        return null;
    }


    private TripPeriod makeTripPeriod(LocalDate startDate, LocalDate endDate, List<CustomException> exceptions) {
        try {
            return TripPeriod.of(startDate, endDate);
        } catch (CustomException e) {
            exceptions.add(e);
        }
        return null;
    }
}