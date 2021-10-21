package dev.zndev.reviewlogger.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.Date;

@Entity(name = "review_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewLog {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name="personnel_id")
    private int personnelId;

    @Column(name="incident_description")
    private String incidentDescription;

    @Column(name="inclusive_dates")
    private String inclusiveDates;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="review_date")
    private Date reviewDate=new Date();

    @Column(name="reviewer_id")
    private int reviewerId;

    @Column(name="reviewer_name")
    private String reviewerName;

    @ManyToOne(targetEntity = Personnel.class)
    @JoinColumn(name="personnel_id", insertable = false, updatable = false)
    private Personnel personnel;

    @JsonInclude()
    @Transient
    private int incidentCount;

}
