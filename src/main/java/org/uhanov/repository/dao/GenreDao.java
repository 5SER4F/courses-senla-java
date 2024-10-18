//package org.uhanov.repository.dao;
//
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Repository;
//import org.uhanov.model.Genre;
//import org.uhanov.model.Genre_;
//import org.uhanov.model.Staff_;
//
//import javax.persistence.EntityGraph;
//import javax.persistence.criteria.CriteriaQuery;
//import java.util.*;
//
//@Repository
//@AllArgsConstructor
//public class GenreDao extends AbstractDao<UUID, Genre> {
//
//    public List<Genre> findByStaffId(UUID staffId) {
//        String paramId = "staffId";
//        String jpqlQuery = " SELECT g FROM Genre g " +
//                " WHERE g." + Genre_.LAST_CHANGER +
//                "." + Staff_.ID +
//                " = :" + paramId;
//
//        return entityManager.createQuery(jpqlQuery, getEntityClass())
//                .setParameter(paramId, staffId)
//                .getResultList();
//    }
//
//    public List<Genre> findGenresByProductId(UUID productId) {
//        String paramId = "productId";
//        String jpqlQuery = "SELECT g FROM Genre g " +
//                "JOIN g.products p WHERE p.id = :" + paramId;
//        return entityManager.createQuery(jpqlQuery, Genre.class)
//                .setParameter(paramId, productId)
//                .getResultList();
//    }
//
//    public List<Genre> getGenresByIds(List<UUID> genreIds) {
//        if (genreIds.isEmpty()) {
//            return Collections.emptyList();
//        }
//
//        if (genreIds.isEmpty()) {
//            return Collections.emptyList();
//        }
//        CriteriaQuery<Genre> criteriaQuery = entityManager.getCriteriaBuilder()
//                .createQuery(Genre.class);
//        criteriaQuery.where(
//                criteriaQuery.from(Genre.class)
//                        .get(Genre_.ID)
//                        .in(genreIds)
//        );
//
//        return entityManager.createQuery(
//                criteriaQuery
//        ).getResultList();
//
//    }
//
//    public Optional<Genre> findByIdEager(UUID uuid) {
//        EntityGraph<Genre> entityGraph = entityManager.createEntityGraph(getEntityClass());
//        entityGraph.addSubgraph(Genre_.LAST_CHANGER);
//        return Optional.ofNullable(
//                entityManager.find(
//                        getEntityClass(),
//                        uuid,
//                        Map.of("javax.persistence.loadgraph", entityGraph)
//
//                )
//        );
//
//    }
//
//    @Override
//    protected Class<Genre> getEntityClass() {
//        return Genre.class;
//    }
//}
