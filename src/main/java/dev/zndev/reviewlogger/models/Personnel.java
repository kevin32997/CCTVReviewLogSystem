package dev.zndev.reviewlogger.models;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Personnel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="middle_name")
    private String middleName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="office")
    private String office;

    @Column(name="position")
    private String position;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="date_created")
    private Date dateCreated=new Date();

    @Column(name="added_by")
    private int addedBy;
}
