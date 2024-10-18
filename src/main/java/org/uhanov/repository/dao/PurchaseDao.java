//package org.uhanov.repository.dao;
//
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Repository;
//import org.uhanov.model.*;
//
//import javax.persistence.EntityNotFoundException;
//import javax.persistence.criteria.*;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@Repository
//@AllArgsConstructor
//public class PurchaseDao extends AbstractDao<UUID, Purchase> {
//
//    public List<Purchase> findByPurchaseDateInPeriod(LocalDateTime left, LocalDateTime right) {
//        if (left.isAfter(right)) {
//            throw new EntityNotFoundException("Left border must be less than right" +
//                    "left=" + left + "right=" + right);
//        }
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Purchase> criteriaQuery = criteriaBuilder.createQuery(Purchase.class);
//        Root<Purchase> root = criteriaQuery.from(Purchase.class);
//
//        Predicate leftPredicate = criteriaBuilder.greaterThanOrEqualTo(
//                root.get(Purchase_.purchaseDate), left
//        );
//        Predicate rightPredicate = criteriaBuilder.lessThanOrEqualTo(
//                root.get(Purchase_.purchaseDate), right
//        );
//
//        criteriaQuery.where(
//                criteriaBuilder.and(leftPredicate, rightPredicate)
//        );
//
//        return entityManager.createQuery(criteriaQuery).getResultList();
//    }
//
//    public List<Purchase> findByProductName(String name) {
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Purchase> criteriaQuery = criteriaBuilder.createQuery(Purchase.class);
//        Root<Purchase> root = criteriaQuery.from(Purchase.class);
//
//        Fetch<Purchase, Product> joinProduct = root.fetch(Purchase_.product, JoinType.INNER);
//        Fetch<Purchase, User> joinBuyer = root.fetch(Purchase_.buyer, JoinType.INNER);
//
//        criteriaQuery.where(
//                criteriaBuilder.equal(
//                        root.get(Purchase_.product).get(Product_.NAME),
//                        name
//                )
//        );
//
//        return entityManager.createQuery(criteriaQuery).getResultList();
//    }
//
//    public Optional<Purchase> findByIdEager(UUID uuid) {
//        String paramId = "uuid";
//        String jpqlQuery = " SELECT p " +
//                " FROM " + Purchase.class.getSimpleName() + " p " +
//                " JOIN FETCH p." + Purchase_.BUYER +
//                " JOIN FETCH p." + Purchase_.PRODUCT +
//                " WHERE p." + Purchase_.ID + "=:" + paramId;
//        return Optional.ofNullable(
//                entityManager.createQuery(jpqlQuery, getEntityClass())
//                        .setParameter(paramId, uuid)
//                        .getSingleResult()
//        );
//    }
//
//    @Override
//    protected Class<Purchase> getEntityClass() {
//        return Purchase.class;
//    }
//
//}
