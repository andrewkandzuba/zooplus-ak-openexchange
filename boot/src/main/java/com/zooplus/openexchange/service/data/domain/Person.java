package com.zooplus.openexchange.service.data.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "persons")
public class Person implements Serializable {
    private static final long serialVersionUID = -8735487642834331320L;

    @Id
    @Column(name = "id")
    @GeneratedValue
    Long id;

    @Column(name = "name", unique=true)
    String name;

    @Column(name = "password")
    String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
