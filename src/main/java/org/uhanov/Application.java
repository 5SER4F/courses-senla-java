package org.uhanov;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.uhanov.conroller.*;
import org.uhanov.dto.*;
import org.uhanov.dto.mapper.*;
import org.uhanov.model.*;
import org.uhanov.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Application {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("org.uhanov");

        testAgeRating(context);

        testCreator(context);

        testGenre(context);

        testProduct(context);

        testPurchase(context);

        testStaff(context);

        testUser(context);

    }

    private static void testUser(ApplicationContext context) {
        UserController controller = context.getBean(UserController.class);
        UserMapper mapper = context.getBean(UserMapper.class);
        User entity1 = User.builder()
                .id(UUID.randomUUID())
                .password("secret123")
                .firstname("Alice")
                .surname("Wonderland")
                .nickname("WonderAlice")
                .birthDate(LocalDate.now().minusDays(10))
                .registrationDate(LocalDateTime.now())
                .country("Wonderland")
                .build();

        User entity2 = User.builder()
                .id(UUID.randomUUID())
                .password("password456")
                .firstname("Bob")
                .surname("Builder")
                .nickname("BobTheBuilder")
                .birthDate(LocalDate.now().minusDays(10))
                .registrationDate(LocalDateTime.now().minusSeconds(1000 * 60 * 60 * 24))
                .country("USA")
                .build();

        UserAuthDTO patch = UserAuthDTO.builder()
                .id(entity1.getId())
                .password("UPDATE_PASSWORD")
                .firstname("UPDATE_NAME")
                .surname("UPDATE_SURNAME")
                .nickname("UPDATE_NICKNAME")
                .country("UPDATE_COUNTRY")
                .build();

        System.out.println();
        System.out.println("Start test " + controller.getClass().getName());

        System.out.println("Add first " + controller.add(mapper.toAuthDto(entity1)));
        System.out.println("Add second " + controller.add(mapper.toAuthDto(entity2)));

        System.out.println("Read first " + controller.get(entity1.getId()));

        System.out.println("Delete Second " + controller.delete(entity2.getId()));


        controller.update(patch);

        System.out.println("Update First" + controller.get(entity1.getId()));

        System.out.println("All the remaining" +
                context.getBean(UserRepositoryMock.class).getAll()
        );

        System.out.println();
    }

    private static void testStaff(ApplicationContext context) {
        StaffController controller = context.getBean(StaffController.class);
        StaffMapper mapper = context.getBean(StaffMapper.class);
        Staff entity1 = Staff.builder()
                .id(UUID.randomUUID())
                .password("password123")
                .firstname("John")
                .surname("Doe")
                .birthDate(LocalDate.now().minusDays(10))
                .registrationDate(LocalDateTime.now())
                .build();

        Staff entity2 = Staff.builder()
                .id(UUID.randomUUID())
                .password("secret123")
                .firstname("Jane")
                .surname("Smith")
                .birthDate(LocalDate.now().minusDays(10))
                .registrationDate(LocalDateTime.now().minusSeconds(1000 * 60 * 60 * 24))
                .build();

        StaffAuthDTO patch = StaffAuthDTO.builder()
                .id(entity1.getId())
                .firstname("NEW_NAME")
                .surname("NEW_SURNAME")
                .build();

        System.out.println();
        System.out.println("Start test " + controller.getClass().getName());

        System.out.println("Add first " + controller.add(mapper.toAuthDto(entity1)));
        System.out.println("Add second " + controller.add(mapper.toAuthDto(entity2)));

        System.out.println("Read first " + controller.get(entity1.getId()));

        System.out.println("Delete Second " + controller.delete(entity2.getId()));


        controller.update(patch);

        System.out.println("Update First" + controller.get(entity1.getId()));

        System.out.println("All the remaining" +
                context.getBean(StaffRepositoryMock.class).getAll()
        );

        System.out.println();
    }

    private static void testPurchase(ApplicationContext context) {
        PurchaseController controller = context.getBean(PurchaseController.class);
        PurchaseMapper mapper = context.getBean(PurchaseMapper.class);
        Purchase entity1 = Purchase.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .cost(15.99)
                .purchaseDate(LocalDateTime.now())
                .build();

        Purchase entity2 = Purchase.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .cost(29.99)
                .purchaseDate(LocalDateTime.now())
                .build();

        PurchaseDTO patch = PurchaseDTO.builder()
                .id(entity1.getId())
                .cost(666666.99)
                .purchaseDate(LocalDateTime.now().minusSeconds(1000 * 60 * 60 * 24))
                .build();

        System.out.println();
        System.out.println("Start test " + controller.getClass().getName());

        System.out.println("Add first " + controller.add(mapper.toDto(entity1)));
        System.out.println("Add second " + controller.add(mapper.toDto(entity2)));

        System.out.println("Read first " + controller.get(entity1.getId()));

        System.out.println("Delete Second " + controller.delete(entity2.getId()));


        controller.update(patch);

        System.out.println("Update First" + controller.get(entity1.getId()));

        System.out.println("All the remaining" +
                context.getBean(PurchaseRepositoryMock.class).getAll()
        );

        System.out.println();
    }

    private static void testProduct(ApplicationContext context) {
        ProductController controller = context.getBean(ProductController.class);
        ProductMapper mapper = context.getBean(ProductMapper.class);
        Product entity1 = Product.builder()
                .id(UUID.randomUUID())
                .creatorId(UUID.randomUUID())
                .name("Product 1")
                .dateAdded(LocalDateTime.now())
                .ageRatingId(UUID.randomUUID())
                .price(10.99)
                .build();

        Product entity2 = Product.builder()
                .id(UUID.randomUUID())
                .creatorId(UUID.randomUUID())
                .name("Product 2")
                .dateAdded(LocalDateTime.now())
                .ageRatingId(UUID.randomUUID())
                .price(19.99)
                .build();

        ProductDTO patch = ProductDTO.builder()
                .id(entity1.getId())
                .name("UPDATE_NAME")
                .price(99999999.99)
                .build();

        System.out.println();
        System.out.println("Start test " + controller.getClass().getName());

        System.out.println("Add first " + controller.add(mapper.toDto(entity1)));
        System.out.println("Add second " + controller.add(mapper.toDto(entity2)));

        System.out.println("Read first " + controller.get(entity1.getId()));

        System.out.println("Delete Second " + controller.delete(entity2.getId()));


        controller.update(patch);

        System.out.println("Update First" + controller.get(entity1.getId()));

        System.out.println("All the remaining" +
                context.getBean(ProductRepositoryMock.class).getAll()
        );

        System.out.println();
    }

    private static void testGenre(ApplicationContext context) {
        GenreController controller = context.getBean(GenreController.class);
        GenreMapper mapper = context.getBean(GenreMapper.class);
        Genre entity1 = Genre.builder()
                .id(UUID.randomUUID())
                .name("Genre 1")
                .lastChanger(UUID.randomUUID())
                .build();

        Genre entity2 = Genre.builder()
                .id(UUID.randomUUID())
                .name("Genre 2")
                .lastChanger(UUID.randomUUID())
                .build();

        GenreDTO patch = GenreDTO.builder()
                .id(entity1.getId())
                .name("UPDATE NAME")
                .lastChanger(UUID.randomUUID())
                .build();

        System.out.println();
        System.out.println("Start test " + controller.getClass().getName());

        System.out.println("Add first " + controller.add(mapper.toDto(entity1)));
        System.out.println("Add second " + controller.add(mapper.toDto(entity2)));

        System.out.println("Read first " + controller.get(entity1.getId()));

        System.out.println("Delete Second " + controller.delete(entity2.getId()));


        controller.update(patch);

        System.out.println("Update First" + controller.get(entity1.getId()));

        System.out.println("All the remaining" +
                context.getBean(GenreRepositoryMock.class).getAll()
        );

        System.out.println();
    }

    private static void testCreator(ApplicationContext context) {
        CreatorController controller = context.getBean(CreatorController.class);
        CreatorMapper mapper = context.getBean(CreatorMapper.class);
        Creator entity1 = Creator.builder()
                .id(UUID.randomUUID())
                .password("password1")
                .name("User1")
                .registrationDate(LocalDateTime.now())
                .build();

        Creator entity2 = Creator.builder()
                .id(UUID.randomUUID())
                .password("password2")
                .name("User2")
                .registrationDate(LocalDateTime.now().minusSeconds(1000 * 60 * 60 * 24))
                .build();

        CreatorAuthDTO patch = CreatorAuthDTO.builder()
                .id(entity1.getId())
                .name("UPDATE_NAME")
                .password("UPDATE_PASSWORD")
                .build();

        System.out.println();
        System.out.println("Start test " + controller.getClass().getName());

        System.out.println("Add first " + controller.add(mapper.toPostDTO(entity1)));
        System.out.println("Add second " + controller.add(mapper.toPostDTO(entity2)));

        System.out.println("Read first " + controller.get(entity1.getId()));

        System.out.println("Delete Second " + controller.delete(entity2.getId()));


        controller.update(patch);

        System.out.println("Update First" + controller.get(entity1.getId()));

        System.out.println("All the remaining" +
                context.getBean(CreatorRepositoryMock.class).getAll()
        );

        System.out.println();


    }

    private static void testAgeRating(ApplicationContext context) {
        AgeRatingController controller = context.getBean(AgeRatingController.class);
        AgeRatingMapper mapper = context.getBean(AgeRatingMapper.class);

        AgeRating entity1 = AgeRating.builder()
                .id(UUID.randomUUID())
                .name("AgeRating1")
                .lastChanger(UUID.randomUUID())
                .build();
        AgeRating entity2 = AgeRating.builder()
                .id(UUID.randomUUID())
                .name("AgeRating2")
                .lastChanger(UUID.randomUUID())
                .build();
        AgeRatingDTO patch = AgeRatingDTO.builder()
                .id(entity1.getId())
                .name("UpdateName")
                .build();

        System.out.println();
        System.out.println("Start test " + controller.getClass().getName());

        System.out.println("Add first " + controller.add(mapper.toDto(entity1)));
        System.out.println("Add second " + controller.add(mapper.toDto(entity2)));

        System.out.println("Read first " + controller.get(entity1.getId()));

        System.out.println("Delete Second " + controller.delete(entity2.getId()));


        controller.update(patch);

        System.out.println("Update First" + controller.get(entity1.getId()));

        System.out.println("All the remaining" +
                context.getBean(AgeRatingRepositoryMock.class).getAll()
        );

        System.out.println();
    }
}
