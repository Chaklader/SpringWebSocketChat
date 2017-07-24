package com.bee.wschat.dao;

import com.bee.wschat.entity.Conversation;

/**
 * Created by Chaklader on 7/24/17.
 */
public interface ConversationDao {

    Conversation getConversation(String conversationKey);

    void updateConversation(Conversation conversation);
}
