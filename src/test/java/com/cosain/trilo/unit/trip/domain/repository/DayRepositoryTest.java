package com.cosain.trilo.unit.trip.domain.repository;

import com.cosain.trilo.fixture.TripFixture;
import com.cosain.trilo.support.RepositoryTest;
import com.cosain.trilo.trip.domain.entity.Day;
import com.cosain.trilo.trip.domain.entity.Trip;
import com.cosain.trilo.trip.domain.repository.DayRepository;
import com.cosain.trilo.trip.domain.vo.TripPeriod;
import com.cosain.trilo.trip.domain.vo.TripStatus;
import com.cosain.trilo.trip.domain.vo.TripTitle;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RepositoryTest
public class DayRepositoryTest {

    @Autowired
    private DayRepository dayRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("findBYWithTripTest - 같이 가져온 Trip이 실제 Trip 클래스인지 함께 검증")
    public void findByIdWithTripTest() {
        // given
        Trip trip = TripFixture.DECIDED_TRIP.createDecided(null, 1L, "여행 제목", LocalDate.of(2023,3,1), LocalDate.of(2023,3,1));
        em.persist(trip);

        Day day = trip.getDays().get(0);
        em.persist(day);

        em.flush();
        em.clear();

        // when
        Day findDay = dayRepository.findByIdWithTrip(day.getId()).get();

        // then
        Trip findDayTrip = findDay.getTrip();

        assertThat(findDay.getId()).isEqualTo(day.getId());
        assertThat(findDay.getTripDate()).isEqualTo(day.getTripDate());
        assertThat(findDayTrip.getId()).isEqualTo(trip.getId());
        assertThat(findDayTrip.getClass()).isSameAs(Trip.class);
    }

    @Test
    @DisplayName("deleteAllByIds- 전달받은 Id 목록의 Day들을 삭제")
    void deleteAllByIdsTest() {
        // given
        Trip trip = TripFixture.DECIDED_TRIP.createDecided(null, 1L, "여행 제목", LocalDate.of(2023,5,1), LocalDate.of(2023,5,4));
        em.persist(trip);

        Day day1 = trip.getDays().get(0);
        Day day2 = trip.getDays().get(1);
        Day day3 = trip.getDays().get(2);
        Day day4 = trip.getDays().get(3);

        em.persist(day1);
        em.persist(day2);
        em.persist(day3);
        em.persist(day4);

        // when
        dayRepository.deleteAllByIds(List.of(day1.getId(), day2.getId()));

        // then
        List<Day> remainingDays = dayRepository.findAll();
        assertThat(remainingDays.size()).isEqualTo(2);
        assertThat(remainingDays).map(Day::getId).containsExactlyInAnyOrder(day3.getId(), day4.getId());
    }

    @Test
    @DirtiesContext
    @DisplayName("deleteAllByTripId로 Day를 삭제하면, 해당 여행의 모든 Day들이 삭제된다.")
    void deleteAllByTripIdTest() {
        // given
        Trip trip = TripFixture.DECIDED_TRIP.createDecided(null, 1L, "여행 제목", LocalDate.of(2023,3,1), LocalDate.of(2023,3,3));

        em.persist(trip);

        Day day1 = trip.getDays().get(0);
        Day day2 = trip.getDays().get(1);
        Day day3 = trip.getDays().get(2);

        em.persist(day1);
        em.persist(day2);
        em.persist(day3);

        // when
        dayRepository.deleteAllByTripId(trip.getId());
        em.clear();

        // then
        List<Day> findDays = dayRepository.findAllById(List.of(day1.getId(), day2.getId(), day3.getId()));
        assertThat(findDays).isEmpty();
    }
}
