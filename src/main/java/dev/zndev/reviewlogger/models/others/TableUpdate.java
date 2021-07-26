package dev.zndev.reviewlogger.models.others;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TableUpdate {

    @Id
    @Column(name = "table_name", unique = true, nullable = false)
    private String tableName;

    @Column(name = "last_update")
    private Date lastUpdate=new Date();

}
