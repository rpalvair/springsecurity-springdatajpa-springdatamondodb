package com.palvair.jpa;

import com.palvair.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by widdy on 07/02/2016.
 */
class UserRepositoryImpl implements UserCustomJpaRepository {

    @Autowired
    private UserRepository repository;

    @Override
    public void saveEntity(User entity) {
        repository.save(entity);
    }
}
