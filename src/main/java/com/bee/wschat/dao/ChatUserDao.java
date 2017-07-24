package com.bee.wschat.dao;

import com.bee.wschat.entity.ChatUser;

import java.util.List;

/**
 * Created by Chaklader on 7/24/17.
 */
public interface ChatUserDao {

    ChatUser getById(int id);

    List<ChatUser> getAllUsers();

    ChatUser saveNewUser(ChatUser user);

    ChatUser getUserByLoginName(String loginName);

    ChatUser getUserByLoginNameAndPassword(String loginName, String password);
}

