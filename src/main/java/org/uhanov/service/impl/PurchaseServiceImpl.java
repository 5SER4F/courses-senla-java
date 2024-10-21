package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.dto.mapper.PurchaseMapper;
import org.uhanov.dto.purchase.PurchaseDto;
import org.uhanov.dto.purchase.PurchasePostDto;
import org.uhanov.exception.PurchaseException;
import org.uhanov.exception.ResourceNotFoundException;
import org.uhanov.model.Product;
import org.uhanov.model.Purchase;
import org.uhanov.model.User;
import org.uhanov.repository.api.ProductRepository;
import org.uhanov.repository.api.PurchaseRepository;
import org.uhanov.repository.api.UserRepository;
import org.uhanov.service.api.PurchaseService;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@Data
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseRepository repository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PurchaseMapper purchaseMapper;

    @Transactional
    @Override
    public PurchaseDto create(
            PurchasePostDto dto
    ) {
        User buyer = userRepository.findById(dto.getBuyerId())
                .orElseThrow(ResourceNotFoundException::new);
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(ResourceNotFoundException::new);

        double balanceAfterBuy = countPurchase(buyer, product);
        if (balanceAfterBuy < 0) {
            throw new PurchaseException("User with id=" + buyer.getId() +
                    "trying create purchase on more money than have");
        }
        buyer.setBalance(balanceAfterBuy);
        userRepository.save(buyer);

        Purchase newPurchase = purchaseMapper.toModel(dto);
        newPurchase.setBuyer(buyer);
        newPurchase.setProduct(product);
        newPurchase.setCost(BigDecimal.valueOf(purchaseCost(product)));

        return purchaseMapper.toDto(
                repository.save(newPurchase)
        );
    }

    @Transactional(readOnly = true)
    @Override
    public PurchaseDto getById(UUID uuid) {
        return purchaseMapper.toDto(
                getEntityById(uuid)
        );
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        repository.deleteById(uuid);
    }

    private Purchase getEntityById(UUID uuid) {
        Optional<Purchase> purchase = repository.findById(uuid);
        return purchase
                .orElseThrow(ResourceNotFoundException::new);
    }

    private double countPurchase(User user, Product product) {
        return user.getBalance() - purchaseCost(product);
    }

    private double purchaseCost(Product product) {
        return product.getPrice() -
                product.getPrice() * product.getDiscount();
    }
}
