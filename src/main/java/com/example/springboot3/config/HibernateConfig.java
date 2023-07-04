package com.example.springboot3.config;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {


    @Autowired
    private EntityManagerFactory entityManagerFactory;


//    @Bean
//    public SessionFactory sessionFactory(){
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        criteriaBuilder.
//        return  entityManagerFactory.unwrap(SessionFactory.class);
//    }
    @Bean
    public CriteriaBuilder criteriaBuilder(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.getCriteriaBuilder();
    }

//    @Bean
//    public HibernateCriteriaBuilder setEntityManagerFactory(){
//        Session session = sessionFactory().getCurrentSession();
//        return session.getCriteriaBuilder();
//    }


}
