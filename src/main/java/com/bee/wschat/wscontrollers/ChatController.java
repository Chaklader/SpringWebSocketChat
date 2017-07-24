package com.bee.wschat.wscontrollers;

import java.security.Principal;
import java.util.Collection;

import com.bee.wschat.domain.ConversationData;
import com.bee.wschat.entity.Conversation;
import com.bee.wschat.event.ParticipantInfo;
import com.bee.wschat.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.bee.wschat.domain.TextMessage;
import com.bee.wschat.domain.SessionProfanity;
import com.bee.wschat.service.ParticipantRepository;
import com.bee.wschat.exception.TooMuchProfanityException;
import com.bee.wschat.util.ProfanityChecker;

;

/**
 * Controller that handles WebSocket chat messages
 * 
 * @author Sergi Almar
 */
@Controller
public class ChatController {

	@Autowired private ProfanityChecker profanityFilter;
	
	@Autowired private SessionProfanity profanity;
	
	@Autowired private ParticipantRepository participantRepository;
	
	@Autowired private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired private ConversationService conversationService;

	@SubscribeMapping("/chat.participants")
	public Collection<ParticipantInfo> retrieveParticipants() {
		return participantRepository.getParticipants();
	}
	
	@MessageMapping("/chat.message")
	public TextMessage filterMessage(@Payload TextMessage message, Principal principal) {
		checkProfanityAndSanitize(message);
		
		message.setUsername(principal.getName());
		
		return message;
	}
	
	@MessageMapping("/chat.private.{participants}.{conversationKey}")
	public void filterPrivateMessage(@Payload TextMessage message,
									 @DestinationVariable("participants") String participants,
									 @DestinationVariable("conversationKey") String conversationKey,
									 Principal principal) {
		checkProfanityAndSanitize(message);
		message.setUsername(principal.getName());
		message.setConversationKey(conversationKey);
		message.setParticipants(participants);
		sendPrivateMessage(participants.split(","), message, principal);
	}

	protected void sendPrivateMessage(final String[] userNames, final TextMessage message, final Principal principal) {
		String sender = principal.getName();
		for (String userName : userNames) {
			if (sender.equals(userName)) {
				continue;
			}
			simpMessagingTemplate.convertAndSend("/user/" + userName + "/exchange/amq.direct/chat.message", message);
		}

		String conversationKey = message.getConversationKey();
		Conversation conversation = conversationService.getConversation(conversationKey);
		if (conversation == null) {
			ConversationData cData = new ConversationData();
			cData.setConversationKey(conversationKey);
			cData.setConversationStarter(principal.getName());
			cData.setDateTime(message.getDateTime());
			cData.setMessage(message.getMessage());
			cData.setParticipants(userNames);
			conversationService.createConversation(cData);
		} else {
			conversationService.addMessageToConversation(conversation, message);
		}
	}
	
	private void checkProfanityAndSanitize(TextMessage message) {
		long profanityLevel = profanityFilter.getMessageProfanity(message.getMessage());
		profanity.increment(profanityLevel);
		message.setMessage(profanityFilter.filter(message.getMessage()));
	}
	
	@MessageExceptionHandler
	@SendToUser(value = "/exchange/amq.direct/errors", broadcast = false)
	public String handleProfanity(TooMuchProfanityException e) {
		return e.getMessage();
	}
}