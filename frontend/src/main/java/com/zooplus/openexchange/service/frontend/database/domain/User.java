package com.zooplus.openexchange.service.frontend.database.domain;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USERS")
public class User implements Serializable {
    private static final long serialVersionUID = -5277266119957501955L;

    public User() {
    }

    public User(String name, String password, String email, Set<Role> roles) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public User(String name, String password, String email) {
        this(name, password, email, Collections.emptySet());
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NAME", unique = true)
    @NotEmpty
    private String name;

    @Column(name = "PASSWORD")
    @NotEmpty
    @JsonIgnore
    private String password;

    @Column(name = "EMAIL")
    @NotEmpty
    private String email;

    @Generated(GenerationTime.INSERT)
    @Column(name =  "CREATEDAT", insertable=false)
    private Timestamp createdAt;

    @Generated(GenerationTime.INSERT)
    @Column(name =  "ENABLED", insertable=false)
    private Boolean enabled;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USERROLES", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
    private Set<Role> roles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
