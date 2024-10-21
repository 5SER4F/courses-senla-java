package org.uhanov.service.impl;

import org.junit.jupiter.api.*;
import org.uhanov.dto.mapper.CreatorMapperImpl;
import org.uhanov.dto.mapper.ProductMapper;
import org.uhanov.dto.mapper.ProductMapperImpl;
import org.uhanov.dto.product.ProductDto;
import org.uhanov.dto.product.ProductPostDto;
import org.uhanov.model.AgeRating;
import org.uhanov.model.Creator;
import org.uhanov.model.Genre;
import org.uhanov.model.Product;
import org.uhanov.repository.api.AgeRatingRepository;
import org.uhanov.repository.api.CreatorRepository;
import org.uhanov.repository.api.GenreRepository;
import org.uhanov.repository.api.ProductRepository;
import org.uhanov.service.api.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductServiceMockTest {
    ProductService productService;
    ProductRepository productRepositoryMock;
    GenreRepository genreRepositoryMock;
    AgeRatingRepository ageRatingRepositoryMock;
    CreatorRepository creatorRepositoryMock;
    ProductMapper productMapper;

    @BeforeEach
    public void init() {
        productRepositoryMock = mock(ProductRepository.class);
        genreRepositoryMock = mock(GenreRepository.class);
        creatorRepositoryMock = mock(CreatorRepository.class);
        ageRatingRepositoryMock = mock(AgeRatingRepository.class);

        productMapper = initProductMapper();

        productService = new ProductServiceImpl(
                productRepositoryMock,
                genreRepositoryMock,
                ageRatingRepositoryMock,
                creatorRepositoryMock,
                productMapper
        );
    }

    @Test
    @Order(1)
    public void whenCreateProduct_thenSetAllEntityInProductAndSave() {
        final UUID productId = UUID.randomUUID();

        Genre genre = Genre.builder()
                .id(UUID.randomUUID())
                .build();
        AgeRating ageRating = AgeRating.builder()
                .id(UUID.randomUUID())
                .build();
        Creator creator = Creator.builder()
                .id(UUID.randomUUID())
                .build();

        when(productRepositoryMock.save(any(Product.class)))
                .thenAnswer(invocationOnMock -> {
                            Product p = invocationOnMock
                                    .getArgument(0, Product.class);
                            p.setId(productId);
                            return p;
                        }
                );
        when(genreRepositoryMock.getGenresByIds(List.of(genre.getId())))
                .thenReturn(List.of(genre));
        when(ageRatingRepositoryMock.findById(ageRating.getId()))
                .thenReturn(Optional.of(ageRating));
        when(creatorRepositoryMock.findById(creator.getId()))
                .thenReturn(Optional.of(creator));

        ProductDto afterCreate = productService.create(ProductPostDto
                .builder()
                .genresIds(Set.of(genre.getId()))
                .ageRatingId(ageRating.getId())
                .creatorId(creator.getId())
                .build());

        assertEquals(afterCreate.getId(), productId);

        assertEquals(afterCreate.getGenres().stream().findFirst().get().getId(), genre.getId());

        assertEquals(afterCreate.getAgeRating().getId(), ageRating.getId());

        assertEquals(afterCreate.getCreator().getId(), creator.getId());

        verify(productRepositoryMock, times(1))
                .save(any(Product.class));

        verify(genreRepositoryMock, times(1))
                .getGenresByIds(anyList());

        verify(ageRatingRepositoryMock, times(1))
                .findById(ageRating.getId());

        verify(creatorRepositoryMock, times(1))
                .findById(creator.getId());
    }

    @Test
    @Order(2)
    public void whenGetById_thenRepositoryCallFind() {
        final UUID productId = UUID.randomUUID();

        when(productRepositoryMock.findById(productId))
                .thenReturn(Optional.of(Product.builder().id(productId).build()));

        ProductDto afterGet = productService.getById(productId);

        assertEquals(afterGet.getId(), productId);

        verify(productRepositoryMock, times(1))
                .findById(productId);


    }

    @Test
    @Order(3)
    public void whenUpdate_thenUpdateNonNullPropAndRepoUpdate() {
        final UUID productId = UUID.randomUUID();

        Genre genre = Genre.builder()
                .id(UUID.randomUUID())
                .build();
        AgeRating ageRating = AgeRating.builder()
                .id(UUID.randomUUID())
                .build();
        Creator creator = Creator.builder()
                .id(UUID.randomUUID())
                .build();

        ProductPostDto productPostDto = ProductPostDto
                .builder()
                .id(productId)
                .genresIds(Set.of(genre.getId()))
                .ageRatingId(ageRating.getId())
                .creatorId(creator.getId())
                .build();

        when(productRepositoryMock.findById(productId))
                .thenReturn(Optional.of(Product.builder().id(productId).build()));

        when(genreRepositoryMock.getGenresByIds(List.of(genre.getId())))
                .thenReturn(List.of(genre));
        when(ageRatingRepositoryMock.findById(ageRating.getId()))
                .thenReturn(Optional.of(ageRating));
        when(creatorRepositoryMock.findById(creator.getId()))
                .thenReturn(Optional.of(creator));

        productService.update(productPostDto);

        verify(productRepositoryMock, times(1))
                .save(any(Product.class));

        verify(genreRepositoryMock, times(1))
                .getGenresByIds(anyList());

        verify(ageRatingRepositoryMock, times(1))
                .findById(ageRating.getId());

        verify(creatorRepositoryMock, times(1))
                .findById(creator.getId());

    }

    @Test
    @Order(4)
    public void whenDelete_thenCallRepoDelete() {
        final UUID productId = UUID.randomUUID();
        productService.delete(productId);

        verify(productRepositoryMock, times(1))
                .deleteById(productId);
    }


    public static ProductMapper initProductMapper() {
        ProductMapper ageRatingMapper = new ProductMapperImpl();
        try {
            var ageRatingMapperField = ageRatingMapper.getClass()
                    .getDeclaredField("ageRatingMapper");
            ageRatingMapperField.setAccessible(true);
            ageRatingMapperField.set(ageRatingMapper, AgeRatingServiceMockTest.initAgeRatingMapper());

            var creatorMapperField = ageRatingMapper.getClass()
                    .getDeclaredField("creatorMapper");
            creatorMapperField.setAccessible(true);
            creatorMapperField.set(ageRatingMapper, new CreatorMapperImpl());

            var genreMapperField = ageRatingMapper.getClass()
                    .getDeclaredField("genreMapper");
            genreMapperField.setAccessible(true);
            genreMapperField.set(ageRatingMapper, GenreServiceMockTest.initGenreMapper());
        } catch (Exception e) {
            System.out.println("Fail to create mapper for test");
            e.printStackTrace();
        }
        return ageRatingMapper;
    }
}
