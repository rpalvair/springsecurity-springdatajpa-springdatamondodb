package com.palvair.jpa;

import com.palvair.security.model.User;

/**
 * Created by widdy on 07/02/2016.
 */
public interface UserCustomJpaRepository {
    void saveEntity(User entity);
}
