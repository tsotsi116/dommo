package com.nndi_tech.labs.dommo;

import com.activepersistence.service.Base;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Objects;

public abstract class AbstractService<T> extends Base<T> {
    public AbstractService(Class<T> entityClass) {
        super(entityClass);
    }

    public T save(T entity) {
        Objects.requireNonNull(entity);
        return withEntityManager(em -> {
            EntityTransaction txn = em.getTransaction();
            try {
                txn.begin();
                em.persist(entity);
                em.flush();
                txn.commit();
                return entity;
            } catch(PersistenceException e) {
                LoggerFactory.getLogger(getClass()).error("Failed to save entity", e);
                txn.rollback();
                throw e;
            } finally {
                txn = null;
            }
        });
    }

    public T update(T entity) {
        Objects.requireNonNull(entity);
        return withEntityManager(em -> {
            EntityTransaction txn = em.getTransaction();
            try {
                txn.begin();
                em.merge(entity);
                em.flush();
                txn.commit();
            } catch(PersistenceException e) {
                LoggerFactory.getLogger(getClass()).error("Failed to update entity", e);
                txn.rollback();
                throw e;
            } finally {
                txn = null;
            }
            return entity;
        });
    }

    public void delete(T entity) {
        Objects.requireNonNull(entity);
        withEntityManager(em -> {
            EntityTransaction txn = em.getTransaction();
            try {
                txn.begin();
                em.detach(entity);
                em.flush();
                txn.commit();
            } catch(PersistenceException e) {
                LoggerFactory.getLogger(getClass()).error("Failed to delete entity", e);
                txn.rollback();
                throw e;
            } finally {
                txn = null;
            }
            return entity;
        });
    }

    public <R> R withEntityManager(java.util.function.Function<EntityManager, R> consumer) {
        EntityManager em = getEntityManager();
        R res = consumer.apply(em);
        em.close();
        return res;
    }

    public long count(Class<?> clazz) {
        return withEntityManager(em -> {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
            countQuery.select(
                criteriaBuilder.count(
                    countQuery.from(clazz)));
            Long count = em.createQuery(countQuery).getSingleResult();
            return count;
        });
    }
}
