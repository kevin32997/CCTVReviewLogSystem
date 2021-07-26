package dev.zndev.reviewlogger.configurations;

import dev.zndev.reviewlogger.models.User;
import dev.zndev.reviewlogger.models.others.SystemData;
import dev.zndev.reviewlogger.models.others.TableUpdate;
import dev.zndev.reviewlogger.repositories.SystemDataRepo;
import dev.zndev.reviewlogger.repositories.TableUpdatesRepo;
import dev.zndev.reviewlogger.repositories.UsersRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Optional;

@Configuration
public class AppStartupConfiguration {
    private static final Logger log = LoggerFactory.getLogger(AppStartupConfiguration.class);


    @Autowired
    private SystemDataRepo systemDataRepo;

    @Autowired
    private TableUpdatesRepo tableUpdatseRepo;

    @Autowired
    private UsersRepo usersRepo;

    @PostConstruct
    private void init() {
        log.info("Running Configurations");
        checkSystemData();
    }

    private void checkSystemData(){
        log.info("Checking "+SystemKeys.CONFIG_KEY_LASTREVIEWID+" initial data . . .");
        Optional<SystemData> lastReviewId=systemDataRepo.findById(SystemKeys.CONFIG_KEY_LASTREVIEWID);
        if(lastReviewId.isEmpty()){
            log.info("Creating "+SystemKeys.CONFIG_KEY_LASTREVIEWID+" initial data.");
            systemDataRepo.save(new SystemData(SystemKeys.CONFIG_KEY_LASTREVIEWID,"0"));
        }


        log.info("Checking "+SystemKeys.TABLE_UPDATE_KEY_REVIEW_LOG+" initial data . . .");
        Optional<TableUpdate> reviewLogTableUpdates=tableUpdatseRepo.findById(SystemKeys.TABLE_UPDATE_KEY_REVIEW_LOG);
        if(reviewLogTableUpdates.isEmpty()){
            log.info("Creating "+SystemKeys.TABLE_UPDATE_KEY_REVIEW_LOG+" initial data.");
            tableUpdatseRepo.save(new TableUpdate(SystemKeys.TABLE_UPDATE_KEY_REVIEW_LOG,new Date()));
        }

        log.info("Checking "+SystemKeys.TABLE_UPDATE_KEY_PERSONNEL+" initial data . . .");
        Optional<TableUpdate> personnelTableUpdates=tableUpdatseRepo.findById(SystemKeys.TABLE_UPDATE_KEY_PERSONNEL);
        if(personnelTableUpdates.isEmpty()){
            log.info("Creating "+SystemKeys.TABLE_UPDATE_KEY_PERSONNEL+" initial data.");
            tableUpdatseRepo.save(new TableUpdate(SystemKeys.TABLE_UPDATE_KEY_PERSONNEL,new Date()));
        }


        log.info("Checking "+SystemKeys.TABLE_UPDATE_KEY_INCIDENTS+" initial data . . .");
        Optional<TableUpdate> incidentTableUpdates=tableUpdatseRepo.findById(SystemKeys.TABLE_UPDATE_KEY_INCIDENTS);
        if(incidentTableUpdates.isEmpty()){
            log.info("Creating "+SystemKeys.TABLE_UPDATE_KEY_INCIDENTS+" initial data.");
            tableUpdatseRepo.save(new TableUpdate(SystemKeys.TABLE_UPDATE_KEY_INCIDENTS,new Date()));
        }


        log.info("Checking "+SystemKeys.TABLE_UPDATE_KEY_USERS+" initial data . . .");
        Optional<TableUpdate> userTableUpdates=tableUpdatseRepo.findById(SystemKeys.TABLE_UPDATE_KEY_USERS);
        if(userTableUpdates.isEmpty()){
            log.info("Creating "+SystemKeys.TABLE_UPDATE_KEY_USERS+" initial data.");
            tableUpdatseRepo.save(new TableUpdate(SystemKeys.TABLE_UPDATE_KEY_USERS,new Date()));
        }

        // Create Default user
        log.info("Checking users . . .");

        if (usersRepo.count() <= 0) {
            log.info("There are no users.");
            log.info("Creating default user. . .");

            // Create user
            User user = new User();
            user.setFullname("Super Admin");
            user.setUserRole(User.ROLE_ADMIN);
            user.setUsername("admin");
            user.setPassword("12345678");

            // Save User
            usersRepo.save(user);
            log.info("Default user created.");
        }else{
            log.info("Already has users.");
        }
    }
}
