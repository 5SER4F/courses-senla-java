package org.uhanov.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uhanov.dto.mapper.PurchaseMapper;
import org.uhanov.dto.mapper.PurchaseMapperImpl;
import org.uhanov.dto.mapper.UserMapperImpl;
import org.uhanov.dto.purchase.PurchaseDto;
import org.uhanov.dto.purchase.PurchasePostDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class PurchaseServiceMockTest {
    PurchaseService purchaseService;

    PurchaseRepository purchaseRepositoryMock;
    UserRepository userRepositoryMock;
    ProductRepository productRepositoryMock;
    PurchaseMapper purchaseMapper;

    @BeforeEach
    public void init() {
        purchaseRepositoryMock = mock(PurchaseRepository.class);
        userRepositoryMock = mock(UserRepository.class);
        productRepositoryMock = mock(ProductRepository.class);
        purchaseMapper = initMapper();

        purchaseService = new PurchaseServiceImpl(
                purchaseRepositoryMock,
                userRepositoryMock,
                productRepositoryMock,
                purchaseMapper
        );
    }

    @Test
    public void whenCreateProduct_thenSetAllEntityInProductAndSave() {
        final UUID purchaseId = UUID.randomUUID();

        User buyer = User.builder()
                .id(UUID.randomUUID())
                .balance(10000.0)
                .build();
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .price(BigDecimal.valueOf(100.0))
                .discount(0.5)
                .build();

        when(purchaseRepositoryMock.save(any(Purchase.class)))
                .thenAnswer(invocationOnMock -> {
                            Purchase p = invocationOnMock
                                    .getArgument(0, Purchase.class);
                            p.setId(purchaseId);
                            return p;
                        }
                );
        when(userRepositoryMock.findById(buyer.getId()))
                .thenReturn(Optional.of(buyer));
        when(productRepositoryMock.findById(product.getId()))
                .thenReturn(Optional.of(product));

        PurchaseDto afterCreate = purchaseService.create(PurchasePostDto
                .builder()
                .productId(product.getId())
                .buyerId(buyer.getId())
                .build());

        assertEquals(afterCreate.getId(), purchaseId);


        assertEquals(afterCreate.getProduct().getId(), product.getId());

        assertEquals(afterCreate.getBuyer().getId(), buyer.getId());

        verify(productRepositoryMock, times(1))
                .findById(product.getId());

        verify(userRepositoryMock, times(1))
                .findById(buyer.getId());

        verify(productRepositoryMock, times(1))
                .findById(product.getId());

    }

    @Test
    public void whenGetById_thenRepositoryCallFind() {
        final UUID purchaseId = UUID.randomUUID();

        when(purchaseRepositoryMock.findById(purchaseId))
                .thenReturn(Optional.of(Purchase.builder().id(purchaseId).build()));

        PurchaseDto afterGet = purchaseService.getById(purchaseId);

        assertEquals(afterGet.getId(), purchaseId);

        verify(purchaseRepositoryMock, times(1))
                .findById(purchaseId);


    }

    @Test
    public void whenDelete_thenCallRepoDelete() {
        final UUID purchaseUuid = UUID.randomUUID();
        purchaseService.delete(purchaseUuid);

        verify(purchaseRepositoryMock, times(1))
                .deleteById(purchaseUuid);
    }

    public static PurchaseMapper initMapper() {
        PurchaseMapperImpl purchaseMapper = new PurchaseMapperImpl();
        try {
            var userMapper = purchaseMapper.getClass()
                    .getDeclaredField("userMapper");
            userMapper.setAccessible(true);
            userMapper.set(purchaseMapper, new UserMapperImpl());

            var productMapper = purchaseMapper.getClass()
                    .getDeclaredField("productMapper");
            productMapper.setAccessible(true);
            productMapper.set(purchaseMapper, ProductServiceMockTest.initProductMapper());
        } catch (Exception e) {
            System.out.println("Fail to create mapper for test");
            e.printStackTrace();
        }
        return purchaseMapper;
    }
}
