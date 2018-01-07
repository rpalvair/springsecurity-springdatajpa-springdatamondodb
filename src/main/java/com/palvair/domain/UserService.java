package com.palvair.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.palvair.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class UserService {

    private final ObjectMapper objectMapper;

    @Autowired
    public UserService(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public User fromJSON(final byte[] userBytes) {
        try {
            return objectMapper.readValue(new ByteArrayInputStream(userBytes), User.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public byte[] toJSON(final User user) {
        try {
            return objectMapper.writeValueAsBytes(user);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
