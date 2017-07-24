package com.bee.wschat.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by Chaklader on 7/24/17.
 */
@Entity
@Table(name = "messages")
public class ChatMessage {
    private long id;
    private String body;
    private Participant sender;
    private Date dateTime;
    private Conversation conversation;
//    private Set<Conversation> newestMessageConversations;

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
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    public Participant getSender() {
        return sender;
    }

    public void setSender(Participant sender) {
        this.sender = sender;
    }

    @Column(name = "date_time")
    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "conversation_id", nullable = false)
    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "newestMessage", cascade = CascadeType.ALL)
//    public Set<Conversation> getNewestMessageConversations() {
//        return newestMessageConversations;
//    }
//
//    public void setNewestMessageConversations(Set<Conversation> newestMessageConversations) {
//        this.newestMessageConversations = newestMessageConversations;
//    }
}
