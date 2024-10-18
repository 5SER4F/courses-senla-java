//package org.uhanov.repository.dao;
//
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//import org.uhanov.exception.ResourceNotFoundException;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import java.io.Serializable;
//import java.util.Optional;
//
//@Repository
//@Transactional
//public abstract class AbstractDao<PK extends Serializable, T> {
//
//    @PersistenceContext
//    protected EntityManager entityManager;
//
//    public Optional<T> findById(PK id) {
//        return Optional.ofNullable(entityManager.find(getEntityClass(), id));
//    }
//
//    public T save(T entity) {
//        entityManager.persist(entity);
//        return entity;
//    }
//
//    public T update(T e) {
//        entityManager.merge(e);
//        return e;
//    }
//
//    public void delete(T entity) {
//        entityManager.remove(entity);
//    }
//
//    public void deleteById(PK id) {
//        T entity = findById(id).orElseThrow(
//                ResourceNotFoundException::new
//        );
//        if (entity != null) {
//            delete(entity);
//        }
//    }
//
//    protected abstract Class<T> getEntityClass();
//}