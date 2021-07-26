package dev.zndev.reviewlogger.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="review_log_id")
    private int reviewLogId;

    @Column(name="day")
    private String day;

    @Column(name="time")
    private String time;

    @Column(name="incident_date")
    private Date incidentDate;

    @Column(name="incident_description")
    private String incidentDescription;


}
