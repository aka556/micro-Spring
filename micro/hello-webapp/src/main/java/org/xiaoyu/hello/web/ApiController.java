package org.xiaoyu.hello.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xiaoyu.hello.User;
import org.xiaoyu.hello.service.UserService;
import org.xiaoyu.micro.annotation.Autowired;
import org.xiaoyu.micro.annotation.GetMapping;
import org.xiaoyu.micro.annotation.PathVariable;
import org.xiaoyu.micro.annotation.RestController;
import org.xiaoyu.micro.exception.DataAccessException;

import java.util.List;
import java.util.Map;

@RestController
public class ApiController {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserService userService;

    @GetMapping("/api/user/{email}")
    Map<String, Boolean> userExist(@PathVariable("email") String email) {
        // check the email
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
        try {
            userService.getUser(email);
            return Map.of("result", Boolean.TRUE);
        } catch (DataAccessException e) {
            return Map.of("result", Boolean.FALSE);
        }
    }

    @GetMapping("/api/users")
    List<User> users() {
        return userService.getUsers();
    }
}
