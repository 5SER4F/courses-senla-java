//package org.uhanov.repository.impl;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//import org.uhanov.model.AgeRating;
//import org.uhanov.repository.api.AgeRatingRepository;
//import org.uhanov.repository.dao.AgeRatingDao;
//
//import java.util.Optional;
//import java.util.UUID;
//
//@Repository
//@RequiredArgsConstructor
//public class AgeRatingRepositoryImpl implements AgeRatingRepository {
//    private final AgeRatingDao ageRatingDao;
//
//    @Override
//    public Optional<AgeRating> findById(UUID id) {
//        return ageRatingDao.findById(id);
//    }
//
//    @Override
//    public AgeRating save(AgeRating entity) {
//        return ageRatingDao.save(entity);
//    }
//
//    @Override
//    public void deleteById(UUID id) {
//        ageRatingDao.deleteById(id);
//    }
//
//    @Override
//    public AgeRating update(AgeRating e) {
//        return ageRatingDao.update(e);
//    }
//
//    @Override
//    public Optional<AgeRating> findByIdEager(UUID uuid) {
//        return ageRatingDao.findByIdEager(uuid);
//    }
//}
