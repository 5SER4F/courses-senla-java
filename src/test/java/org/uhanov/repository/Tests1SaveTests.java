package org.uhanov.repository;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.uhanov.config.TestConfig;
import org.uhanov.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)


public class Tests1SaveTests {
    @Autowired
    TestedRepositoryHolder repo;

    private static User user;
    static User addedUser;

    private static Staff staff;
    static Staff addedStaff;

    private static Creator creator;
    static Creator addedCreator;

    private static Genre genre;
    static Genre addedGenre;

    private static AgeRating ageRating;
    static AgeRating addedAgeRating;

    private static Product product;
    static Product addedProduct;

    private static Purchase purchase;
    static Purchase addedPurchase;

    @Test
    public void test1whenSaveUser_thenReturnUserWithIdAndSameProperties() {
        user = User.builder()
                .password("secret123")
                .firstname("Alice")
                .surname("Wonderland")
                .nickname("WonderAlice")
                .birthDate(LocalDate.now().minusDays(10))
                .registrationDate(LocalDateTime.now())
                .country("Wonderland")
                .build();
        addedUser = repo.userRepository.save(user);

        user.setId(addedUser.getId());

        assertEquals(user, addedUser);
    }

    @Test
    public void test2whenSaveStaff_thenReturnStaffWithIdAndSameProperties() {
        staff = Staff.builder()
                .password("password123")
                .firstname("John")
                .surname("Doe")
                .birthDate(LocalDate.now().minusDays(10))
                .registrationDate(LocalDateTime.now())
                .build();

        addedStaff = repo.staffRepository.save(staff);

        staff.setId(addedStaff.getId());

        assertEquals(staff, addedStaff);
    }

    @Test
    public void test3whenSaveCreator_thenReturnCreatorWithIdAndSameProperties() {
        creator = Creator.builder()
                .password("password1")
                .name("User1")
                .registrationDate(LocalDateTime.now())
                .build();

        addedCreator = repo.creatorRepository.save(creator);

        creator.setId(addedCreator.getId());

        assertEquals(creator, addedCreator);
    }

    @Test
    public void test4whenSaveGenre_thenReturnGenreWithIdAndSameProperties() {
        genre = Genre.builder()
                .name("Genre 1")
                .lastChanger(addedStaff)
                .build();

        addedGenre = repo.genreRepository.save(genre);

        genre.setId(addedGenre.getId());

        assertEquals(genre, addedGenre);

        assertEquals(addedGenre.getLastChanger(), addedStaff);
    }


    @Test
    public void test5whenAgeRating_thenReturnAgeRatingWithIdAndSameProperties() {
        ageRating = AgeRating.builder()
                .name("AgeRating1")
                .lastChanger(addedStaff)
                .build();

        addedAgeRating = repo.ageRatingRepository.save(ageRating);

        ageRating.setId(addedAgeRating.getId());

        assertEquals(ageRating, addedAgeRating);
        assertEquals(addedAgeRating.getLastChanger(), addedStaff);
    }

    @Test
    public void test6whenProduct_thenReturnProductWithIdAndSameProperties() {
        product = Product.builder()
                .creator(addedCreator)
                .name("Product 1")
                .dateAdded(LocalDate.now())
                .ageRating(addedAgeRating)
                .price(BigDecimal.valueOf(10.99))
                .genres(Set.of(addedGenre))
                .build();

        addedProduct = repo.productRepository.save(product);

        product.setId(addedProduct.getId());

        assertEquals(product, addedProduct);
        assertEquals(Set.of(addedGenre), addedProduct.getGenres());
    }

    @Test
    public void test7whenPurchase_thenPurchaseUserWithIdAndSameProperties() {
        purchase = Purchase.builder()
                .buyer(addedUser)
                .product(addedProduct)
                .cost(BigDecimal.valueOf(15.99))
                .purchaseDate(LocalDateTime.now())
                .build();

        addedPurchase = repo.purchaseRepository.save(purchase);

        purchase.setId(addedPurchase.getId());

        assertEquals(purchase, addedPurchase);

        assertEquals(addedPurchase.getBuyer(), addedUser);
        assertEquals(addedPurchase.getProduct(), addedProduct);
    }

}
