package dev.zndev.reviewlogger.controllers.api;

import dev.zndev.reviewlogger.configurations.SystemKeys;
import dev.zndev.reviewlogger.controllers.others.TableUpdateService;
import dev.zndev.reviewlogger.helpers.Helper;
import dev.zndev.reviewlogger.models.Incident;
import dev.zndev.reviewlogger.models.ReviewLog;
import dev.zndev.reviewlogger.models.others.Response;
import dev.zndev.reviewlogger.repositories.IncidentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
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
            if (incident.getTime() != null) {
                if (!incident.getTime().equals("")) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    df.setTimeZone(TimeZone.getTimeZone("UTC"));
                    incident.setIncidentDate(df.parse(incident.getDay() + " " + incident.getTime()));
                } else {
                    incident.setIncidentDate(new SimpleDateFormat("yyyy-MM-dd").parse(incident.getDay()));
                    System.out.println("There is no time");
                    System.out.println("Incident date is " + incident.getIncidentDate());
                }
            } else {
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

    /*==================================================================================================================
                                                       UPDATE REQUEST
    ==================================================================================================================*/

    @PutMapping("api/incident/update/{incident_id}")
    private Response updateIncident(@PathVariable("incident_id") int incidentId, @ModelAttribute Incident details) {
        try {
            Optional<Incident> optionalIncident = incidentRepo.findById(incidentId);
            Incident incident = optionalIncident.get();
            if (details.getTime() != null) {
                if (!details.getTime().equals("")) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    df.setTimeZone(TimeZone.getTimeZone("UTC"));
                    incident.setTime(details.getTime());
                    incident.setDay(details.getDay());
                    incident.setIncidentDate(df.parse(details.getDay() + " " + details.getTime()));
                } else {
                    incident.setTime("");
                    incident.setDay(details.getDay());
                    incident.setIncidentDate(new SimpleDateFormat("yyyy-MM-dd").parse(details.getDay()));
                }
            } else {
                incident.setTime("");
                incident.setDay(details.getDay());
                incident.setIncidentDate(new SimpleDateFormat("yyyy-MM-dd").parse(details.getDay()));
            }

            incident.setIncidentDescription(details.getIncidentDescription());

            Incident updatedIncident = incidentRepo.save(incident);

            // Auto Update
            tableUpdateService.updateTable(SystemKeys.TABLE_UPDATE_KEY_INCIDENTS);

            List<Incident> list = new ArrayList<>();
            list.add(updatedIncident);
            return Helper.createResponse("Incident Updated.", true, list);
        } catch (Exception ex) {
            return Helper.createResponse("Error: " + ex.toString(), false);
        }
    }

    /*==================================================================================================================
                                                       DELETE REQUEST
    ==================================================================================================================*/

    @DeleteMapping("api/incident/delete/{incident_id}")
    private Response deleteIncident(@PathVariable("incident_id") int incidentId) {
        try {
            Optional<Incident> optionalIncident = incidentRepo.findById(incidentId);
            if (optionalIncident.isPresent()) {
                incidentRepo.delete(optionalIncident.get());
                tableUpdateService.updateTable(SystemKeys.TABLE_UPDATE_KEY_INCIDENTS);


                List<Incident> list = new ArrayList<>();
                list.add(optionalIncident.get());

                return Helper.createResponse("Incident Deleted.", true, list);
            }

            return Helper.createResponse("Incident id doesn't exist.", false);
        } catch (Exception ex) {
            return Helper.createResponse("Error: " + ex.toString(), false);
        }

    }


}
