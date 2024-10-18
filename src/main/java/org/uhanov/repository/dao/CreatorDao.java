//package org.uhanov.repository.dao;
//
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Repository;
//import org.uhanov.model.Creator;
//import org.uhanov.model.Creator_;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@Repository
//@AllArgsConstructor
//public class CreatorDao extends AbstractDao<UUID, Creator> {
//
//    public List<Creator> findByName(String nameSubString) {
//        String nameParam = "nameSubString";
//        String jpqlQuery = " SELECT c FROM " + Creator.class.getSimpleName() + " c " +
//                "WHERE c." + Creator_.NAME + " LIKE :" + nameParam;
//        return entityManager.createQuery(jpqlQuery, getEntityClass())
//                .setParameter(nameParam, nameSubString)
//                .getResultList();
//    }
//
//    public Optional<Creator> findByIdEager(UUID uuid) {
//        String paramId = "creatorId";
//        String jpqlQuery = " SELECT c " +
//                " FROM " + Creator.class.getSimpleName() + " c" +
//                " JOIN FETCH c." + Creator_.PRODUCTS +
//                " WHERE c." + Creator_.ID + "=:" + paramId;
//
//        return Optional.ofNullable(entityManager.createQuery(jpqlQuery, getEntityClass())
//                .setParameter(paramId, uuid)
//                .getSingleResult()
//        );
//    }
//
//    @Override
//    protected Class<Creator> getEntityClass() {
//        return Creator.class;
//    }
//}
