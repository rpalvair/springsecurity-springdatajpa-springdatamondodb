package com.palvair.security.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by widdy on 20/12/2015.
 */
@Table
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Connection {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;

    @Column
    @CreatedBy
    private User user;

    @Column
    @CreatedDate
    private Date createdDate;

}
