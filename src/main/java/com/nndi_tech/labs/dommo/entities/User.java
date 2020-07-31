package com.nndi_tech.labs.dommo.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column(name="display_name")
    private String displayName;

    @Column
    private String email;

    @Column
    private String phone;

    @Column(name = "user_type")
    private String userType;

    @Column(name="is_activated")
    private boolean activated;

    @Column(name="last_login_at")
    private LocalDateTime lastLogin;

    @Column(name="created")
    private LocalDateTime createdAt;

    @Column(name="modified")
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deleted;
}
