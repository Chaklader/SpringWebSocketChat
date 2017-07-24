package com.bee.wschat.entity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Chaklader on 7/24/17.
 */
@Entity
@Table(name = "users")
public class Participant {

    private long id;
    private String username;
    private Set<Conversation> conversations;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy  = "participants")
    public Set<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(Set<Conversation> conversations) {
        this.conversations = conversations;
    }
}
