package org.cps.labman.service;
import org.cps.labman.dto.AppointmentDto;
import org.cps.labman.persistence.model.Appointment;
import org.cps.labman.persistence.repo.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    public void testCreateAppointment() {
        // Prepare test data
        Long userId = 123L;
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(1L);
        appointmentDto.setPatientName("John Doe");
        appointmentDto.setTestName("Test A");
        appointmentDto.setLocation("Location A");
        appointmentDto.setAppointmentDate("2023-07-05");
        appointmentDto.setAppointmentTime("9:00 AM");
        appointmentDto.setStatus("Scheduled");

        // Perform the method call
        appointmentService.createAppointment(userId, appointmentDto);

        // Verify that the appointment is saved with the correct data
        Appointment expectedAppointment = new Appointment();
        expectedAppointment.setId(1L);
        expectedAppointment.setUserId(userId);
        expectedAppointment.setPatientName("John Doe");
        expectedAppointment.setTestName("Test A");
        expectedAppointment.setLocation("Location A");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
        LocalDateTime appointmentTime = LocalDateTime.parse("2023-07-05 9:00 AM", formatter);
        expectedAppointment.setAppointmentDate(appointmentTime);
        expectedAppointment.setStatus("Scheduled");

        verify(appointmentRepository).save(expectedAppointment);
    }
}
