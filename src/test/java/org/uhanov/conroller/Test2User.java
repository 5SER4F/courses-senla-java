package org.uhanov.conroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.uhanov.SecurityWebApplicationTestInitializer;
import org.uhanov.config.SecurityTestConfig;
import org.uhanov.dto.SignInDto;
import org.uhanov.dto.user.MoneyTransferDto;
import org.uhanov.dto.user.UserFullDto;
import org.uhanov.dto.user.UserSignUpDto;
import org.uhanov.security.JwtAuthenticationFilter;
import org.uhanov.security.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringJUnitWebConfig(value = SecurityWebApplicationTestInitializer.class)
@ContextConfiguration(classes = SecurityTestConfig.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test2User {
    @Autowired
    public WebApplicationContext wac;

    @Autowired
    public ObjectMapper objectMapper;

    //    @Autowired
    public static MockMvc mvc;

    private static UserFullDto addedUser;
    private static UserFullDto recipientUser;

    private static String addedUserToken;

    private static String recipientToken;

    private static final String ADDED_USER_PASSWORD = "password";

    public static final String PATH_PREFIX = "/users";

    private static final Double BALANCE_UPDATE = 1000.0;


    @BeforeEach
    public void init() {
        mvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

    }

    @Test
    @Order(1)
    public void whenCreate_ThenReturn201AndSameUserWithId() {
//        assertTrue(true);
        UserSignUpDto userSignUpDto = UserSignUpDto.builder()
                .password(ADDED_USER_PASSWORD)
                .firstname("John")
                .surname("Doe")
                .nickname("johndoe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .registrationDate(LocalDateTime.now())
                .country("USA")
                .build();

        try {
            MvcResult result = createUser(userSignUpDto);

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

        assertEquals(addedUser.getFirstname(), userSignUpDto.getFirstname());
        assertEquals(addedUser.getSurname(), userSignUpDto.getSurname());
        assertEquals(addedUser.getBirthDate(), userSignUpDto.getBirthDate());
        assertEquals(addedUser.getNickname(), userSignUpDto.getNickname());
    }

    @Test
    @Order(2)
    public void whenSignIn_thenReturnToken() {

        try {
            SignInDto signInDto = SignInDto.builder()
                    .username(addedUser.getNickname())
                    .password(ADDED_USER_PASSWORD)
                    .role(Role.USER.name())
                    .build();
            MvcResult result = mvc.perform(
                    MockMvcRequestBuilders.post(
                                    PATH_PREFIX + "/" + "/login"
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(signInDto))
            ).andReturn();

            assertEquals(200, result.getResponse().getStatus());
            
            

            addedUserToken = preToken(result.getResponse().getContentAsString());

            System.out.println("QQQQQQQQ" + addedUserToken);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }


        System.out.println("SERVLET CONTEXT\n" +
                wac.getBean(DefaultSecurityFilterChain.class).getFilters()
        );

        System.out.println("\n" +
                wac.getBeansOfType(SecurityFilterChain.class).values()
        );

        var context = SecurityContextHolder.getContext();

        System.out.println("CONTEXT=" + context);

    }

    @Test
    @Order(3)
    public void whenUpdate_thenReturn200() {
        try {
            System.out.println(
                    wac.getBean(JwtAuthenticationFilter.class)
            );

            MvcResult result = mvc.perform(
                            MockMvcRequestBuilders.patch(
                                            PATH_PREFIX + "/" + addedUser.getId()
                                    )
                                    .header(JwtAuthenticationFilter.HEADER_NAME, addedUserToken)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(UserSignUpDto.builder()
                                            .balance(BALANCE_UPDATE)
                                            .build()))
                    )
                    .andReturn();
            assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }



    @Test
    @Order(4)
    public void whenGet_ThenReturn201AndUserWithPassedId() {
        try {
            MvcResult result = getUser(addedUser.getId(), addedUserToken);
            assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());

            UserFullDto getUser = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    UserFullDto.class
            );

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
//
    @Test
    @Order(5)
    public void whenMoneyTransfer_ThenReturn204AndTransferMoney() {
        try {
            UserSignUpDto recipientAuthDto = UserSignUpDto.builder()
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
                            .header(JwtAuthenticationFilter.HEADER_NAME, addedUserToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(moneyTransferDto))
            ).andReturn();
            assertEquals(
                    HttpStatus.NO_CONTENT.value(),
                    transferResult.getResponse().getStatus()
            );

            addedUser = objectMapper.readValue(
                    getUser(addedUser.getId(), addedUserToken).getResponse().getContentAsString(),
                    UserFullDto.class
            );


            SignInDto signInDto = SignInDto.builder()
                    .username(recipientUser.getNickname())
                    .password(recipientAuthDto.getPassword())
                    .role(Role.USER.name())
                    .build();
            MvcResult result = mvc.perform(
                    MockMvcRequestBuilders.post(
                                    PATH_PREFIX + "/" + "/login"
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(signInDto))
            ).andReturn();
            assertEquals(200, result.getResponse().getStatus());



            recipientToken = preToken(result.getResponse().getContentAsString());


            recipientUser = objectMapper.readValue(
                    getUser(recipientUser.getId(), recipientToken).getResponse().getContentAsString(),
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
    @Order(6)
    public void whenDelete_theReturn204After404() {
        try {
            MvcResult deleteResult = mvc.perform(
                    MockMvcRequestBuilders.delete(PATH_PREFIX + "/" + recipientUser.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .header(JwtAuthenticationFilter.HEADER_NAME, recipientToken)
            ).andReturn();
            assertEquals(HttpStatus.NO_CONTENT.value(), deleteResult.getResponse().getStatus());

            MvcResult getWithExceptionResult = mvc.perform(
                            MockMvcRequestBuilders.get(PATH_PREFIX +
                                            "/" + recipientUser.getId())
                                    .accept(MediaType.APPLICATION_JSON)
                                    .header(JwtAuthenticationFilter.HEADER_NAME, recipientToken))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();
            ;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
//
//
    public MvcResult createUser(UserSignUpDto dto) throws Exception {
        return mvc.perform(
                        MockMvcRequestBuilders.post(PATH_PREFIX)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andReturn();

    }

    private MvcResult getUser(UUID uuid, String token) throws Exception {
        return mvc.perform(
                        MockMvcRequestBuilders.get(PATH_PREFIX +
                                        "/" + uuid)
                                .header(JwtAuthenticationFilter.HEADER_NAME, token)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
    }
    
    private String preToken(String token) {
        token = token.substring(
                token.indexOf(":") + 2,
                token.length() - 2
        );
        return JwtAuthenticationFilter.BEARER_PREFIX + token;

    }
}





