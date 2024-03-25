package org.cps.labman.persistence.repo;

import org.cps.labman.persistence.model.Appointment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AppointmentRepository extends CrudRepository<Appointment,Long> {
    List<Appointment> findAllByUserId(Long userId);
}
