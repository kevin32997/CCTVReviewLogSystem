package dev.zndev.reviewlogger.controllers.api;

import dev.zndev.reviewlogger.helpers.Helper;
import dev.zndev.reviewlogger.models.others.Response;
import dev.zndev.reviewlogger.models.others.TableUpdate;
import dev.zndev.reviewlogger.repositories.TableUpdatesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class TableUpdateApiController {

    @Autowired
    private TableUpdatesRepo updatesRepo;

    @GetMapping("api/table_updates/view")
    private Response getUpdates() {
        List<TableUpdate> updates = updatesRepo.findAll();
        return Helper.createResponse("Request Successful", true, updates);
    }



}
