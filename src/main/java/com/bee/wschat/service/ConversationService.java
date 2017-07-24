package com.bee.wschat.service;

import com.bee.wschat.domain.ConversationData;
import com.bee.wschat.domain.TextMessage;
import com.bee.wschat.entity.Conversation;

/**
 * Created by Chaklader on 7/24/17.
 */
public interface ConversationService {

    Conversation getConversation(String conversationKey);

    void createConversation(ConversationData cData);

    void addMessageToConversation(Conversation conversation, TextMessage message);
}
