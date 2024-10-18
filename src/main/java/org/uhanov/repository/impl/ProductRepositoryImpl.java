//package org.uhanov.repository.impl;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//import org.uhanov.model.Product;
//import org.uhanov.repository.api.ProductRepository;
//import org.uhanov.repository.dao.ProductDao;
//
//import java.util.Optional;
//import java.util.UUID;
//
//@Repository
//@RequiredArgsConstructor
//public class ProductRepositoryImpl implements ProductRepository {
//    private final ProductDao productDao;
//
//    @Override
//    public Optional<Product> findById(UUID id) {
//        return productDao.findById(id);
//    }
//
//    @Override
//    public Product save(Product entity) {
//        return productDao.save(entity);
//    }
//
//    @Override
//    public void deleteById(UUID id) {
//        productDao.deleteById(id);
//    }
//
//    @Override
//    public Product update(Product e) {
//        return productDao.update(e);
//    }
//
//    @Override
//    public Optional<Product> findByIdEager(UUID uuid) {
//        return productDao.findByIdEager(uuid);
//    }
//}
