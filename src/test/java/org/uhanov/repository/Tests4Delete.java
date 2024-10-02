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
import org.uhanov.config.TestConfig;

import static org.junit.Assert.assertTrue;
import static org.uhanov.repository.Tests1SaveTests.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@TestExecutionListeners(
        listeners = {DirtiesContextTestExecutionListener.class,
                DependencyInjectionTestExecutionListener.class},
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Tests4Delete {
    @Autowired
    TestedRepositoryHolder repo;

    @Test
    public void test7whenDeleteUser_thenFindReturnNull() {
        repo.userRepository.deleteById(addedUser.getId());
        assertTrue(repo.userRepository.findById(addedUser.getId()).isEmpty());
    }

    @Test
    public void test6whenDeleteStaff_thenFindReturnNull() {
        repo.staffRepository.deleteById(addedStaff.getId());
        assertTrue(repo.staffRepository.findById(addedStaff.getId()).isEmpty());
    }

    @Test
    public void test5whenDeleteCreator_thenFindReturnNull() {
        repo.creatorRepository.deleteById(addedCreator.getId());
        assertTrue(repo.creatorRepository.findById(addedCreator.getId()).isEmpty());
    }

    @Test
    public void test4whenDeleteGenre_thenFindReturnNull() {
        repo.genreRepository.deleteById(addedGenre.getId());
        assertTrue(repo.genreRepository.findById(addedGenre.getId()).isEmpty());
    }


    @Test
    public void test3whenDeleteAgeRating_thenFindReturnNull() {
        repo.ageRatingRepository.deleteById(addedAgeRating.getId());
        assertTrue(repo.ageRatingRepository.findById(addedAgeRating.getId()).isEmpty());
    }

    @Test
    public void test2whenDeleteProduct_thenFindReturnNull() {
        repo.productRepository.deleteById(addedProduct.getId());
        assertTrue(repo.productRepository.findById(addedProduct.getId()).isEmpty());
    }

    @Test
    public void test1whenDeletePurchase_thenFindReturnNull() {
        repo.purchaseRepository.deleteById(addedPurchase.getId());
        assertTrue(repo.purchaseRepository.findById(addedPurchase.getId()).isEmpty());
    }

//    repo..deleteById(.getId());
//    assertTrue(repo..findById(.getId()).isEmpty());
}
