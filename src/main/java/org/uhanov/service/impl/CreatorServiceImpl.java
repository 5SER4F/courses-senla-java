package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.dto.creator.CreatorDto;
import org.uhanov.dto.creator.CreatorPostDto;
import org.uhanov.dto.mapper.CreatorMapper;
import org.uhanov.exception.ResourceNotFoundException;
import org.uhanov.model.Creator;
import org.uhanov.model.product.Product;
import org.uhanov.model.purchase.Purchase;
import org.uhanov.model.user.AccountStatus;
import org.uhanov.repository.api.CreatorRepository;
import org.uhanov.repository.api.ProductRepository;
import org.uhanov.repository.api.PurchaseRepository;
import org.uhanov.security.JwtUtil;
import org.uhanov.service.api.CreatorService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
@Data
@RequiredArgsConstructor
public class CreatorServiceImpl implements CreatorService {
    private final CreatorRepository repository;
    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;
    private final CreatorMapper creatorMapper;
    private final JwtUtil jwtUtils;

    @Transactional
    @Override
    public CreatorDto create(CreatorPostDto creatorPostDto) {
        Creator newCreator = creatorMapper.authToModel(creatorPostDto);
        newCreator.setAccountStatus(AccountStatus.CREATED);
        return creatorMapper.toDto(
                repository.save(
                        newCreator
                )
        );
    }

    @Transactional(readOnly = true)
    @Override
    public CreatorDto getById(UUID uuid) {
        return creatorMapper.toDto(get(uuid));
    }

    @Transactional
    @Override
    public CreatorDto update(CreatorPostDto creatorPostDto) {
        Creator creator = get(creatorPostDto.getId());
        creatorMapper.updateCreator(creatorPostDto, creator);
        return creatorMapper.toDto(
                repository.save(creator)
        );
    }

    private Creator get(UUID uuid) {
        Optional<Creator> creator = repository.findById(uuid);
        return creator
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    @Override
    public void countProfit(UUID creatorId) {
        List<UUID> creatorProductsIds = productRepository
                .findAllByCreator_IdIn(List.of(creatorId))
                .stream()
                .map(Product::getId)
                .collect(Collectors.toList());
        double profit = purchaseRepository.countFinalCostWhereProduct_IdInAndPurchaseDateAfter(
                creatorProductsIds,
                LocalDateTime.now().minusDays(Purchase.PERIOD_OF_REFUND)
        );
        Creator creator = get(creatorId);

        creator.changeBalance(profit);

        creator.setLastSettlementDate(LocalDateTime.now());

        repository.save(creator);
    }
}
