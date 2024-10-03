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
import org.uhanov.dto.*;
import org.uhanov.dto.mapper.*;
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

        userMapper.updateUser(patch, user);
        user = repo.userRepository.update(user);

        assertEquals(user,
                repo.userRepository.findById(user.getId()).get());
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

        staffMapper.updateStaff(patch, staff);
        staff = repo.staffRepository.update(staff);
        assertEquals(staff,
                repo.staffRepository.findById(staff.getId()).get());
    }

    @Test
    public void test3whenUpdateCreator_thenReturnUpdatedCreatorAndFindReturnSame() {
        CreatorAuthDTO patch = CreatorAuthDTO.builder()
                .password("UPDATEDpassword1")
                .name("UPDATEDUser1")
                .registrationDate(LocalDateTime.now())
                .build();

        creatorMapper.updateCreator(patch, creator);

        creator = repo.creatorRepository.update(creator);

        assertEquals(creator,
                repo.creatorRepository.findById(creator.getId()).get());
    }

    @Test
    public void test4whenUpdateGenre_thenReturnUpdatedGenreAndFindReturnSame() {
        GenreDTO patch = GenreDTO.builder()
                .name("UPDATEDGenre 1")
                .lastChanger(staff)
                .build();
        genreMapper.updateGenre(patch, genre);

        genre = repo.genreRepository.update(genre);

        assertEquals(genre,
                repo.genreRepository.findById(genre.getId()).get());

    }


    @Test
    public void test5whenUpdateAgeRating_thenReturnUpdatedAgeRatingAndFindReturnSame() {
        AgeRatingDTO patch = AgeRatingDTO.builder()
                .name("UPDATEDAgeRating1")
                .lastChanger(staff)
                .build();

        ageRatingMapper.updateAgeRating(patch, ageRating);

        ageRating = repo.ageRatingRepository.update(ageRating);

        assertEquals(ageRating,
                repo.ageRatingRepository.findById(ageRating.getId()).get());
    }

    @Test
    public void test6whenUpdateProduct_thenReturnUpdatedProductAndFindReturnSame() {
        ProductDTO patch = ProductDTO.builder()
                .name("UPDATEDProduct 1")
                .dateAdded(LocalDate.now())
                .ageRating(ageRating)
                .price(BigDecimal.valueOf(999.99))
                .build();

        productMapper.updateProduct(patch, product);

        product = repo.productRepository.update(product);

        assertEquals(product,
                repo.productRepository.findById(product.getId()).get());
    }

    @Test
    public void test7whenUpdatePurchase_thenReturnUpdatedPurchaseAndFindReturnSame() {
        PurchaseDTO patch = PurchaseDTO.builder()
                .cost(BigDecimal.valueOf(999.99))
                .purchaseDate(LocalDateTime.now())
                .build();

        purchaseMapper.updatePurchase(patch, purchase);

        purchase = repo.purchaseRepository.update(purchase);

        assertEquals(purchase,
                repo.purchaseRepository.findById(purchase.getId()).get());

    }
}
