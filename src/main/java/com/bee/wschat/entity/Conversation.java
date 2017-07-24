package com.bee.wschat.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by Chaklader on 7/24/17.
 */
@Entity
@Table(name = "conversations")
public class Conversation {

    private Long id;
    private String conversationKey;
    private List<Participant> participants;
    private Participant conversationStarter;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "conversation_key")
    public String getConversationKey() {
        return conversationKey;
    }

    public void setConversationKey(String conversationKey) {
        this.conversationKey = conversationKey;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "participants", joinColumns = {
            @JoinColumn(name = "conversation_id", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "user_id", nullable = false, updatable = false) })
    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "conversation_starter_id", nullable = false)
    public Participant getConversationStarter() {
        return conversationStarter;
    }

    public void setConversationStarter(Participant conversationStarter) {
        this.conversationStarter = conversationStarter;
    }
}
