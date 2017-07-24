package com.bee.wschat.domain;

import java.util.Date;

/**
 * 
 * @author Sergi Almar
 */
public class TextMessage {

	private String conversationKey;
	private String username;
	private String message;
	private String fileName;
	private String participants;
	private Date dateTime = new Date();
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMessage() {
		return message;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getConversationKey() {
		return conversationKey;
	}

	public void setConversationKey(String conversationKey) {
		this.conversationKey = conversationKey;
	}

	public String getParticipants() {
		return participants;
	}

	public void setParticipants(String participants) {
		this.participants = participants;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public String toString() {
		return "ChatMessage [user:" + username + ", message:" + message + ", to:" + participants + "]";
	}
}
