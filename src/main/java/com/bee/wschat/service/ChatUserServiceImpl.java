package com.bee.wschat.service;

import com.bee.wschat.dao.ChatUserDao;
import com.bee.wschat.entity.ChatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Chaklader on 7/24/17.
 */
@Service
public class ChatUserServiceImpl implements ChatUserService {

    @Autowired
    private ChatUserDao chatUserDao;

    @Override
    public ChatUser getUserByLoginName(String loginName) {
        return chatUserDao.getUserByLoginName(loginName);
    }

    @Override
    public ChatUser registerNewUser(final String userName, final String password) {
        ChatUser chatUser = new ChatUser();
        chatUser.setName(userName);
        chatUser.setPassword(password);
        return chatUserDao.saveNewUser(chatUser);
    }

}
