package com.palvair.controller;

import com.palvair.jpa.UserRepository;
import com.palvair.security.model.User;
import com.palvair.security.model.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by widdy on 20/12/2015.
 */
@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/api/users/current", method = RequestMethod.GET)
    public User getCurrent() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UserAuthentication) {
            return ((UserAuthentication) authentication).getDetails();
        }
        return new User(authentication.getName()); //anonymous user support
    }

    @RequestMapping(value = "/admin/api/users", method = RequestMethod.GET)
    public List<User> list() {
        return userRepository.findAll();
    }
}
