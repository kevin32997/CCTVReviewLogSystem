package dev.zndev.reviewlogger.controllers.api;


import dev.zndev.reviewlogger.configurations.SystemKeys;
import dev.zndev.reviewlogger.controllers.others.TableUpdateService;
import dev.zndev.reviewlogger.helpers.Helper;
import dev.zndev.reviewlogger.helpers.ResourceHelper;
import dev.zndev.reviewlogger.models.Personnel;
import dev.zndev.reviewlogger.models.ReviewLog;
import dev.zndev.reviewlogger.models.others.Response;
import dev.zndev.reviewlogger.repositories.PersonnelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class PersonnelApiController {

    @Autowired
    private PersonnelRepo personnelRepo;

    @Autowired
    private TableUpdateService tableUpdateService;

    /*==================================================================================================================
                                                        POST REQUEST
    ==================================================================================================================*/

    @PostMapping("api/personnel/add")
    private Response addPersonnel(@ModelAttribute Personnel personnel) {
        try {
            Personnel savedPersonnel = personnelRepo.save(personnel);

            // Auto Update
            tableUpdateService.updateTable(SystemKeys.TABLE_UPDATE_KEY_PERSONNEL);

            List<Personnel> list = new ArrayList<>();
            list.add(savedPersonnel);
            return Helper.createResponse("Personnel Saved.", true, list);
        } catch (Exception ex) {
            return Helper.createResponse("An Error Occurred\n" + ex.toString(), false);
        }
    }

    /*==================================================================================================================
                                                        GET REQUEST
    ==================================================================================================================*/

    // Counts ///////////////////////

    @GetMapping("api/personnel/count")
    private Map<String, Long> getPersonnelCount() {
        HashMap<String, Long> map = new HashMap<>();
        map.put("count", personnelRepo.count());
        return map;
    }

    // Page /////////////////////////

    @GetMapping("api/personnel/page/{page}/{size}/sort/{sortType}/{sortBy}")
    private Response getPersonnelByPageSorted(
            @PathVariable("page") int page,
            @PathVariable("size") int size,
            @PathVariable("sortType") String sortType,
            @PathVariable("sortBy") String sortBy) {
        Sort sort = null;

        if (sortType.equals(ResourceHelper.DIRECTION_ASCENDING)) {
            sort = Sort.by(Sort.Direction.ASC, sortBy);
        } else if (sortType.equals(ResourceHelper.DIRECTION_DESCENDING)) {
            sort = Sort.by(Sort.Direction.DESC, sortBy);
        }

        Pageable pageRequest = PageRequest.of(page - 1, size, sort);
        Page<Personnel> personnelPage = personnelRepo.findAll(pageRequest);
        Response response = Helper.createResponse("Request Successful", true);
        response.setList(personnelPage.getContent());
        return response;
    }

    // Search ///////////////////////

    @GetMapping("api/personnel/search/{search}/{size}")
    private Response searchPersonnel(@PathVariable("search") String search, @PathVariable("size") int size) {
        try {
            List<Personnel> searchedItems =
                    personnelRepo.findByfirstNameContainsOrMiddleNameContainsOrLastNameContainsOrOfficeContains(
                            search, search, search, search, PageRequest.of(0, size));
            Response response = Helper.createResponse("Request Successful", true);
            response.setList(searchedItems);
            return response;
        } catch (Exception ex) {
            return Helper.createResponse("An Error Occurred\n" + ex.toString(), false);
        }
    }

    // Get Single Personnel
    @GetMapping("api/personnel/view/{personnel_id}")
    private Response getPersonnelById(@PathVariable("personnel_id") int id) {
        Optional<Personnel> optionalReviewLog = personnelRepo.findById(id);
        List<Personnel> list = new ArrayList<>();
        list.add(optionalReviewLog.get());
        Response response = Helper.createResponse("Request Successful", true);
        response.setList(list);
        return response;
    }

    /*==================================================================================================================
                                                        PUT REQUEST
    ==================================================================================================================*/

    @PutMapping("api/personnel/update/{personnel_id}")
    private Response updatePersonnel(@PathVariable("personnel_id") int personnelId, @ModelAttribute Personnel details) {

        try {
            Optional<Personnel> optionalPersonnel = personnelRepo.findById(personnelId);
            Personnel personnel = optionalPersonnel.get();
            personnel.setFirstName(details.getFirstName());
            personnel.setLastName(details.getLastName());
            personnel.setMiddleName(details.getMiddleName());
            personnel.setOffice(details.getOffice());
            personnel.setPosition(details.getPosition());
            Personnel savedPersonnel = personnelRepo.save(personnel);

            // Auto Update
            tableUpdateService.updateTable(SystemKeys.TABLE_UPDATE_KEY_PERSONNEL);

            List<Personnel> list = new ArrayList<>();
            list.add(savedPersonnel);
            return Helper.createResponse("Personnel Updated.", true, list);
        } catch (Exception ex) {
            return Helper.createResponse("Error: " + ex.toString(), false);
        }
    }


}
