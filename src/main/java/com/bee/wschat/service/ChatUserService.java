package com.bee.wschat.service;

import com.bee.wschat.entity.ChatUser;

/**
 * Created by Chaklader on 7/24/17.
 */
public interface ChatUserService {

    ChatUser getUserByLoginName(String loginName);

    ChatUser registerNewUser(String userName, String password);

}
