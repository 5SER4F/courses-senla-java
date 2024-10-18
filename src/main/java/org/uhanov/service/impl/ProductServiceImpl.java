package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.dto.mapper.ProductMapper;
import org.uhanov.dto.product.ProductDto;
import org.uhanov.dto.product.ProductPostDto;
import org.uhanov.exception.ResourceNotFoundException;
import org.uhanov.model.Product;
import org.uhanov.repository.api.AgeRatingRepository;
import org.uhanov.repository.api.CreatorRepository;
import org.uhanov.repository.api.GenreRepository;
import org.uhanov.repository.api.ProductRepository;
import org.uhanov.service.api.ProductService;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Transactional
@Service
@Data
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final GenreRepository genreRepository;
    private final AgeRatingRepository ageRatingRepository;
    private final CreatorRepository creatorRepository;
    private final ProductMapper productMapper;

    @Transactional
    @Override
    public ProductDto create(ProductPostDto dto) {
        Product newProduct = productMapper.toModel(dto);
        newProduct.setGenres(
                Set.copyOf(genreRepository.getGenresByIds(
                        new ArrayList<>(dto.getGenresIds()))
                )
        );
        newProduct.setAgeRating(
                ageRatingRepository.findById(dto.getAgeRatingId())
                        .orElseThrow(ResourceNotFoundException::new)
        );
        newProduct.setCreator(
                creatorRepository.findById(dto.getCreatorId())
                        .orElseThrow(ResourceNotFoundException::new)
        );
        System.out.println(newProduct.getGenres());
        System.out.println(newProduct.getAgeRating());
        System.out.println(newProduct.getCreator());
        repository.save(newProduct);

        return productMapper.toDto(
                newProduct
        );
    }

    @Transactional(readOnly = true)
    @Override
    public ProductDto getById(UUID uuid) {
        return productMapper.toDto(
                repository.findById(uuid)
                        .orElseThrow(ResourceNotFoundException::new)
        );
    }

    @Transactional
    @Override
    public ProductDto update(ProductPostDto dto) {
        Product product = get(dto.getId());
        productMapper.updateProduct(dto, product);

        if (dto.getAgeRatingId() != null) {
            product.setAgeRating(
                    ageRatingRepository.findById(dto.getAgeRatingId())
                            .orElseThrow(ResourceNotFoundException::new)
            );
        }
        if (dto.getCreatorId() != null) {
            product.setCreator(
                    creatorRepository.findById(dto.getCreatorId())
                            .orElseThrow(ResourceNotFoundException::new)
            );
        }
        if (dto.getGenresIds() != null &&
                !dto.getGenresIds().isEmpty()) {
            product.setGenres(
                    Set.copyOf(genreRepository.getGenresByIds(new ArrayList<>(dto.getGenresIds())))
            );
        }
        return productMapper.toDto(
                repository.save(product)
        );
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        repository.deleteById(uuid);
    }

    private Product get(UUID uuid) {
        Optional<Product> product = repository.findById(uuid);
        return product
                .orElseThrow(ResourceNotFoundException::new);
    }
}
