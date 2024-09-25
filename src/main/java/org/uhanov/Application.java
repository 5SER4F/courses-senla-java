package org.uhanov;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.uhanov.conroller.PurchaseController;
import org.uhanov.conroller.StaffController;
import org.uhanov.conroller.UserController;
import org.uhanov.dto.PurchaseDTO;
import org.uhanov.dto.StaffAuthDTO;
import org.uhanov.dto.UserAuthDTO;
import org.uhanov.dto.UserFullDTO;
import org.uhanov.dto.mapper.PurchaseMapper;
import org.uhanov.dto.mapper.StaffMapper;
import org.uhanov.dto.mapper.UserMapper;
import org.uhanov.exception.MoneyTransferException;
import org.uhanov.model.Purchase;
import org.uhanov.model.Staff;
import org.uhanov.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@ComponentScan("org.uhanov")
public class Application {
    static final Object lock = new Object();

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext("org.uhanov");
        System.out.println();

//            //В процессе должно выкинуться исключение
//            testUser(context, 1100);
//        testUser(context, 600);

//        testStaff(context);
        context.close();

    }

    private static void testUser(ApplicationContext context, double amount) {
        UserController controller = context.getBean(UserController.class);
        UserMapper mapper = context.getBean(UserMapper.class);
        ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
        UserFullDTO addedUser, addedUser2;
        User entity1 = User.builder()
                .password("secret123")
                .firstname("Alice")
                .surname("Wonderland")
                .nickname("WonderAlice")
                .birthDate(LocalDate.now().minusDays(10))
                .registrationDate(LocalDateTime.now())
                .country("Wonderland")
                .build();
        User entity2 = User.builder()
                .password("password456")
                .firstname("Bob")
                .surname("Builder")
                .nickname("BobTheBuilder")
                .birthDate(LocalDate.now().minusDays(10))
                .registrationDate(LocalDateTime.now().minusSeconds(1000 * 60 * 60 * 24))
                .country("USA")
                .build();
        try {
            addedUser = objectMapper.readValue(controller.add(mapper.toAuthDto(entity1)).toString(), UserFullDTO.class);
            addedUser2 = objectMapper.readValue(controller.add(mapper.toAuthDto(entity2)).toString(), UserFullDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        UserAuthDTO patch = UserAuthDTO.builder()
                .id(addedUser.getId())
                .password("UPDATE_PASSWORD")
                .firstname("UPDATE_NAME")
                .balance(1000d)
                .surname("UPDATE_SURNAME")
                .nickname("UPDATE_NICKNAME")
                .country("UPDATE_COUNTRY")
                .build();

        System.out.println("Added user1=" + addedUser);
        System.out.println("Added user2=" + addedUser);

        controller.update(patch);

        System.out.println(
                "After update user1=" + controller.get(addedUser.getId())
        );

        try {
            controller.moneyTransfer(addedUser.getId(), addedUser2.getId(), amount);
        } catch (MoneyTransferException e) {
            e.printStackTrace();
        }

        System.out.println("User1 after money transfer=" + controller.get(addedUser.getId()));
        System.out.println("User2 after money transfer=" + controller.get(addedUser2.getId()));

        controller.delete(addedUser.getId());
        controller.delete(addedUser2.getId());

    }
    private static void testStaff(ApplicationContext context) {
        StaffController controller = context.getBean(StaffController.class);
        StaffMapper mapper = context.getBean(StaffMapper.class);
        StaffAuthDTO added1, added2;
        ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
        Staff entity1 = Staff.builder()
                .password("password123")
                .firstname("John")
                .surname("Doe")
                .birthDate(LocalDate.now().minusDays(10))
                .registrationDate(LocalDateTime.now())
                .build();

        Staff entity2 = Staff.builder()
                .password("secret123")
                .firstname("Jane")
                .surname("Smith")
                .birthDate(LocalDate.now().minusDays(10))
                .registrationDate(LocalDateTime.now().minusSeconds(1000 * 60 * 60 * 24))
                .build();

        StaffAuthDTO patch = StaffAuthDTO.builder()
                .firstname("NEW_NAME")
                .surname("NEW_SURNAME")
                .build();

        System.out.println();
        System.out.println("Start test " + controller.getClass().getName());

        try {
            added1 = objectMapper.readValue(controller.add(mapper.toAuthDto(entity1)).toString(), StaffAuthDTO.class);
            added2 = objectMapper.readValue(controller.add(mapper.toAuthDto(entity2)).toString(), StaffAuthDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Add first " + controller.add(mapper.toAuthDto(entity1)));
        System.out.println("Add second " + controller.add(mapper.toAuthDto(entity2)));

        System.out.println("Read first " + controller.get(added1.getId()));

        System.out.println("Delete Second " + controller.delete(added2.getId()));

        patch.setId(added1.getId());
        controller.update(patch);

        System.out.println("Update First" + controller.get(added1.getId()));

        System.out.println("Delete First " + controller.delete(added1.getId()));

        System.out.println();
    }

}
