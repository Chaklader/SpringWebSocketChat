package com.bee.wschat.service;

import com.bee.wschat.dao.ChatMessageDao;
import com.bee.wschat.dao.ConversationDao;
import com.bee.wschat.dao.ParticipantDao;
import com.bee.wschat.domain.ConversationData;
import com.bee.wschat.domain.TextMessage;
import com.bee.wschat.entity.ChatMessage;
import com.bee.wschat.entity.Conversation;
import com.bee.wschat.entity.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Chaklader on 7/24/17.
 */
@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationDao conversationDao;

    @Autowired
    private ParticipantDao participantDao;

    @Autowired
    private ChatMessageDao chatMessageDao;

    @Override
    public Conversation getConversation(final String conversationKey) {
        return conversationDao.getConversation(conversationKey);
    }

    @Override
    public void createConversation(final ConversationData cData) {
        Conversation conversation = new Conversation();
        conversation.setConversationKey(cData.getConversationKey());
        List<Participant> participants = getParticipants(cData.getParticipants());
        conversation.setParticipants(participants);
        Participant starter = getParticipant(participants, cData.getConversationStarter());
        conversation.setConversationStarter(starter);

        ChatMessage chatMessage = createChatMessage();
        chatMessage.setConversation(conversation);
        chatMessage.setDateTime(cData.getDateTime());
        chatMessage.setBody(cData.getMessage());
        chatMessage.setSender(starter);

        chatMessageDao.saveChatMessage(chatMessage);
    }

    @Override
    public void addMessageToConversation(final Conversation conversation, final TextMessage message) {
        updateConversationParticipants(conversation, Arrays.asList(message.getParticipants().split(",")));
        ChatMessage chatMessage = createChatMessage();
        chatMessage.setConversation(conversation);
        chatMessage.setDateTime(message.getDateTime());
        chatMessage.setBody(message.getMessage());
        List<Participant> participants = conversation.getParticipants();
        Participant sender = getParticipant(participants, message.getUsername());
        chatMessage.setSender(sender);
        chatMessageDao.saveChatMessage(chatMessage);
    }

    protected void updateConversationParticipants(final Conversation conversation, List<String> participantNames) {
        List<Participant> participants = conversation.getParticipants();
        List<String> partToAdd = new ArrayList<>();
        for (String name : participantNames) {
            Participant participant = getParticipant(participants, name);
            if (participant == null) {
                partToAdd.add(name);
            }
        }
        if (!partToAdd.isEmpty()) {
            List<Participant> newParticipants = participantDao.getParticipants(partToAdd);
            participants.addAll(newParticipants);
            conversationDao.updateConversation(conversation);
        }
    }

    protected List<Participant> getParticipants(String[] names) {
        List<Participant> result = participantDao.getParticipants(Arrays.asList(names));
        return result;
    }

    protected Participant getParticipant(Collection<Participant> participants, String name) {
        for (Participant participant : participants) {
            if (name.equals(participant.getUsername())) {
                return participant;
            }
        }
        return null;
    }

    protected ChatMessage createChatMessage() {
        ChatMessage chatMessage = new ChatMessage();
        return chatMessage;
    }
}
