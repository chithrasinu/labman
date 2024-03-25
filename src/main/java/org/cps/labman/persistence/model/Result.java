package org.cps.labman.persistence.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="results")
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "test_id")
    private long testId;

    @Column(name = "appointment_id")
    private long appointmentId;

    @Column(name = "test_date")
    private Date testDate;

    @Column(name = "result_status")
    private String resultStatus;
}