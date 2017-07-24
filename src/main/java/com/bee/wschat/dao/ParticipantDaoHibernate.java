package com.bee.wschat.dao;

import com.bee.wschat.entity.Participant;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Chaklader on 7/24/17.
 */
@Repository
public class ParticipantDaoHibernate extends HibernateDaoSupport implements ParticipantDao {

    @Autowired
    private SessionFactory sessionFactory;

    @PostConstruct
    protected void init() {
        setSessionFactory(sessionFactory);
    }

    @Override
    public List<Participant> getParticipants(List<String> names) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Participant> query = session.createQuery("from Participant where name in :names");
            query.setParameter("names", names);
            List<Participant> result = query.list();
            return result;
        } catch (PersistenceException e) {
            return Collections.emptyList();
        }

    }
}
