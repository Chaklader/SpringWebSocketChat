package com.bee.wschat.dao;

import com.bee.wschat.entity.Conversation;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

/**
 * Created by Chaklader on 7/24/17.
 */
@Repository
public class ConversationDaoHibernate extends HibernateDaoSupport implements ConversationDao {

    @Autowired
    private SessionFactory sessionFactory;

    @PostConstruct
    protected void init() {
        setSessionFactory(sessionFactory);
    }

    @Override
    public Conversation getConversation(String conversationKey) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Conversation> query = session.createQuery("from Conversation where conversationKey = :key");
            query.setParameter("key", conversationKey);
            Conversation result = query.getSingleResult();
            return result;
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (NoResultException e) {
            // OK
        }
        return null;
    }

    @Override
    public void updateConversation(Conversation conversation) {
        Transaction tx = null;
        try (Session session = getSessionFactory().openSession()){
            tx = session.beginTransaction();
            session.update(conversation);
            tx.commit();
        } catch (PersistenceException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }
}
