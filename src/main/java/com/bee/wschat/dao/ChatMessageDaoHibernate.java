package com.bee.wschat.dao;

import com.bee.wschat.entity.ChatMessage;
import com.bee.wschat.entity.Conversation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.PersistenceException;

/**
 * Created by Chaklader on 7/24/17.
 */
@Repository
public class ChatMessageDaoHibernate extends HibernateDaoSupport implements ChatMessageDao {

    @Autowired
    private SessionFactory sessionFactory;

    @PostConstruct
    protected void init() {
        setSessionFactory(sessionFactory);
    }

    @Override
    public void saveChatMessage(ChatMessage chatMessage) {
        Transaction tx = null;
        try (Session session = getSessionFactory().openSession()){
            tx = session.beginTransaction();
            Conversation conversation = chatMessage.getConversation();
            if (conversation.getId() == null) {
                session.persist(conversation);
            } else {
                session.refresh(conversation);
            }
            session.persist(chatMessage);
            tx.commit();
        } catch (PersistenceException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }
}
