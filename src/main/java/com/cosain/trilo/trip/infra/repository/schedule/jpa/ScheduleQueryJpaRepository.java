package com.cosain.trilo.trip.infra.repository.schedule.jpa;

import com.cosain.trilo.trip.domain.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleQueryJpaRepository extends JpaRepository<Schedule, Long>, ScheduleQueryJpaRepositoryCustom{
}