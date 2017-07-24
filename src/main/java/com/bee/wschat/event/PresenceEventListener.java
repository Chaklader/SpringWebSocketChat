package com.bee.wschat.event;

import java.util.Optional;

import com.bee.wschat.config.ChatConfig;
import com.bee.wschat.service.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Listener to track user presence. 
 * Sends notifications to the login destination when a connected event is received
 * and notifications to the logout destination when a disconnect event is received
 * 
 * @author Sergi Almar
 */
@Component
public class PresenceEventListener {

    public static class Destinations {
        private Destinations() {
        }

        private static final String LOGIN = "/topic/chat.login";
        private static final String LOGOUT = "/topic/chat.logout";
    }

    @Autowired
	private ParticipantRepository participantRepository;

    @Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	private String loginDestination = Destinations.LOGIN;
	
	private String logoutDestination = Destinations.LOGOUT;
	
//	public PresenceEventListener(SimpMessagingTemplate messagingTemplate, ParticipantRepository participantRepository) {
//		this.messagingTemplate = messagingTemplate;
//		this.participantRepository = participantRepository;
//	}
		
	@EventListener
	private void handleSessionConnected(SessionConnectEvent event) {
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
		String username = headers.getUser().getName();

		LoginEvent loginEvent = new LoginEvent(username);
		messagingTemplate.convertAndSend(loginDestination, loginEvent);
		
		// We store the session as we need to be idempotent in the disconnect event processing
		participantRepository.loggedIn(headers.getSessionId(), loginEvent);
	}
	
	@EventListener
	private void handleSessionDisconnect(SessionDisconnectEvent event) {
		
		Optional.ofNullable(participantRepository.getParticipant(event.getSessionId()))
				.ifPresent(login -> {
					messagingTemplate.convertAndSend(logoutDestination, new LogoutEvent(login.getUsername()));
					participantRepository.loggedOut(event.getSessionId());
				});
	}

	public void setLoginDestination(String loginDestination) {
		this.loginDestination = loginDestination;
	}

	public void setLogoutDestination(String logoutDestination) {
		this.logoutDestination = logoutDestination;
	}
}
