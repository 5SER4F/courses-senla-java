//package org.uhanov.repository.dao;
//
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Repository;
//import org.uhanov.model.*;
//
//import javax.persistence.criteria.*;
//import java.util.Optional;
//import java.util.UUID;
//
//@Repository
//@AllArgsConstructor
//public class ProductDao extends AbstractDao<UUID, Product> {
//
//    public Optional<Product> findByIdEager(UUID uuid) {
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(getEntityClass());
//        Root<Product> root = criteriaQuery.from(getEntityClass());
//
//        Fetch<Product, AgeRating> ageRatingFetch = root.fetch(Product_.ageRating, JoinType.INNER);
//        Fetch<Product, Genre> genreFetch = root.fetch(Product_.genres, JoinType.INNER);
//        Fetch<Product, Creator> creatorFetch = root.fetch(Product_.creator, JoinType.INNER);
//
//        criteriaQuery.where(
//                criteriaBuilder.equal(root.get(Product_.ID), uuid)
//        );
//
//        return entityManager.createQuery(criteriaQuery)
//                .getResultList()
//                .stream()
//                .findFirst();
//    }
//
//    @Override
//    protected Class<Product> getEntityClass() {
//        return Product.class;
//    }
//}
