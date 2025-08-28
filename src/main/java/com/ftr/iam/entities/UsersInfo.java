package com.ftr.iam.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users_info")
public class UsersInfo implements Serializable {
    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String salt;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private Date createdDate;

    @Column(nullable = false)
    private String updatedBy;

    @Column(nullable = false)
    private Date updatedDate;
}
