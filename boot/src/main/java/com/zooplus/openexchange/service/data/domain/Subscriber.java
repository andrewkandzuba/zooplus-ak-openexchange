package com.zooplus.openexchange.service.data.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SUBSCRIBERS")
public class Subscriber implements Serializable {
    private static final long serialVersionUID = 4616356482743882423L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "NAME", unique = true)
    String name;

    @Column(name = "PASSWORD")
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
