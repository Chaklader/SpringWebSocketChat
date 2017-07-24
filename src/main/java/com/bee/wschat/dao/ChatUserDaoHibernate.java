package com.bee.wschat.dao;

import com.bee.wschat.entity.ChatUser;
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
import java.util.List;

/**
 * Created by Chaklader on 7/24/17.
 */
@Repository
public class ChatUserDaoHibernate extends HibernateDaoSupport implements ChatUserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @PostConstruct
    protected void init() {
        setSessionFactory(sessionFactory);
    }

    @Override
    public ChatUser saveNewUser(final ChatUser user) {
        Transaction tx = null;
        try (Session session = getSessionFactory().openSession()){
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
        } catch (PersistenceException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
        return user;
    }

    @Override
    public ChatUser getUserByLoginNameAndPassword(final String loginName, final String password) {
        try (Session session = getSessionFactory().openSession()) {
            Query<ChatUser> query = session.createQuery(
                    "from ChatUser where name = :name and password = :password");
            query.setParameter("name", loginName);
            query.setParameter("password", password);
            ChatUser result = query.getSingleResult();
            return result;
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (NoResultException e) {
            // OK
        }
        return null;
    }

    @Override
    public ChatUser getUserByLoginName(final String loginName) {
        try (Session session = getSessionFactory().openSession()) {
            Query<ChatUser> query = session.createQuery("from ChatUser where name = :name");
            query.setParameter("name", loginName);
            ChatUser result = query.getSingleResult();
            return result;
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (NoResultException e) {
            // OK
        }
        return null;
    }

    @Override
    public List<ChatUser> getAllUsers() {
        try (Session session = getSessionFactory().openSession()) {
            List<ChatUser> userList = session.createQuery("from ChatUser").list();
            return userList;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ChatUser getById(int id) {
        try (Session session = getSessionFactory().openSession()){
            ChatUser user = session.get(ChatUser.class, id);
            return user;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }
}

