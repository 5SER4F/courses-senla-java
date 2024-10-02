package org.uhanov.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.uhanov.model.Creator;
import org.uhanov.repository.api.CreatorRepository;
import org.uhanov.repository.dao.CreatorDao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CreatorRepositoryImp implements CreatorRepository {
    private final CreatorDao creatorDao;

    @Override
    public Optional<Creator> findById(UUID id) {
        return creatorDao.findById(id);
    }

    @Override
    public Creator save(Creator entity) {
        return creatorDao.save(entity);
    }

    @Override
    public void deleteById(UUID id) {
        creatorDao.deleteById(id);
    }

    @Override
    public Creator update(Creator e) {
        return creatorDao.update(e);
    }

    @Override
    public List<Creator> findByName(String nameSubString) {
        return creatorDao.findByName(nameSubString);
    }

    @Override
    public Optional<Creator> findByIdEager(UUID uuid) {
        return creatorDao.findByIdEager(uuid);
    }
}
