package org.uhanov.repository.dao;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.uhanov.model.Genre;
import org.uhanov.model.Genre_;
import org.uhanov.model.Staff_;

import javax.persistence.EntityGraph;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class GenreDao extends AbstractDao<UUID, Genre> {

    public List<Genre> findByStaffId(UUID uuid) {
        String paramId = "id";
        String jpqlQuery = " SELECT g FROM Genre g " +
                " WHERE g." + Genre_.LAST_CHANGER +
                "." + Staff_.ID +
                " = :" + paramId;

        return entityManager.createQuery(jpqlQuery, getEntityClass())
                .setParameter(paramId, uuid)
                .getResultList();
    }

    public Optional<Genre> findByIdEager(UUID uuid) {
        EntityGraph<Genre> entityGraph = entityManager.createEntityGraph(getEntityClass());
        entityGraph.addSubgraph(Genre_.LAST_CHANGER);
        return Optional.ofNullable(
                entityManager.find(
                        getEntityClass(),
                        uuid,
                        Map.of("javax.persistence.loadgraph", entityGraph)

                )
        );

    }

    @Override
    protected Class<Genre> getEntityClass() {
        return Genre.class;
    }
}
