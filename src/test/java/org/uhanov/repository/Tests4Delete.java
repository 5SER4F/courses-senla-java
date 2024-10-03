package org.uhanov.repository;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.uhanov.config.TestConfig;
import org.uhanov.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Tests4Delete {
    @Autowired
    TestedRepositoryHolder repo;

    User userToDelete;

    Staff staffToDelete;

    Creator creatorToDelete;

    Genre genreToDelete;

    AgeRating ageRatingToDelete;

    Product productToDelete;

    Purchase purchaseToDelete;

    @Before
    @Transactional
    public void init() {
        userToDelete = User.builder()
                .password("secret123")
                .firstname("Alice")
                .surname("Wonderland")
                .nickname("WonderAlice")
                .birthDate(LocalDate.now().minusDays(10))
                .registrationDate(LocalDateTime.now())
                .country("Wonderland")
                .build();
        userToDelete = repo.userRepository.save(userToDelete);

        staffToDelete = Staff.builder()
                .password("password123")
                .firstname("John")
                .surname("Doe")
                .birthDate(LocalDate.now().minusDays(10))
                .registrationDate(LocalDateTime.now())
                .build();

        staffToDelete = repo.staffRepository.save(staffToDelete);

        creatorToDelete = Creator.builder()
                .password("password1")
                .name("UserR1")
                .registrationDate(LocalDateTime.now())
                .build();

        creatorToDelete = repo.creatorRepository.save(creatorToDelete);

        genreToDelete = Genre.builder()
                .name("Genre 1")
                .lastChanger(staffToDelete)
                .build();

        genreToDelete = repo.genreRepository.save(genreToDelete);

        ageRatingToDelete = AgeRating.builder()
                .name("AgeRating1")
                .lastChanger(staffToDelete)
                .build();

        ageRatingToDelete = repo.ageRatingRepository.save(ageRatingToDelete);

        productToDelete = Product.builder()
                .creator(creatorToDelete)
                .name("ProductT 1")
                .dateAdded(LocalDate.now())
                .ageRating(ageRatingToDelete)
                .price(BigDecimal.valueOf(10.99))
                .genres(Set.of(genreToDelete))
                .build();

        productToDelete = repo.productRepository.save(productToDelete);


        purchaseToDelete = Purchase.builder()
                .buyer(userToDelete)
                .product(productToDelete)
                .cost(BigDecimal.valueOf(15.99))
                .purchaseDate(LocalDateTime.now().plusDays(100))
                .build();

        purchaseToDelete = repo.purchaseRepository.save(purchaseToDelete);

    }

    @Test
    @Transactional
    public void test7whenDeleteUser_thenFindReturnNull() {
        repo.userRepository.deleteById(userToDelete.getId());
        assertTrue(repo.userRepository.findById(userToDelete.getId()).isEmpty());
    }

    @Test
    @Transactional
    public void test6whenDeleteStaff_thenFindReturnNull() {
        repo.staffRepository.deleteById(staffToDelete.getId());
        assertTrue(repo.staffRepository.findById(staffToDelete.getId()).isEmpty());
    }

    @Test
    @Transactional
    public void test5whenDeleteCreator_thenFindReturnNull() {
        repo.creatorRepository.deleteById(creatorToDelete.getId());
        assertTrue(repo.creatorRepository.findById(creatorToDelete.getId()).isEmpty());
    }

    @Test
    @Transactional
    public void test4whenDeleteGenre_thenFindReturnNull() {
        repo.genreRepository.deleteById(genreToDelete.getId());
        assertTrue(repo.genreRepository.findById(genreToDelete.getId()).isEmpty());
    }


    @Test
    @Transactional
    public void test3whenDeleteAgeRating_thenFindReturnNull() {
        repo.ageRatingRepository.deleteById(ageRatingToDelete.getId());
        assertTrue(repo.ageRatingRepository.findById(ageRatingToDelete.getId()).isEmpty());
    }

    @Test
    @Transactional
    public void test2whenDeleteProduct_thenFindReturnNull() {
        repo.productRepository.deleteById(productToDelete.getId());
        assertTrue(repo.productRepository.findById(productToDelete.getId()).isEmpty());
    }

    @Test
    @Transactional
    public void test1whenDeletePurchase_thenFindReturnNull() {
        repo.purchaseRepository.deleteById(purchaseToDelete.getId());
        assertTrue(repo.purchaseRepository.findById(purchaseToDelete.getId()).isEmpty());
    }

//    repo..deleteById(.getId());
//    assertTrue(repo..findById(.getId()).isEmpty());
}
