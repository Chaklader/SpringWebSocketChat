package com.bee.wschat.domain;

import java.util.Date;

/**
 * Created by Chaklader on 7/24/17.
 */
public class ConversationData {

    private String conversationKey;
    private String conversationStarter;
    private String message;
    private String[] participants;
    private Date dateTime;

    public String getConversationKey() {
        return conversationKey;
    }

    public void setConversationKey(String conversationKey) {
        this.conversationKey = conversationKey;
    }

    public String getConversationStarter() {
        return conversationStarter;
    }

    public void setConversationStarter(String conversationStarter) {
        this.conversationStarter = conversationStarter;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getParticipants() {
        return participants;
    }

    public void setParticipants(String[] participants) {
        this.participants = participants;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
