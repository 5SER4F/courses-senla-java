package org.uhanov.repository;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.config.TestConfig;
import org.uhanov.dto.*;
import org.uhanov.dto.mapper.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.uhanov.repository.Tests1SaveTests.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@TestExecutionListeners(
        listeners = {DirtiesContextTestExecutionListener.class,
                DependencyInjectionTestExecutionListener.class},
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Tests3Update {
    @Autowired
    TestedRepositoryHolder repo;

    @Autowired
    UserMapper userMapper;

    @Autowired
    StaffMapper staffMapper;

    @Autowired
    CreatorMapper creatorMapper;

    @Autowired
    GenreMapper genreMapper;

    @Autowired
    AgeRatingMapper ageRatingMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    PurchaseMapper purchaseMapper;

    @Test
    @Transactional
    public void test1whenUpdateUser_thenReturnUpdatedUserAndFindReturnSame() {
        UserAuthDTO patch = UserAuthDTO.builder()
                .password("UPDATEDsecret123")
                .firstname("UPDATEDAlice")
                .surname("UPDATEDWonderland")
                .nickname("UPDATEDWonderAlice")
                .birthDate(LocalDate.now().minusDays(10))
                .registrationDate(LocalDateTime.now())
                .country("UPDATEDWonderland")
                .build();

        userMapper.updateUser(patch, addedUser);
        addedUser = repo.userRepository.update(addedUser);

        assertEquals(addedUser,
                repo.userRepository.findById(addedUser.getId()).get());
    }

    @Test
    public void test2whenUpdateStaff_thenReturnUpdatedStaffAndFindReturnSame() {
        StaffAuthDTO patch = StaffAuthDTO.builder()
                .password("UPDATEDpassword123")
                .firstname("UPDATEDJohn")
                .surname("UPDATEDDoe")
                .birthDate(LocalDate.now().minusDays(10))
                .registrationDate(LocalDateTime.now())
                .build();

        staffMapper.updateStaff(patch, addedStaff);
        addedStaff = repo.staffRepository.update(addedStaff);
        assertEquals(addedStaff,
                repo.staffRepository.findById(addedStaff.getId()).get());
    }

    @Test
    public void test3whenUpdateCreator_thenReturnUpdatedCreatorAndFindReturnSame() {
        CreatorAuthDTO patch = CreatorAuthDTO.builder()
                .password("UPDATEDpassword1")
                .name("UPDATEDUser1")
                .registrationDate(LocalDateTime.now())
                .build();

        creatorMapper.updateCreator(patch, addedCreator);

        addedCreator = repo.creatorRepository.update(addedCreator);

        assertEquals(addedCreator,
                repo.creatorRepository.findById(addedCreator.getId()).get());
    }

    @Test
    public void test4whenUpdateGenre_thenReturnUpdatedGenreAndFindReturnSame() {
        GenreDTO patch = GenreDTO.builder()
                .name("UPDATEDGenre 1")
                .lastChanger(addedStaff)
                .build();
        genreMapper.updateGenre(patch, addedGenre);

        addedGenre = repo.genreRepository.update(addedGenre);

        assertEquals(addedGenre,
                repo.genreRepository.findById(addedGenre.getId()).get());

    }


    @Test
    public void test5whenUpdateAgeRating_thenReturnUpdatedAgeRatingAndFindReturnSame() {
        AgeRatingDTO patch = AgeRatingDTO.builder()
                .name("UPDATEDAgeRating1")
                .lastChanger(addedStaff)
                .build();

        ageRatingMapper.updateAgeRating(patch, addedAgeRating);

        addedAgeRating = repo.ageRatingRepository.update(addedAgeRating);

        assertEquals(addedAgeRating,
                repo.ageRatingRepository.findById(addedAgeRating.getId()).get());
    }

    @Test
    public void test6whenUpdateProduct_thenReturnUpdatedProductAndFindReturnSame() {
        ProductDTO patch = ProductDTO.builder()
                .name("UPDATEDProduct 1")
                .dateAdded(LocalDate.now())
                .ageRating(addedAgeRating)
                .price(BigDecimal.valueOf(999.99))
                .build();

        productMapper.updateProduct(patch, addedProduct);

        addedProduct = repo.productRepository.update(addedProduct);

        assertEquals(addedProduct,
                repo.productRepository.findById(addedProduct.getId()).get());
    }

    @Test
    public void test7whenUpdatePurchase_thenReturnUpdatedPurchaseAndFindReturnSame() {
        PurchaseDTO patch = PurchaseDTO.builder()
                .cost(BigDecimal.valueOf(999.99))
                .purchaseDate(LocalDateTime.now())
                .build();

        purchaseMapper.updatePurchase(patch, addedPurchase);

        addedPurchase = repo.purchaseRepository.update(addedPurchase);

        assertEquals(addedPurchase,
                repo.purchaseRepository.findById(addedPurchase.getId()).get());

    }
}
