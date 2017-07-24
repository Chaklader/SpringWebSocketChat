package com.bee.wschat.service;

import com.bee.wschat.dao.ChatUserDao;
import com.bee.wschat.entity.ChatUser;
import com.bee.wschat.event.LoginEvent;
import com.bee.wschat.event.ParticipantInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ParticipantRepository {

	@Autowired
	private ChatUserDao chatUserDao;

	private Map<String, LoginEvent> activeSessions = new ConcurrentHashMap<>();

	private Map<String,ParticipantInfo> participants;

	@PostConstruct
	protected void init() {
		List<ChatUser> users = chatUserDao.getAllUsers();
		initParticipants(users);
	}

	public void loggedIn(String sessionId, LoginEvent event) {
		activeSessions.put(sessionId, event);

		Date eventTime = event.getTime();
		String userName = event.getUsername();
		doLoggedIn(userName, eventTime);
	}

	public void loggedOut(final String sessionId) {
		LoginEvent event = activeSessions.remove(sessionId);
		if (event != null) {
			String userName = event.getUsername();
			doLoggedOut(userName);
		}
	}

	public LoginEvent getParticipant(final String sessionId) {
		return activeSessions.get(sessionId);
	}

	public Collection<ParticipantInfo> getParticipants() {
		return participants.values();
	}

	protected void initParticipants(final List<ChatUser> users) {
		Map<String,ParticipantInfo> map = users.stream().
				map(this::createParticipant).
				collect(Collectors.toMap(ParticipantInfo::getUsername, p -> p));
		participants = new ConcurrentHashMap<>(map);
	}

	protected void doLoggedIn(final String userName, final Date loginTime) {
		ParticipantInfo pInfo = participants.get(userName);
		if (pInfo == null) {
			pInfo = createParticipant(userName, loginTime);
			participants.put(userName, pInfo);
		} else {
			pInfo.setLoginDatetime(loginTime);
			pInfo.setOnline(true);
		}
	}

	protected void doLoggedOut(final String userName) {
		ParticipantInfo pInfo = participants.get(userName);
		if (pInfo != null) {
			pInfo.setOnline(false);;
		}
	}

	protected ParticipantInfo createParticipant(final String userName, final Date loginTime) {
		ParticipantInfo pIfo = new ParticipantInfo(userName);
		pIfo.setLoginDatetime(loginTime);
		pIfo.setOnline(true);
		return pIfo;
	}

	protected ParticipantInfo createParticipant(final ChatUser user) {
		ParticipantInfo pInfo = new ParticipantInfo(user.getName());
		return pInfo;
	}
}
