//package org.uhanov.repository.impl;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//import org.uhanov.model.Purchase;
//import org.uhanov.repository.api.PurchaseRepository;
//import org.uhanov.repository.dao.PurchaseDao;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@Repository
//@RequiredArgsConstructor
//public class PurchaseRepositoryImpl implements PurchaseRepository {
//    private final PurchaseDao purchaseDao;
//
//    @Override
//    public Optional<Purchase> findById(UUID id) {
//        return purchaseDao.findById(id);
//    }
//
//    @Override
//    public Purchase save(Purchase entity) {
//        return purchaseDao.save(entity);
//    }
//
//    @Override
//    public void deleteById(UUID id) {
//        purchaseDao.deleteById(id);
//    }
//
//    @Override
//    public Purchase update(Purchase e) {
//        return purchaseDao.update(e);
//    }
//
//    @Override
//    public List<Purchase> findByPurchaseDateInPeriod(LocalDateTime left, LocalDateTime right) {
//        return purchaseDao.findByPurchaseDateInPeriod(left, right);
//    }
//
//    @Override
//    public List<Purchase> findByProductName(String name) {
//        return purchaseDao.findByProductName(name);
//    }
//
//    @Override
//    public Optional<Purchase> findByIdEager(UUID uuid) {
//        return purchaseDao.findByIdEager(uuid);
//    }
//}
