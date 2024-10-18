//package org.uhanov.repository.impl;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//import org.uhanov.model.User;
//import org.uhanov.repository.api.UserRepository;
//import org.uhanov.repository.dao.UserDao;
//
//import java.util.Optional;
//import java.util.UUID;
//
//@Repository
//@RequiredArgsConstructor
//public class UserRepositoryImpl implements UserRepository {
//    private final UserDao userDao;
//
//    @Override
//    public Optional<User> findById(UUID id) {
//        return userDao.findById(id);
//    }
//
//    @Override
//    public User save(User entity) {
//        return userDao.save(entity);
//    }
//
//    @Override
//    public User update(User entity) {
//        return userDao.update(entity);
//    }
//
//    @Override
//    public void deleteById(UUID id) {
//        userDao.deleteById(id);
//    }
//
//}
