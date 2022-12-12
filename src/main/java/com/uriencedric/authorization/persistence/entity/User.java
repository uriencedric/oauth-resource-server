package com.uriencedric.authorization.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(exclude = "authorities")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String userType;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @Column(columnDefinition = "tinyint(1) default 0")
    private boolean active;
    @Column(columnDefinition = "tinyint(1) default 0")
    private boolean locked;
    @Column(columnDefinition = "tinyint(1) default 0")
    private boolean deleted;
    private Date createdAt;
    private Date updatedAt;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_authorities", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    @Builder.Default
    @JsonIgnoreProperties(value = "users")
    private Set<Authority> authorities = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}