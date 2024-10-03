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

import static org.junit.Assert.assertEquals;

@RunWith(
        SpringJUnit4ClassRunner.class
)
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
public class Tests2FindByIdTests {
    @Autowired
    TestedRepositoryHolder repo;

    User user;

    Staff staff;

    Creator creator;

    Genre genre;

    AgeRating ageRating;

    Product product;

    Purchase purchase;

    @Before
    @Transactional
    public void init() {
        user = User.builder()
                .password("secret123")
                .firstname("Alice")
                .surname("Wonderland")
                .nickname("WonderAlice")
                .birthDate(LocalDate.now().minusDays(10))
                .registrationDate(LocalDateTime.now())
                .country("Wonderland")
                .build();
        user = repo.userRepository.save(user);

        staff = Staff.builder()
                .password("password123")
                .firstname("John")
                .surname("Doe")
                .birthDate(LocalDate.now().minusDays(10))
                .registrationDate(LocalDateTime.now())
                .build();

        staff = repo.staffRepository.save(staff);

        creator = Creator.builder()
                .password("password1")
                .name("UserR1")
                .registrationDate(LocalDateTime.now())
                .build();

        creator = repo.creatorRepository.save(creator);

        genre = Genre.builder()
                .name("Genre 1")
                .lastChanger(staff)
                .build();

        genre = repo.genreRepository.save(genre);

        ageRating = AgeRating.builder()
                .name("AgeRating1")
                .lastChanger(staff)
                .build();

        ageRating = repo.ageRatingRepository.save(ageRating);

        product = Product.builder()
                .creator(creator)
                .name("ProductT 1")
                .dateAdded(LocalDate.now())
                .ageRating(ageRating)
                .price(BigDecimal.valueOf(10.99))
                .genres(Set.of(genre))
                .build();

        product = repo.productRepository.save(product);


        purchase = Purchase.builder()
                .buyer(user)
                .product(product)
                .cost(BigDecimal.valueOf(15.99))
                .purchaseDate(LocalDateTime.now().plusDays(100))
                .build();

        purchase = repo.purchaseRepository.save(purchase);

    }


    @Test
    @Transactional
    public void test1whenFindByIdUser_thenReturnUser() {
        assertEquals(user,
                repo.userRepository.findById(user.getId())
                        .get()
        );
    }

    @Test
    @Transactional
    public void test2whenFindByIdStaffEager_thenReturnStaff() {
        //Eager
        Staff staffByIdEager = repo.staffRepository.findStaffByIdEager(staff.getId()).get();

        assertEquals(staffByIdEager, staff);

    }

    @Test
    @Transactional
    public void test2whenFindByIdStaff_thenReturnStaff() {
        assertEquals(staff,
                repo.staffRepository.findById(staff.getId())
                        .get()
        );
    }


    @Test
    @Transactional
    public void test3whenFindByIdCreator_thenReturnCreator() {
        assertEquals(creator,
                repo.creatorRepository.findById(creator.getId())
                        .get()
        );


    }

    @Test
    @Transactional
    public void test3whenFindByIdCreatorEager_thenReturnCreator() {
        Creator creatorEager = repo
                .creatorRepository
                .findByIdEager(creator.getId())
                .get();
        assertEquals(creatorEager, creator);
    }

    @Test
    @Transactional
    public void test3whenFindByNameCreator_thenReturnCreator() {
        assertEquals(creator,
                repo.creatorRepository
                        .findByName(creator.getName())
                        .get(0));
    }

    @Test
    @Transactional
    public void test4whenFindByIdGenre_thenReturnGenre() {
        assertEquals(genre,
                repo.genreRepository.findById(genre.getId())
                        .get()
        );
        assertEquals(staff,
                repo.genreRepository.findById(genre.getId())
                        .get().getLastChanger()
        );
    }

    @Test
    @Transactional
    public void test4whenFindByIdGenreEager_thenReturnGenre() {
        assertEquals(genre,
                repo.genreRepository
                        .findByIdEager(genre.getId())
                        .get());
        assertEquals(repo.genreRepository
                        .findByIdEager(genre.getId())
                        .get().getLastChanger(),
                staff);
    }

    @Test
    @Transactional
    public void test4whenFindByStaffIdGenre_thenReturnGenre() {
        assertEquals(genre,
                repo.genreRepository
                        .findByStaffId(staff.getId())
                        .get(0));
    }


    @Test
    @Transactional
    public void test5whenFindByIdAgeRating_thenReturnAgeRating() {
        assertEquals(ageRating,
                repo.ageRatingRepository.findById(ageRating.getId())
                        .get()
        );

        assertEquals(staff,
                repo.ageRatingRepository.findById(ageRating.getId())
                        .get().getLastChanger()
        );
    }

    @Test
    @Transactional
    public void test5whenFindByIdAgeRatingEager_thenReturnAgeRating() {
        AgeRating eager = repo.ageRatingRepository
                .findByIdEager(ageRating.getId())
                .get();
        assertEquals(ageRating,
                eager);
        assertEquals(staff,
                eager.getLastChanger());
    }

    @Test
    @Transactional
    public void test6whenFindByIdProduct_thenReturnProduct() {
        assertEquals(product,
                repo.productRepository.findById(product.getId()).get()
        );
        assertEquals(creator,
                repo.productRepository.findById(product.getId())
                        .get()
                        .getCreator()
        );

        assertEquals(ageRating,
                repo.productRepository.findById(product.getId())
                        .get()
                        .getAgeRating()
        );
    }

    @Test
    @Transactional
    public void test6whenFindByIdProductEager_thenReturnProduct() {
        Product eager = repo.productRepository
                .findByIdEager(product.getId())
                .get();
        assertEquals(product,
                eager
        );
        assertEquals(creator,
                eager.getCreator()
        );

        assertEquals(ageRating,
                eager.getAgeRating()
        );
    }

    @Test
    @Transactional
    public void test7whenFindByIdPurchase_thenReturnPurchase() {
        assertEquals(purchase,
                repo.purchaseRepository.findById(purchase.getId()).get());

        assertEquals(user,
                repo.purchaseRepository.findById(purchase.getId())
                        .get()
                        .getBuyer());

        assertEquals(product,
                repo.purchaseRepository.findById(purchase.getId())
                        .get()
                        .getProduct());

    }

    @Test
    @Transactional
    public void test7whenFindByIdPurchaseEager_thenReturnPurchase() {
        Purchase eager = repo.purchaseRepository
                .findByIdEager(purchase.getId())
                .get();

        assertEquals(purchase,
                eager);

        assertEquals(user,
                eager.getBuyer());

        assertEquals(product,
                eager.getProduct());

    }

    @Test
    @Transactional
    public void test7whenFindByProductNamePurchase_thenReturnPurchase() {
        Purchase eagerByProductName = repo.purchaseRepository
                .findByProductName(product.getName())
                .get(0);

        assertEquals(purchase,
                eagerByProductName);

        assertEquals(user,
                eagerByProductName.getBuyer());

        assertEquals(product,
                eagerByProductName.getProduct());

    }

    @Test
    @Transactional
    public void test7whenFindByDatePurchase_thenReturnPurchase() {
        Purchase purchaseByDate = repo.purchaseRepository
                .findByPurchaseDateInPeriod(
                        purchase.getPurchaseDate().minusSeconds(1000L),
                        purchase.getPurchaseDate().plusSeconds(1000L)
                ).get(0);
        assertEquals(
                purchase,
                purchaseByDate
        );
    }

}
