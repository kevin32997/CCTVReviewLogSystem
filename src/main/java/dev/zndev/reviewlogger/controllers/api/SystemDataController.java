package dev.zndev.reviewlogger.controllers.api;

import dev.zndev.reviewlogger.helpers.Helper;
import dev.zndev.reviewlogger.models.others.Response;
import dev.zndev.reviewlogger.models.others.SystemData;
import dev.zndev.reviewlogger.repositories.SystemDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
public class SystemDataController {

    @Autowired
    private SystemDataRepo systemDataRepo;

    @GetMapping("api/system_data/view/{key}")
    private Map<String, String> getConfigByKey(@PathVariable("key") String key) {
        HashMap<String, String> map = new HashMap<>();
        Optional<SystemData> systemData = systemDataRepo.findById(key);


        if (!systemData.isEmpty()) {
            map.put(systemData.get().getKey(), systemData.get().getValue());
        }

        return map;
    }


    @GetMapping("api/system_data/test_connection")
    private Response testConnection(){
        return Helper.createResponse("Connected to Server",true);
    }
}
