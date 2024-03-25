package org.cps.labman.persistence.repo;

import org.cps.labman.persistence.model.Appointment;
import org.cps.labman.persistence.repo.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentRepositoryTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Test
    void findAllByUserId_shouldReturnAppointmentsForUser() {
        List<Appointment> appointments = Arrays.asList(
                new Appointment(),
                new Appointment()
        );

        when(appointmentRepository.findAllByUserId(1L)).thenReturn(appointments);

        List<Appointment> foundAppointments = appointmentRepository.findAllByUserId(1L);

        assertEquals(appointments, foundAppointments);
    }
}