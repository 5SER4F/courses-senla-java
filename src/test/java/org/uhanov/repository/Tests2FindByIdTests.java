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
import org.uhanov.model.*;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.uhanov.repository.Tests1SaveTests.*;

@RunWith(
        SpringJUnit4ClassRunner.class
)
@ContextConfiguration(classes = TestConfig.class)
@TestExecutionListeners(
        listeners = {DirtiesContextTestExecutionListener.class,
                DependencyInjectionTestExecutionListener.class},
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Tests2FindByIdTests {
    @Autowired
    TestedRepositoryHolder repo;

    @Test
    @Transactional

    public void test1whenFindByIdUser_thenReturnUser() {
        assertEquals(addedUser,
                repo.userRepository.findById(addedUser.getId())
                        .get()
        );
    }

    @Test
//    @Transactional

    public void test2whenFindByIdStaff_thenReturnStaff() {
        assertEquals(addedStaff,
                repo.staffRepository.findById(addedStaff.getId())
                        .get()
        );
    }

    @Test
    @Transactional
    public void test2whenFindByIdStaffEager_thenReturnStaff() {
        //Eager
        Staff staffByIdEager = repo.staffRepository.findStaffByIdEager(addedStaff.getId()).get();

        assertEquals(staffByIdEager, addedStaff);

        assertEquals(staffByIdEager.getAgeRatingsAddedBy(),
                Set.of(addedAgeRating));

        assertEquals(staffByIdEager.getGenresAddedBy(),
                Set.of(addedGenre));

    }


    @Test
    @Transactional
    public void test3whenFindByIdCreator_thenReturnCreator() {
        assertEquals(addedCreator,
                repo.creatorRepository.findById(addedCreator.getId())
                        .get()
        );


    }

    @Test
    @Transactional
    public void test3whenFindByIdCreatorEager_thenReturnCreator() {
        Creator creatorEager = repo
                .creatorRepository
                .findByIdEager(addedCreator.getId())
                .get();
        assertEquals(creatorEager, addedCreator);
        assertEquals(creatorEager.getProducts().get(0),
                addedProduct);
    }

    @Test
    @Transactional
    public void test3whenFindByNameCreator_thenReturnCreator() {
        assertEquals(addedCreator,
                repo.creatorRepository
                        .findByName(addedCreator.getName())
                        .get(0));
    }

    @Test
    @Transactional
    public void test4whenFindByIdGenre_thenReturnGenre() {
        assertEquals(addedGenre,
                repo.genreRepository.findById(addedGenre.getId())
                        .get()
        );
        assertEquals(addedStaff,
                repo.genreRepository.findById(addedGenre.getId())
                        .get().getLastChanger()
        );
    }

    @Test
    @Transactional
    public void test4whenFindByIdGenreEager_thenReturnGenre() {
        assertEquals(addedGenre,
                repo.genreRepository
                        .findByIdEager(addedGenre.getId())
                        .get());
    }

    @Test
    @Transactional
    public void test4whenFindByStaffIdGenre_thenReturnGenre() {
        assertEquals(addedGenre,
                repo.genreRepository
                        .findByStaffId(addedStaff.getId())
                        .get(0));
    }


    @Test
    @Transactional
    public void test5whenFindByIdAgeRating_thenReturnAgeRating() {
        assertEquals(addedAgeRating,
                repo.ageRatingRepository.findById(addedAgeRating.getId())
                        .get()
        );

        assertEquals(addedStaff,
                repo.ageRatingRepository.findById(addedAgeRating.getId())
                        .get().getLastChanger()
        );
    }

    @Test
    @Transactional
    public void test5whenFindByIdAgeRatingEager_thenReturnAgeRating() {
        AgeRating eager = repo.ageRatingRepository
                .findByIdEager(addedAgeRating.getId())
                .get();
        assertEquals(addedAgeRating,
                eager);
        assertEquals(addedStaff,
                eager.getLastChanger());
    }

    @Test
    @Transactional
    public void test6whenFindByIdProduct_thenReturnProduct() {
        assertEquals(addedProduct,
                repo.productRepository.findById(addedProduct.getId()).get()
        );
        assertEquals(addedCreator,
                repo.productRepository.findById(addedProduct.getId())
                        .get()
                        .getCreator()
        );

        assertEquals(addedAgeRating,
                repo.productRepository.findById(addedProduct.getId())
                        .get()
                        .getAgeRating()
        );
    }

    @Test
    @Transactional
    public void test6whenFindByIdProductEager_thenReturnProduct() {
        Product eager = repo.productRepository
                .findByIdEager(addedProduct.getId())
                .get();
        assertEquals(addedProduct,
                eager
        );
        assertEquals(addedCreator,
                eager.getCreator()
        );

        assertEquals(addedAgeRating,
                eager.getAgeRating()
        );
    }

    @Test
    @Transactional
    public void test7whenFindByIdPurchase_thenReturnPurchase() {
        assertEquals(addedPurchase,
                repo.purchaseRepository.findById(addedPurchase.getId()).get());

        assertEquals(addedUser,
                repo.purchaseRepository.findById(addedPurchase.getId())
                        .get()
                        .getBuyer());

        assertEquals(addedProduct,
                repo.purchaseRepository.findById(addedPurchase.getId())
                        .get()
                        .getProduct());

    }

    @Test
    @Transactional
    public void test7whenFindByIdPurchaseEager_thenReturnPurchase() {
        Purchase eager = repo.purchaseRepository
                .findByIdEager(addedPurchase.getId())
                .get();

        assertEquals(addedPurchase,
                eager);

        assertEquals(addedUser,
                eager.getBuyer());

        assertEquals(addedProduct,
                eager.getProduct());

    }

    @Test
    @Transactional
    public void test7whenFindByProductNamePurchase_thenReturnPurchase() {
        Purchase eagerByProductName = repo.purchaseRepository
                .findByProductName(addedProduct.getName())
                .get(0);

        assertEquals(addedPurchase,
                eagerByProductName);

        assertEquals(addedUser,
                eagerByProductName.getBuyer());

        assertEquals(addedProduct,
                eagerByProductName.getProduct());

    }

    @Test
    @Transactional
    public void test7whenFindByDatePurchase_thenReturnPurchase() {
        Purchase purchaseByDate = repo.purchaseRepository
                .findByPurchaseDateInPeriod(
                        addedPurchase.getPurchaseDate().minusSeconds(1000L),
                        addedPurchase.getPurchaseDate().plusSeconds(1000L)
                ).get(0);
        assertEquals(
                addedPurchase,
                purchaseByDate
        );
    }

}
