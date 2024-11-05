package org.uhanov.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.dto.mapper.ProductMapper;
import org.uhanov.dto.product.ProductDto;
import org.uhanov.dto.product.ProductGetAllParams;
import org.uhanov.dto.product.ProductPostDto;
import org.uhanov.exception.InvalidLoginException;
import org.uhanov.exception.ResourceNotFoundException;
import org.uhanov.model.Creator;
import org.uhanov.model.Genre;
import org.uhanov.model.product.Product;
import org.uhanov.model.product.ProductStatus;
import org.uhanov.model.purchase.PurchaseStatus;
import org.uhanov.model.user.User;
import org.uhanov.repository.api.CreatorRepository;
import org.uhanov.repository.api.GenreRepository;
import org.uhanov.repository.api.ProductRepository;
import org.uhanov.repository.api.PurchaseRepository;
import org.uhanov.service.api.ProductService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
@Data
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final GenreRepository genreRepository;
    private final CreatorRepository creatorRepository;
    private final PurchaseRepository purchaseRepository;
    private final ProductMapper productMapper;

    @Transactional
    @Override
    public ProductDto create(ProductPostDto dto, UUID idFromToken) {
        Product newProduct = productMapper.toModel(dto);
        newProduct.setGenres(
                Set.copyOf(genreRepository.getGenresByIds(
                        new ArrayList<>(dto.getGenresIds()))
                )
        );

        Creator creator = creatorRepository.findById(dto.getCreatorId())
                .orElseThrow(ResourceNotFoundException::new);

        validProductCreator(creator, idFromToken);

        newProduct.setCreator(
                creator
        );

        newProduct.setProductStatus(ProductStatus.ADDED);

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
    public ProductDto update(ProductPostDto dto, UUID idFromToken) {
        Product product = get(dto.getId());
        productMapper.updateProduct(dto, product);

        Creator creator = creatorRepository.findById(product.getCreator().getId())
                .orElseThrow(ResourceNotFoundException::new);

        validProductCreator(creator, idFromToken);

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
    public void delete(UUID uuid, UUID idFromToken) {
        Product product = get(uuid);

        validProductCreator(product.getCreator(), idFromToken);

        product.setProductStatus(ProductStatus.REMOVED);

        repository.save(product);
    }

    @Transactional
    @Override
    public List<ProductDto> loadCustomerLibrary(UUID customerId) {
        return repository.findAllByPurchaseOfProduct_Buyer_IdAndPurchaseOfProduct_PurchaseStatus(customerId,
                        PurchaseStatus.CREATED)
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Page<ProductDto> findAllWithParam(
            ProductGetAllParams params, Double maxPrice,
            Pageable pageable

    ) {
        if (params == null) {
            return repository.findAllByDateAddedBetweenAndPriceLessThanOrderByPriceAsc(
                    LocalDate.MIN, LocalDate.now(), BigDecimal.valueOf(maxPrice), pageable
            ).map(productMapper::toDto);
        }

        Set<UUID> productsIdsWithPassedGenresAndCreatorsNames = productsWithGenresAndCreators(
                params
        );

        Page<Product> response = productsIdsWithPassedGenresAndCreatorsNames == null ?
                repository.findAllByDateAddedBetweenAndPriceLessThanOrderByPriceAsc(
                        params.getMinDate(), params.getMaxDate(), BigDecimal.valueOf(maxPrice), pageable
                ) :
                repository
                        .findAllByIdInAndDateAddedBetweenAndPriceLessThanOrderByPriceAsc(
                                productsIdsWithPassedGenresAndCreatorsNames,
                                params.getMinDate(), params.getMaxDate(), BigDecimal.valueOf(maxPrice), pageable

                        );

        return response.map(productMapper::toDto);
    }

    private Set<UUID> productsWithGenresAndCreators(ProductGetAllParams productGetAllParams) {
        List<String> genresNames = productGetAllParams.getGenresNames();
        Set<UUID> productIdsWithPassedGenresNames = genresNames.isEmpty() ?
                null :
                productsWithGenres(genresNames);

        List<String> creatorsNames = productGetAllParams.getCreatorNames();
        Set<UUID> productsIdsWithPassedCreatorsNames = creatorsNames.isEmpty() ?
                null :
                productsWithCreators(creatorsNames);

        return intersectionOfSet(
                productIdsWithPassedGenresNames,
                productsIdsWithPassedCreatorsNames);

    }

    private Set<UUID> intersectionOfSet(Set<UUID> s1, Set<UUID> s2) {
        if (s1 == null) {
            return s2;
        }
        if (s2 == null) {
            return s1;
        }
        s1.retainAll(s2);
        return s1;
    }

    private Set<UUID> productsWithGenres(List<String> genresNames) {
        Set<UUID> genresIdsWithPassedNames = genreRepository.findAllByNameIn(genresNames)
                .stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());
        return repository.findAllByGenres_IdIn(genresIdsWithPassedNames)
                .stream()
                .map(Product::getId)
                .collect(Collectors.toSet());
    }

    private Set<UUID> productsWithCreators(List<String> creatorsNames) {
        Set<UUID> creatorsIdsWithPassedNames = creatorRepository.findAllByNameInIgnoreCase(creatorsNames)
                .stream()
                .map(User::getId)
                .collect(Collectors.toSet());
        return repository.findAllByCreator_IdIn(creatorsIdsWithPassedNames)
                .stream()
                .map(Product::getId)
                .collect(Collectors.toSet());
    }

    private Product get(UUID uuid) {
        Optional<Product> product = repository.findById(uuid);
        return product
                .orElseThrow(ResourceNotFoundException::new);
    }

    private void validProductCreator(Creator creator, UUID idFromToken) {
        if (!creator.getId().equals(idFromToken)) {
            throw new InvalidLoginException("Попытка доступа к чужому продукту");
        }
    }

}
