package dev.zndev.reviewlogger.controllers.api;

import dev.zndev.reviewlogger.configurations.SystemKeys;
import dev.zndev.reviewlogger.controllers.others.TableUpdateService;
import dev.zndev.reviewlogger.helpers.Helper;
import dev.zndev.reviewlogger.models.Incident;
import dev.zndev.reviewlogger.models.others.Response;
import dev.zndev.reviewlogger.repositories.IncidentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@RestController
public class IncidentApiController {

    @Autowired
    private IncidentRepo incidentRepo;

    @Autowired
    private TableUpdateService tableUpdateService;

    /*==================================================================================================================
                                                        POST REQUEST
    ==================================================================================================================*/

    @PostMapping("api/incident/add")
    private Response addIncident(@ModelAttribute Incident incident) {
        try {
            if(incident.getTime()!=null) {
                if (!incident.getTime().equals("")) {
                    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    df.setTimeZone(TimeZone.getTimeZone("UTC"));
                    incident.setIncidentDate(df.parse(incident.getDay()+" "+incident.getTime()));
                } else {
                    incident.setIncidentDate(new SimpleDateFormat("yyyy-MM-dd").parse(incident.getDay()));
                    System.out.println("There is no time");
                    System.out.println("Incident date is "+incident.getIncidentDate());
                }
            }else{
                incident.setIncidentDate(new SimpleDateFormat("yyyy-MM-dd").parse(incident.getDay()));
            }
            Incident savedIncident = incidentRepo.save(incident);


            // Table Update
            tableUpdateService.updateTable(SystemKeys.TABLE_UPDATE_KEY_INCIDENTS);

            List<Incident> list = new ArrayList<>();
            list.add(savedIncident);
            return Helper.createResponse("Incident Saved.", true, list);
        } catch (Exception ex) {
            return Helper.createResponse("An Error Occurred\n" + ex.toString(), false);
        }
    }

    /*==================================================================================================================
                                                        GET REQUEST
    ==================================================================================================================*/

    // Counts ///////////////////////

    @GetMapping("api/incident/count")
    private Map<String, Long> getIncidentCount() {
        HashMap<String, Long> map = new HashMap<>();
        map.put("count", incidentRepo.count());
        return map;
    }

    @GetMapping("api/incident/count_by_review_log/{review_log_id}")
    private Map<String, Long> getIncidentCountByReviewLog(@PathVariable("review_log_id") int reviewLogId) {
        HashMap<String, Long> map = new HashMap<>();
        map.put("count", incidentRepo.countByReviewLogId(reviewLogId));
        return map;
    }

    // By ReviewLog /////////////////////////
    @GetMapping("api/incident/view_by_review_log/{review_log_id}")
    private Response getIncidentsByReviewLogId(
            @PathVariable("review_log_id") int reviewLogId) {
        List<Incident> incidents = incidentRepo.findByReviewLogId(reviewLogId);
        Response response = Helper.createResponse("Request Successful", true);
        response.setList(incidents);
        return response;
    }
}
