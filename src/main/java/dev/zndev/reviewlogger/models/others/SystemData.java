package dev.zndev.reviewlogger.models.others;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemData {

    @Id
    @Column(name = "system_key", unique = true, nullable = false)
    private String key;

    @Column(name = "system_value")
    private String value;

}
