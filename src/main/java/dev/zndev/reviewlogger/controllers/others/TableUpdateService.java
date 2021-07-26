package dev.zndev.reviewlogger.controllers.others;


import dev.zndev.reviewlogger.models.others.TableUpdate;
import dev.zndev.reviewlogger.repositories.TableUpdatesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TableUpdateService {

    @Autowired
    TableUpdatesRepo tableUpdatesRepo;

    public void updateTable(String table_key) {
        tableUpdatesRepo.save(new TableUpdate(table_key, new Date()));
    }
}
