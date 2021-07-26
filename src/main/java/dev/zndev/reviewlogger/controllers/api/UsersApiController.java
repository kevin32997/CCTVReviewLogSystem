package dev.zndev.reviewlogger.controllers.api;

import dev.zndev.reviewlogger.configurations.SystemKeys;
import dev.zndev.reviewlogger.controllers.others.TableUpdateService;
import dev.zndev.reviewlogger.helpers.Helper;
import dev.zndev.reviewlogger.helpers.ResourceHelper;
import dev.zndev.reviewlogger.models.User;
import dev.zndev.reviewlogger.models.others.Response;
import dev.zndev.reviewlogger.repositories.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UsersApiController {

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private TableUpdateService updateService;

    /*==================================================================================================================
                                                        POST REQUEST
    ==================================================================================================================*/

    @PostMapping("api/users/add")
    private Response addUser(@ModelAttribute User user) {
        try {
            User savedUser = usersRepo.save(user);

            // Table Update
            updateService.updateTable(SystemKeys.TABLE_UPDATE_KEY_USERS);

            List<User> list = new ArrayList<>();
            list.add(savedUser);
            return Helper.createResponse("User Saved.", true, list);
        } catch (Exception ex) {
            return Helper.createResponse("An Error Occurred\n" + ex.toString(), false);
        }
    }

    /*==================================================================================================================
                                                        GET REQUEST
    ==================================================================================================================*/


    @GetMapping("api/users/view/{id}")
    private Response getUserById(@PathVariable("id") int id) {
        Optional<User> userOptional = usersRepo.findById(id);
        List<User> list = new ArrayList<>();
        list.add(userOptional.get());
        Response response = Helper.createResponse("Request Successful", true);
        response.setList(list);
        return response;
    }

    @GetMapping("api/users/page/{page}/{size}/sort/{sortType}/{sortBy}")
    private Response getUserByPageSorted(
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
        Page<User> userPage = usersRepo.findAll(pageRequest);
        Response response = Helper.createResponse("Request Successful", true);
        response.setList(userPage.getContent());
        return response;
    }

    @GetMapping("api/users/check/{user_name}/{password}")
    private Response checkUser(@PathVariable("user_name") String username, @PathVariable("password") String password) {

        Optional<User> userOptional = usersRepo.findByUsernameAndPassword(username, password);
        List<User> list = new ArrayList<>();
        Response response;
        if (!userOptional.isEmpty()) {
            response = Helper.createResponse("Request Successful wew", true);
            list.add(userOptional.get());
        } else {
            response = Helper.createResponse("Wrong Username or Password.", false);
        }
        response.setList(list);
        return response;
    }

    // Counts ///////////////////////

    @GetMapping("api/users/count")
    private Map<String, Long> getUsersCount() {
        HashMap<String, Long> map = new HashMap<>();
        map.put("count", usersRepo.count());
        return map;
    }

    /*==================================================================================================================
                                                        PUT REQUEST
    ==================================================================================================================*/

    @PutMapping("api/user/update/{user_id}")
    private Response updateUser(@PathVariable("user_id") int userId, @ModelAttribute User details) {
        try {
            Optional<User> optionalUser = usersRepo.findById(userId);
            User user = optionalUser.get();
            user.setFullname(details.getFullname());
            user.setUsername(details.getUsername());
            user.setPassword(details.getPassword());
            user.setUserRole(details.getUserRole());
            User savedUser = usersRepo.save(user);
            List<User> list = new ArrayList<>();
            list.add(savedUser);
            return Helper.createResponse("User Updated.", true, list);
        } catch (Exception ex) {
            return Helper.createResponse("Error: " + ex.toString(), false);
        }
    }


}
