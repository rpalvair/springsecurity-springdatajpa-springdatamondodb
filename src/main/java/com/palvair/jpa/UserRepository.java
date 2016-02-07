package com.palvair.jpa;

import com.palvair.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by widdy on 20/12/2015.
 */
public interface UserRepository extends JpaRepository<User, Long>, UserCustomJpaRepository {
    User findByUsername(String username);
}
