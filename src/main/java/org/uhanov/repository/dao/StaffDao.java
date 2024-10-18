//package org.uhanov.repository.dao;
//
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Repository;
//import org.uhanov.model.AgeRating;
//import org.uhanov.model.Genre;
//import org.uhanov.model.Staff;
//import org.uhanov.model.Staff_;
//
//import javax.persistence.EntityGraph;
//import javax.persistence.Subgraph;
//import java.util.Map;
//import java.util.Optional;
//import java.util.UUID;
//
//@Repository
//@AllArgsConstructor
//public class StaffDao extends AbstractDao<UUID, Staff> {
//
//    public Optional<Staff> findStaffByIdEager(UUID uuid) {
//        String paramId = "uuid";
//        EntityGraph<Staff> entityGraph = entityManager.createEntityGraph(getEntityClass());
//        Subgraph<AgeRating> ageRatingsSubGraph = entityGraph.addSubgraph(Staff_.AGE_RATINGS_ADDED_BY);
//        Subgraph<Genre> genreSubgraph = entityGraph.addSubgraph(Staff_.GENRES_ADDED_BY);
//        return Optional.ofNullable(
//                entityManager.find(
//                        getEntityClass(),
//                        uuid,
//                        Map.of("javax.persistence.loadgraph", entityGraph)
//                )
//        );
//    }
//
//    @Override
//    protected Class<Staff> getEntityClass() {
//        return Staff.class;
//    }
//}
