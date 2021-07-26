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
@AllArgsConstructor
@NoArgsConstructor
public class User {

    public static final String ROLE_ADMIN="admin";
    public static final String ROLE_ENCODER="encoder";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String fullname;

    @Column
    private String username;

    @Column
    private String password;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="date_created")
    private Date dateCreated=new Date();

    @Column(name="user_role")
    private String userRole;
}
