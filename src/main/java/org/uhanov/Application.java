package org.uhanov;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.uhanov.conroller.*;
import org.uhanov.dto.*;
import org.uhanov.dto.mapper.*;
import org.uhanov.model.*;
import org.uhanov.test.SimpleTest;

import java.sql.Timestamp;
import java.util.UUID;

public class Application {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("org.uhanov");

        testAgeRating(context.getBean(AgeRatingController.class), context.getBean(AgeRatingMapper.class));

        testCreator(context.getBean(CreatorController.class), context.getBean(CreatorMapper.class));

        testGenre(context.getBean(GenreController.class), context.getBean(GenreMapper.class));

        testProduct(context.getBean(ProductController.class), context.getBean(ProductMapper.class));

        testPurchase(context.getBean(PurchaseController.class), context.getBean(PurchaseMapper.class));

        testStaff(context.getBean(StaffController.class), context.getBean(StaffMapper.class));

        testUser(context.getBean(UserController.class), context.getBean(UserMapper.class));

    }

    private static void testUser(CRUDController<UserDTO> controller, UserMapper mapper) {
        User user1 = User.builder()
                .id(UUID.randomUUID())
                .password("secret123")
                .firstname("Alice")
                .surname("Wonderland")
                .nickname("WonderAlice")
                .birthDate(new Timestamp(System.currentTimeMillis() - (1000L * 60 * 60 * 24 * 365 * 20)))
                .registrationDate(new Timestamp(System.currentTimeMillis()))
                .country("Wonderland")
                .build();

        User user2 = User.builder()
                .id(UUID.randomUUID())
                .password("password456")
                .firstname("Bob")
                .surname("Builder")
                .nickname("BobTheBuilder")
                .birthDate(new Timestamp(System.currentTimeMillis() - (1000L * 60 * 60 * 24 * 365 * 28)))
                .registrationDate(new Timestamp(System.currentTimeMillis() - (1000L * 60 * 60 * 24 * 30)))
                .country("USA")
                .build();

        UserDTO patch = UserDTO.builder()
                .id(user1.getId())
                .password("UPDATE_PASSWORD")
                .firstname("UPDATE_NAME")
                .surname("UPDATE_SURNAME")
                .nickname("UPDATE_NICKNAME")
                .country("UPDATE_COUNTRY")
                .build();

        new SimpleTest<>(controller, mapper::toDto,
                user1, user2, patch).doTest();
    }

    private static void testStaff(CRUDController<StaffDTO> controller, StaffMapper mapper) {
        Staff staff1 = Staff.builder()
                .id(UUID.randomUUID())
                .password("password123")
                .firstname("John")
                .surname("Doe")
                .birthDate(new Timestamp(System.currentTimeMillis() - (1000L * 60 * 60 * 24 * 365 * 25)))
                .registrationDate(new Timestamp(System.currentTimeMillis()))
                .build();

        Staff staff2 = Staff.builder()
                .id(UUID.randomUUID())
                .password("secret123")
                .firstname("Jane")
                .surname("Smith")
                .birthDate(new Timestamp(System.currentTimeMillis() - (1000L * 60 * 60 * 24 * 365 * 30)))
                .registrationDate(new Timestamp(System.currentTimeMillis() - (1000L * 60 * 60 * 24 * 180)))
                .build();

        StaffDTO patch = StaffDTO.builder()
                .id(staff1.getId())
                .password("UPDATE_PASSWORD")
                .firstname("NEW_NAME")
                .surname("NEW_SURNAME")
                .build();

        new SimpleTest<>(controller, mapper::toDto,
                staff1, staff2, patch).doTest();
    }

    private static void testPurchase(CRUDController<PurchaseDTO> controller, PurchaseMapper mapper) {
        Purchase purchase1 = Purchase.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .cost(15.99)
                .purchaseDate(new Timestamp(System.currentTimeMillis()))
                .build();

        Purchase purchase2 = Purchase.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .cost(29.99)
                .purchaseDate(new Timestamp(System.currentTimeMillis()))
                .build();

        PurchaseDTO patch = PurchaseDTO.builder()
                .id(purchase1.getId())
                .cost(666666.99)
                .purchaseDate(new Timestamp(System.currentTimeMillis() - (1000 * 60 * 60 * 24)))
                .build();

        new SimpleTest<>(controller, mapper::toDto,
                purchase1, purchase2, patch).doTest();
    }

    private static void testProduct(CRUDController<ProductDTO> controller, ProductMapper mapper) {
        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .creatorId(UUID.randomUUID())
                .name("Product 1")
                .dateAdded(new Timestamp(System.currentTimeMillis()))
                .ageRatingId(UUID.randomUUID())
                .price(10.99)
                .build();

        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .creatorId(UUID.randomUUID())
                .name("Product 2")
                .dateAdded(new Timestamp(System.currentTimeMillis()))
                .ageRatingId(UUID.randomUUID())
                .price(19.99)
                .build();

        ProductDTO patch = ProductDTO.builder()
                .id(product1.getId())
                .name("UPDATE_NAME")
                .price(99999999.99)
                .build();

        new SimpleTest<>(controller, mapper::toDto,
                product1, product2, patch).doTest();
    }

    private static void testGenre(CRUDController<GenreDTO> controller, GenreMapper mapper) {
        Genre genre1 = Genre.builder()
                .id(UUID.randomUUID())
                .name("Genre 1")
                .lastChanger(UUID.randomUUID())
                .build();

        Genre genre2 = Genre.builder()
                .id(UUID.randomUUID())
                .name("Genre 2")
                .lastChanger(UUID.randomUUID())
                .build();

        GenreDTO patch = GenreDTO.builder()
                .id(genre1.getId())
                .name("UPDATE NAME")
                .lastChanger(UUID.randomUUID())
                .build();
        new SimpleTest<>(controller, mapper::toDto,
                genre1, genre2, patch).doTest();

    }

    private static void testCreator(CRUDController<CreatorDTO> controller, CreatorMapper mapper) {
        Creator creator1 = Creator.builder()
                .id(UUID.randomUUID())
                .password("password1")
                .name("User1")
                .registrationDate(new Timestamp(System.currentTimeMillis()))
                .build();

        Creator creator2 = Creator.builder()
                .id(UUID.randomUUID())
                .password("password2")
                .name("User2")
                .registrationDate(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60 * 24))
                .build();

        CreatorDTO patch = CreatorDTO.builder()
                .id(creator1.getId())
                .name("UPDATE_NAME")
                .password("UPDATE_PASSWORD")
                .build();
        new SimpleTest<>(controller, mapper::toDto,
                creator1, creator2, patch).doTest();
    }

    private static void testAgeRating(CRUDController<AgeRatingDTO> controller, AgeRatingMapper mapper) {
        AgeRating ageRating1 = AgeRating.builder()
                .id(UUID.randomUUID())
                .name("AgeRating1")
                .lastChanger(UUID.randomUUID())
                .build();
        AgeRating ageRating2 = AgeRating.builder()
                .id(UUID.randomUUID())
                .name("AgeRating2")
                .lastChanger(UUID.randomUUID())
                .build();
        AgeRatingDTO patch = AgeRatingDTO.builder()
                .id(ageRating1.getId())
                .name("UpdateName")
                .build();
        new SimpleTest<>(controller, mapper::toDto,
                ageRating1, ageRating2, patch).doTest();
    }
}
