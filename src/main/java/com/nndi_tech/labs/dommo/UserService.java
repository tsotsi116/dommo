package com.nndi_tech.labs.dommo;

import com.activepersistence.service.Base;
import com.nndi_tech.labs.dommo.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

public class UserService extends AbstractService<User> {

    @PersistenceContext
    private final EntityManagerFactory entityManagerFactory;

    public UserService(EntityManagerFactory entityManagerFactory) {
        super(User.class);
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
