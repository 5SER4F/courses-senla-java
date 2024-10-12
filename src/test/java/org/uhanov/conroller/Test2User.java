package org.uhanov.conroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.uhanov.WebAppInitializerTestConfig;
import org.uhanov.dto.user.MoneyTransferDto;
import org.uhanov.dto.user.UserAuthDto;
import org.uhanov.dto.user.UserFullDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringJUnitWebConfig(value = WebAppInitializerTestConfig.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test2User {
    @Autowired
    public WebApplicationContext wac;
    @Autowired
    public ObjectMapper objectMapper;
    public static MockMvc mvc;

    private static UserFullDto addedUser;
    private static UserFullDto recipientUser;

    public static final String PATH_PREFIX = "/users";

    private static final Double BALANCE_UPDATE = 1000.0;


    @BeforeEach
    public void init() {
        mvc = MockMvcBuilders.webAppContextSetup(wac)
                .build();

    }

    @Test
    @Order(1)
    public void whenCreate_ThenReturn201AndSameUserWithId() {
        UserAuthDto userAuthDto = UserAuthDto.builder()
                .password("password")
                .firstname("John")
                .surname("Doe")
                .nickname("johndoe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .registrationDate(LocalDateTime.now())
                .country("USA")
                .build();

        try {
            MvcResult result = createUser(userAuthDto);

            assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());

            addedUser = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    UserFullDto.class
            );

            System.out.println(addedUser);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        assertNotNull(addedUser.getId());

        assertEquals(addedUser.getFirstname(), userAuthDto.getFirstname());
        assertEquals(addedUser.getSurname(), userAuthDto.getSurname());
        assertEquals(addedUser.getBirthDate(), userAuthDto.getBirthDate());
        assertEquals(addedUser.getNickname(), userAuthDto.getNickname());
    }

    @Test
    @Order(2)
    public void whenUpdate_thenReturn200() {
        try {
            MvcResult result = mvc.perform(
                    MockMvcRequestBuilders.patch(
                                    PATH_PREFIX + "/" + addedUser.getId()
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(UserAuthDto.builder().balance(BALANCE_UPDATE)
                                    .build()))
            ).andReturn();
            assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }


    }


    @Test
    @Order(3)
    public void whenGet_ThenReturn201AndUserWithPassedId() {
        try {
            MvcResult result = getUser(addedUser.getId());
            assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());

            UserFullDto getUser = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    UserFullDto.class
            );

            System.out.println("PPPPP" + getUser);

            addedUser.setBalance(getUser.getBalance());

            assertEquals(addedUser.getId(), getUser.getId());

            assertEquals(addedUser.getFirstname(), getUser.getFirstname());
            assertEquals(addedUser.getSurname(), getUser.getSurname());
            assertEquals(addedUser.getBirthDate(), getUser.getBirthDate());
            assertEquals(BALANCE_UPDATE, getUser.getBalance());
            assertEquals(addedUser.getNickname(), getUser.getNickname());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @Order(4)
    public void whenMoneyTransfer_ThenReturn204AndTransferMoney() {
        try {
            UserAuthDto recipientAuthDto = UserAuthDto.builder()
                    .password("12345")
                    .firstname("recipient")
                    .surname("Jackson")
                    .nickname("qwerty")
                    .birthDate(LocalDate.of(1990, 1, 1))
                    .registrationDate(LocalDateTime.now())
                    .country("USA")
                    .build();
            recipientUser = objectMapper.readValue(
                    createUser(recipientAuthDto).getResponse().getContentAsString(),
                    UserFullDto.class
            );
            MoneyTransferDto moneyTransferDto = MoneyTransferDto.builder()
                    .recipientId(recipientUser.getId())
                    .amount(600)
                    .build();
            MvcResult transferResult = mvc.perform(
                    MockMvcRequestBuilders.patch(PATH_PREFIX + "/" + addedUser.getId() + "/transfer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(moneyTransferDto))
            ).andReturn();
            assertEquals(
                    HttpStatus.NO_CONTENT.value(),
                    transferResult.getResponse().getStatus()
            );

            addedUser = objectMapper.readValue(
                    getUser(addedUser.getId()).getResponse().getContentAsString(),
                    UserFullDto.class
            );
            recipientUser = objectMapper.readValue(
                    getUser(recipientUser.getId()).getResponse().getContentAsString(),
                    UserFullDto.class
            );

            assertEquals(400.0, addedUser.getBalance());

            assertEquals(600.0, recipientUser.getBalance());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @Order(5)
    public void whenDelete_theReturn204After404() {
        try {
            MvcResult deleteResult = mvc.perform(
                    MockMvcRequestBuilders.delete(PATH_PREFIX + "/" + recipientUser.getId())
                            .accept(MediaType.APPLICATION_JSON)
            ).andReturn();
            assertEquals(HttpStatus.NO_CONTENT.value(), deleteResult.getResponse().getStatus());

            MvcResult getWithExceptionResult = mvc.perform(
                            MockMvcRequestBuilders.get(PATH_PREFIX +
                                            "/" + recipientUser.getId())
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


    public MvcResult createUser(UserAuthDto dto) throws Exception {
        return mvc.perform(
                        MockMvcRequestBuilders.post(PATH_PREFIX)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andReturn();

    }

    private MvcResult getUser(UUID uuid) throws Exception {
        return mvc.perform(
                        MockMvcRequestBuilders.get(PATH_PREFIX +
                                        "/" + uuid)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
    }
}





