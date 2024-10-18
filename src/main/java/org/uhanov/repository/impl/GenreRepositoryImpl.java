//package org.uhanov.repository.impl;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//import org.uhanov.model.Genre;
//import org.uhanov.repository.api.GenreRepository;
//import org.uhanov.repository.dao.GenreDao;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@Repository
//@RequiredArgsConstructor
//public class GenreRepositoryImpl implements GenreRepository {
//    private final GenreDao genreDao;
//
//    @Override
//    public Optional<Genre> findById(UUID id) {
//        return genreDao.findById(id);
//    }
//
//    @Override
//    public Genre save(Genre entity) {
//        return genreDao.save(entity);
//    }
//
//    @Override
//    public void deleteById(UUID id) {
//        genreDao.deleteById(id);
//    }
//
//    @Override
//    public Genre update(Genre e) {
//        return genreDao.update(e);
//    }
//
//    @Override
//    public List<Genre> findByStaffId(UUID uuid) {
//        return genreDao.findByStaffId(uuid);
//    }
//
//    @Override
//    public Optional<Genre> findByIdEager(UUID uuid) {
//        return genreDao.findByIdEager(uuid);
//    }
//
//    @Override
//    public List<Genre> findGenresByProductId(UUID productId) {
//        return genreDao.findGenresByProductId(productId);
//    }
//
//    @Override
//    public List<Genre> getGenresByIds(List<UUID> genreIds) {
//        return genreDao.getGenresByIds(genreIds);
//    }
//}
