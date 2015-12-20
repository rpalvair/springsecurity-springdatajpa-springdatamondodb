package com.palvair.security.model;

import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

/**
 * Created by widdy on 20/12/2015.
 */
@Table
@Entity
@Data
public class Connection {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;

    private String zipCode;

    @CreatedBy
    private User user;

    @CreatedDate
    private DateTime createdDate;
}
