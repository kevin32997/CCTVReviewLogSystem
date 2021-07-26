package dev.zndev.reviewlogger.helpers;


import dev.zndev.reviewlogger.configurations.AppStartupConfiguration;
import dev.zndev.reviewlogger.models.others.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

public class Helper {
    private static final Logger log = LoggerFactory.getLogger(Helper.class);


    public Helper(){
    }

    public static Response createResponse(String message, Boolean status){
        Response response=new Response();
        response.setMessage(message);
        response.setStatus(status);
        return response;
    }

    public static Response createResponse(String message, Boolean status, List<?> list){
        Response response=new Response();
        response.setMessage(message);
        response.setStatus(status);
        response.setList(list);
        return response;
    }

    public static final String GenerateRandomString(int lenght){

        // create a string of uppercase and lowercase characters and numbers
        String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        // combine all strings
        String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;

        // create random string builder
        StringBuilder sb = new StringBuilder();

        // create an object of Random class
        Random random = new Random();

        // specify length of random string
        int length = 10;

        for(int i = 0; i < length; i++) {

            // generate random index number
            int index = random.nextInt(alphaNumeric.length());

            // get character specified by index
            // from the string
            char randomChar = alphaNumeric.charAt(index);

            // append the character to string builder
            sb.append(randomChar);
        }

    return sb.toString();
    }

    public static LocalDateTime convertToUtc(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    public static Date getDateTimeNow()  {
        LocalDateTime ldt=LocalDateTime.now();
        log.info("LocalDateTime is "+ldt.toString());
        log.info("Converted Utc is "+convertToUtc(ldt));
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return Date.from(convertToUtc(ldt).atZone(ZoneId.systemDefault()).toInstant());
    }
}
