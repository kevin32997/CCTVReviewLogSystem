package dev.zndev.reviewlogger.controllers.api;

import dev.zndev.reviewlogger.helpers.Helper;
import dev.zndev.reviewlogger.models.ReviewLog;
import dev.zndev.reviewlogger.models.others.Response;
import dev.zndev.reviewlogger.repositories.IncidentRepo;
import dev.zndev.reviewlogger.repositories.ReviewLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.List;

@RestController
public class ReportingApiController {

    @Autowired
    private ReviewLogRepo reviewLogRepo;

    @Autowired
    private IncidentRepo incidentRepo;


    /*
        Get Review Log by Date Range, Date Format: YYYY-mm-dd
     */
    @GetMapping("api/report/review/date_range/{date_from}/{date_until}")
    private Response getReviewByDateRange(@PathVariable("date_from") String dateFrom, @PathVariable("date_until") String dateUntil) {
        try {
            List<ReviewLog> list = reviewLogRepo.findAllBetweenDates(dateFrom, dateUntil);
            for (ReviewLog log : list) {
                log.setIncidentCount((int) incidentRepo.countByReviewLogId(log.getId()));
            }

            return Helper.createResponse("Request Successful", true, list);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Helper.createResponse("An Error Occurred\n" + ex.toString(), false);
        }
    }

    /*
        Get Review Log by Month and Year, Date Format: (mm-yyyy)
     */

    @GetMapping("api/report/review/month_year/{month}/{year}")
    private Response getReviewByMonthAndYear(@PathVariable("month") String month, @PathVariable("year") String year) {
        try {
            DecimalFormat decimalFormat=new DecimalFormat("00");
            String dateYearFormatted=decimalFormat.format(Integer.parseInt(month))+"-"+year;
            List<ReviewLog> list = reviewLogRepo.findAllByMonthAndYear(dateYearFormatted);
            for (ReviewLog log : list) {
                log.setIncidentCount((int) incidentRepo.countByReviewLogId(log.getId()));
            }
            return Helper.createResponse("Request Successful", true, list);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Helper.createResponse("An Error Occurred\n" + ex.toString(), false);
        }
    }

}
