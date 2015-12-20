package com.palvair.security;

import com.palvair.jpa.UserRepository;
import com.palvair.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by widdy on 20/12/2015.
 */
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public User loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println("loadByUsername");
        final User user = userRepo.findByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        System.out.println("user = " + user);
        return user;
    }
}
