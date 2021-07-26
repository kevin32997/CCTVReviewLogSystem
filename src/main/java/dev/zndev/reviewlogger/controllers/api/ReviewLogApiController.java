package dev.zndev.reviewlogger.controllers.api;


import dev.zndev.reviewlogger.configurations.SystemKeys;
import dev.zndev.reviewlogger.controllers.others.TableUpdateService;
import dev.zndev.reviewlogger.helpers.Helper;
import dev.zndev.reviewlogger.helpers.ResourceHelper;
import dev.zndev.reviewlogger.models.ReviewLog;
import dev.zndev.reviewlogger.models.others.Response;
import dev.zndev.reviewlogger.models.others.SystemData;
import dev.zndev.reviewlogger.repositories.ReviewLogRepo;
import dev.zndev.reviewlogger.repositories.SystemDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
public class ReviewLogApiController {

    @Autowired
    private ReviewLogRepo reviewLogRepo;

    @Autowired
    private SystemDataRepo systemDataRepo;

    @Autowired
    private TableUpdateService tableUpdateService;

    /*==================================================================================================================
                                                        POST REQUEST
    ==================================================================================================================*/

    @PostMapping("api/review_log/add")
    private Response addReview(@ModelAttribute ReviewLog reviewLog) {
        try {
            int id = Integer.parseInt(systemDataRepo.findById(SystemKeys.CONFIG_KEY_LASTREVIEWID).get().getValue()) + 1;

            reviewLog.setId(id);
            ReviewLog savedReviewLog = reviewLogRepo.save(reviewLog);

            // Updates
            tableUpdateService.updateTable(SystemKeys.TABLE_UPDATE_KEY_REVIEW_LOG);
            systemDataRepo.save(new SystemData(SystemKeys.CONFIG_KEY_LASTREVIEWID, "" + id));

            List<ReviewLog> list = new ArrayList<>();
            list.add(savedReviewLog);
            return Helper.createResponse("Review Saved.", true, list);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Helper.createResponse("An Error Occurred\n" + ex.toString(), false);
        }
    }

    /*==================================================================================================================
                                                        GET REQUEST
    ==================================================================================================================*/

    // Counts ///////////////////////

    @GetMapping("api/review_log/count")
    private Map<String, Long> getReviewCount() {
        HashMap<String, Long> map = new HashMap<>();
        map.put("count", reviewLogRepo.count());
        return map;
    }

    @GetMapping("api/review_log/count_user/{user_id}")
    private Map<String, Long> getReviewCountByUser(@PathVariable("user_id") int id) {
        HashMap<String, Long> map = new HashMap<>();
        map.put("count", reviewLogRepo.countByReviewerId(id));
        return map;
    }

    @GetMapping("api/review_log/count_personnel/{per_id}")
    private Map<String, Long> getReviewCountByPersonnel(@PathVariable("per_id") int id) {
        HashMap<String, Long> map = new HashMap<>();
        map.put("count", reviewLogRepo.countByPersonnelId(id));
        return map;
    }

    // Page /////////////////////////

    @GetMapping("api/review_log/page/{page}/{size}/sort/{sortType}/{sortBy}")
    private Response getReviewByPageSorted(
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
        Page<ReviewLog> reviewLogPage = reviewLogRepo.findAll(pageRequest);
        Response response = Helper.createResponse("Request Successful", true);
        response.setList(reviewLogPage.getContent());
        return response;
    }


    // Page by User /////////////////////////

    @GetMapping("api/review_log/page_user/{reviewer_id}/{page}/{size}/sort/{sortType}/{sortBy}")
    private Response getReviewByPageAndUserSorted(
            @PathVariable("reviewer_id") int reviewer_id,
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
        List<ReviewLog> reviewLogPage = reviewLogRepo.findAllByReviewerId(reviewer_id, pageRequest);
        Response response = Helper.createResponse("Request Successful", true);
        response.setList(reviewLogPage);
        return response;
    }

    // Page by Personnel /////////////////////////

    @GetMapping("api/review_log/page_personnel/{per_id}/{page}/{size}/sort/{sortType}/{sortBy}")
    private Response getReviewByPageAndPersonnelSorted(
            @PathVariable("per_id") int per_id,
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
        List<ReviewLog> reviewLogPage = reviewLogRepo.findAllByPersonnelId(per_id, pageRequest);
        Response response = Helper.createResponse("Request Successful", true);
        response.setList(reviewLogPage);
        return response;
    }

    // Search ///////////////////////

    @GetMapping("api/review_log/search/{search}/{size}")
    private Response searchReview(@PathVariable("search") String search, @PathVariable("size") int size) {
        List<ReviewLog> searchedItems =
                reviewLogRepo.findByIdOrPersonnel_firstNameContainsOrPersonnel_lastNameContainsOrPersonnel_middleNameContains(
                        search, search, search, search, PageRequest.of(0, size));
        Response response = Helper.createResponse("Request Successful", true);
        response.setList(searchedItems);
        return response;
    }

    @GetMapping("api/review_log/view/{review_id}")
    private Response getReviewLog(@PathVariable("review_id") int id) {
        Optional<ReviewLog> optionalReviewLog=reviewLogRepo.findById(id);
        List<ReviewLog> list=new ArrayList<>();

        list.add(optionalReviewLog.get());
        Response response = Helper.createResponse("Request Successful", true);
        response.setList(list);
        return response;
    }


}
