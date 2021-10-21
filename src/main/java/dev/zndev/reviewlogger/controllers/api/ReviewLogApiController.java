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

    /*
        Search by Personnel's Name
     */
    @GetMapping("api/review_log/search/personnel/{search}/{size}")
    private Response searchReviewByPersonnel(@PathVariable("search") String search, @PathVariable("size") int size) {
        try {
            List<ReviewLog> searchedItems =
                    reviewLogRepo.findAllByPersonnel_firstNameContainsOrPersonnel_lastNameContainsOrPersonnel_middleNameContainsOrPersonnel_officeContains(
                            search, search, search, search, PageRequest.of(0, size));
            Response response = Helper.createResponse("Request Successful", true);
            response.setList(searchedItems);
            return response;
        } catch (Exception ex) {
            return Helper.createResponse("Error: " + ex, false);
        }
    }

    /*
        Search by Review ID Pageable
     */
    @GetMapping("api/review_log/search/id/{search}/{size}")
    private Response searchReviewById(@PathVariable("search") String search, @PathVariable("size") int size) {
        try {
            // Check if searched is Integer
            int idToSearch = Integer.parseInt(search);

            List<ReviewLog> searchedItems =
                    reviewLogRepo.findAllById(
                            idToSearch, PageRequest.of(0, size));
            Response response = Helper.createResponse("Request Successful", true);
            response.setList(searchedItems);
            return response;
        } catch (NumberFormatException ex) {
            return Helper.createResponse("Error: " + ex, false);
        } catch (Exception ex) {
            return Helper.createResponse("Error: " + ex, false);
        }

    }


    @GetMapping("api/review_log/view/{review_id}")
    private Response getReviewLog(@PathVariable("review_id") int id) {
        Optional<ReviewLog> optionalReviewLog = reviewLogRepo.findById(id);
        List<ReviewLog> list = new ArrayList<>();

        list.add(optionalReviewLog.get());
        Response response = Helper.createResponse("Request Successful", true);
        response.setList(list);
        return response;
    }


    /*==================================================================================================================
                                                       UPDATE REQUEST
    ==================================================================================================================*/

    @PutMapping("api/review_log/update/{review_id}")
    private Response updateReviewInclusiveDates(@PathVariable("review_id") int reviewId, @ModelAttribute ReviewLog details) {
        try {
            System.out.println("UpdateReview: inclusive dates is " + details.getInclusiveDates());
            System.out.println("Review Log id is: " + reviewId);

            Optional<ReviewLog> optionalReviewLog = reviewLogRepo.findById(reviewId);
            ReviewLog reviewLog = optionalReviewLog.get();
            reviewLog.setInclusiveDates(details.getInclusiveDates());

            ReviewLog updatedReviewLog = reviewLogRepo.save(reviewLog);

            // Auto Update
            tableUpdateService.updateTable(SystemKeys.TABLE_UPDATE_KEY_REVIEW_LOG);

            List<ReviewLog> list = new ArrayList<>();
            list.add(updatedReviewLog);
            return Helper.createResponse("Review Log Updated.", true, list);
        } catch (Exception ex) {
            return Helper.createResponse("Error: " + ex, false);
        }
    }


    /*==================================================================================================================
                                                       DELETE REQUEST
    ==================================================================================================================*/

    @DeleteMapping("api/review_log/delete/{review_id}")
    private Response deleteReviewLog(@PathVariable("review_id") int reviewId) {
        try {
            Optional<ReviewLog> optionalReviewLog = reviewLogRepo.findById(reviewId);
            if (optionalReviewLog.isPresent()) {
                reviewLogRepo.delete(optionalReviewLog.get());
                tableUpdateService.updateTable(SystemKeys.TABLE_UPDATE_KEY_REVIEW_LOG);


                return Helper.createResponse("Review Log Deleted.", true);
            }

            return Helper.createResponse("Review id doesn't exist.", false);
        } catch (Exception ex) {
            return Helper.createResponse("Error: " + ex.toString(), false);
        }
    }

}
