package org.uhanov;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.uhanov.conroller.UserController;
import org.uhanov.dto.UserAuthDTO;
import org.uhanov.dto.UserFullDTO;
import org.uhanov.dto.mapper.UserMapper;
import org.uhanov.exception.MoneyTransferException;
import org.uhanov.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Application {
    static final Object lock = new Object();

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext("org.uhanov");

        List<Thread> lst = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            lst.add(
                    new Thread(
                            () -> testUser(context, 600)
                    ));
        }

        lst.forEach(Thread::start);

        while (lst.stream().map(t -> t.isAlive()).reduce((t, t1) -> t || t1).get())

            //В процессе должно выкинуться исключение
            testUser(context, 1100);
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
                .id(UUID.randomUUID())
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

}
