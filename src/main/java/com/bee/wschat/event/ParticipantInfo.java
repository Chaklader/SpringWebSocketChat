package com.bee.wschat.event;

import java.util.Date;

/**
 * 
 * @author Sergi Almar
 */
public class ParticipantInfo {

	private String username;
	private boolean online;
	private Date loginDatetime;

	public ParticipantInfo(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public Date getLoginDatetime() {
		return loginDatetime;
	}

	public void setLoginDatetime(Date time) {
		this.loginDatetime = time;
	}
}
