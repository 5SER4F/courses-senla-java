package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.dto.mapper.PurchaseMapper;
import org.uhanov.dto.purchase.PurchaseDto;
import org.uhanov.dto.purchase.PurchasePostDto;
import org.uhanov.exception.InvalidLoginException;
import org.uhanov.exception.PurchaseException;
import org.uhanov.exception.ResourceNotFoundException;
import org.uhanov.model.Customer;
import org.uhanov.model.product.Product;
import org.uhanov.model.product.ProductStatus;
import org.uhanov.model.purchase.Purchase;
import org.uhanov.model.purchase.PurchaseStatus;
import org.uhanov.model.user.Role;
import org.uhanov.repository.api.CustomerRepository;
import org.uhanov.repository.api.ProductRepository;
import org.uhanov.repository.api.PurchaseRepository;
import org.uhanov.service.api.PurchaseService;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
@Data
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseRepository repository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final PurchaseMapper purchaseMapper;

    @Transactional
    @Override
    public PurchaseDto create(
            PurchasePostDto dto
    ) {
        Customer buyer = customerRepository.findById(dto.getBuyerId())
                .orElseThrow(ResourceNotFoundException::new);
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(ResourceNotFoundException::new);

        validPurchase(buyer, product);

        payPurchase(buyer, product);

        Purchase newPurchase = purchaseMapper.toModel(dto);
        newPurchase.setPurchaseStatus(PurchaseStatus.CREATED);

        newPurchase.setBuyer(buyer);
        newPurchase.setProduct(product);
        newPurchase.setFinalCost(BigDecimal.valueOf(product.getFinalPrice()));

        Purchase addedPurchase = repository.save(newPurchase);

        return purchaseMapper.toDto(addedPurchase);
    }

    @Transactional
    @Override
    public void delete(UUID purchaseId, UUID customerId) {
        Purchase purchaseToRefund = getPurchaseById(purchaseId);

        validRefund(purchaseToRefund, customerId);

        refundMoney(purchaseToRefund, customerId);

        purchaseToRefund.setPurchaseStatus(PurchaseStatus.RETURNED);

        repository.save(purchaseToRefund);
    }

    @Transactional(readOnly = true)
    @Override
    public PurchaseDto getById(UUID purchaseId, UUID customerIdFromToken, Role role) {
        Purchase purchase = repository.findById(purchaseId)
                .orElseThrow(EntityNotFoundException::new);
        if (role.equals(Role.CUSTOMER)) {
            validCustomer(purchase, customerIdFromToken);
        }
        return purchaseMapper.toDto(purchase);

    }

    @Transactional
    @Override
    public List<PurchaseDto> getCustomerPurchase(UUID customerId) {
        return repository.findAllByBuyer_Id(customerId)
                .stream()
                .map(purchaseMapper::toDto)
                .collect(Collectors.toList());
    }

    private void validCustomer(Purchase purchase, UUID customerIdFromToken) {
        if (!purchase
                .getBuyer()
                .getId()
                .equals(customerIdFromToken)) {
            throw new InvalidLoginException("Попытка доступа к чужой покупке");
        }
    }

    private void refundMoney(Purchase purchase, UUID customerId) {
        Customer buyer = customerRepository.findById(customerId)
                .orElseThrow(ResourceNotFoundException::new);

        buyer.changeBalance(
                purchase.getFinalCost().doubleValue()
        );
        customerRepository.save(buyer);
    }

    private void validRefund(Purchase purchaseToRefund, UUID customerId) {
        if (
                !purchaseToRefund
                        .getBuyer()
                        .getId()
                        .equals(customerId)
        ) {
            throw new InvalidLoginException("Попытка доступа к чужой покупке");
        }

        if (!purchaseToRefund.isCanBeRefund()) {
            throw new PurchaseException("Нельзя вернуть покупку после истечения срока возврата");
        }
    }

    private void payPurchase(Customer buyer, Product product) {
        double balanceAfterBuy = countPurchase(buyer, product);
        if (balanceAfterBuy < 0) {
            throw new PurchaseException("Пользователь с id=" + buyer.getId() +
                    "У пользователя не хватает средств для совершнеия покупки");
        }

        buyer.setBalance(balanceAfterBuy);
        customerRepository.save(buyer);
    }

    private void validPurchase(Customer buyer, Product product) {
        if (repository.existsByBuyer_IdAndProduct_IdAndPurchaseStatus(
                buyer.getId(), product.getId(), PurchaseStatus.CREATED
        )) {
            throw new PurchaseException("Попытка повторно купить продукт");
        }

        if (!product.isUserPassAgeFilter(buyer.getBirthDate())) {
            throw new PurchaseException("Возраст покупателя меньше, чем доступный для продукта");
        }

        if (product.getProductStatus().equals(ProductStatus.REMOVED)) {
            throw new PurchaseException("Попытка купить удаленный продукт");
        }
    }

    private Purchase getPurchaseById(UUID uuid) {
        Optional<Purchase> purchase = repository.findById(uuid);
        return purchase
                .orElseThrow(ResourceNotFoundException::new);
    }

    private double countPurchase(Customer customer, Product product) {
        return customer.getBalance() - product.getFinalPrice();
    }

}
