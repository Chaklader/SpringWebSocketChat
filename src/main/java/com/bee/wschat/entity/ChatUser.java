package com.bee.wschat.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Chaklader on 7/24/17.
 */
@Entity
@Table(name = "users")
public class ChatUser {

    private long id;

    @NotNull
    @Size(min = 1, max = 20, message = "Name must be between 1 and 20 characters.")
    private String name;

    @NotNull
    @Size(min = 1, max = 40, message = "Password must be between 1 and 20 characters.")
    private String password;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return name;
    }
}
