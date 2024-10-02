package org.uhanov.repository.dao;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.uhanov.model.AgeRating;
import org.uhanov.model.AgeRating_;
import org.uhanov.model.Staff;

import javax.persistence.criteria.*;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class AgeRatingDao extends AbstractDao<UUID, AgeRating> {

    public Optional<AgeRating> findByIdEager(UUID uuid) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AgeRating> criteriaQuery = criteriaBuilder.createQuery(getEntityClass());
        Root<AgeRating> root = criteriaQuery.from(getEntityClass());

        Fetch<AgeRating, Staff> staffFetch = root.fetch(AgeRating_.lastChanger, JoinType.INNER);

        criteriaQuery.where(
                criteriaBuilder.equal(root.get(AgeRating_.ID), uuid)
        );

        return Optional.ofNullable(entityManager.createQuery(criteriaQuery)
                .getSingleResult()
        );
    }

    @Override
    protected Class<AgeRating> getEntityClass() {
        return AgeRating.class;
    }
}
