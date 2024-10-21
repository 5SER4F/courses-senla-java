package org.uhanov.conroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
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
import org.uhanov.dto.SignInDto;
import org.uhanov.dto.creator.CreatorDto;
import org.uhanov.dto.creator.CreatorSignUpDto;
import org.uhanov.security.JwtAuthenticationFilter;
import org.uhanov.security.Role;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringJUnitWebConfig(value = WebAppInitializerTestConfig.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test6Creator {
    @Autowired
    public WebApplicationContext wac;
    @Autowired
    public ObjectMapper objectMapper;
    public static MockMvc mvc;

    public static CreatorDto addedCreator;
    public static String addedCreatorToken;
    public static String ADDED_CREATOR_PASSWORD = "password123";

    public static final String PATH_PREFIX = "/creators";

    @BeforeEach
    public void init() {
        mvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

    }

    @Test
    @Order(1)
    public void whenCreate_ThenReturn201AndSameCreatorWithId() {
        CreatorSignUpDto creatorSignUpDto = CreatorSignUpDto.builder()
                .password(ADDED_CREATOR_PASSWORD)
                .name("CreatorName")
                .registrationDate(LocalDateTime.now())
                .build();
        try {
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post(PATH_PREFIX)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(creatorSignUpDto)))
                    .andReturn();

            assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());

            addedCreator = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    CreatorDto.class
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        assertNotNull(addedCreator.getId());

        assertEquals(addedCreator.getName(), creatorSignUpDto.getName());
    }

    @Test
    @Order(2)
    public void whenSignIn_thenReturn200() {
        try {
            SignInDto signInDto = SignInDto.builder()
                    .username(addedCreator.getName())
                    .password(ADDED_CREATOR_PASSWORD)
                    .role(Role.CREATOR.name())
                    .build();

            MvcResult result = mvc.perform(
                    MockMvcRequestBuilders.post(
                                    PATH_PREFIX + "/" + "/login"
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(signInDto))
            ).andReturn();
            assertEquals(200, result.getResponse().getStatus());

            addedCreatorToken = preToken(result.getResponse().getContentAsString());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @Order(3)
    public void whenUpdate_thenReturn200() {
        try {
            LocalDateTime newReg = LocalDateTime.now().plusDays(10);
            MvcResult result = mvc.perform(
                    MockMvcRequestBuilders.patch(
                                    PATH_PREFIX + "/" + addedCreator.getId()
                            )
                            .header(JwtAuthenticationFilter.HEADER_NAME, addedCreatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(CreatorSignUpDto.builder()
                                    .registrationDate(newReg)
                                    .build()))
            ).andReturn();
            addedCreator.setRegistrationDate(newReg);
            assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @Order(3)
    public void whenGet_ThenReturn201AndCreatorWithPassedId() {
        try {
            MvcResult result = mvc.perform(
                            MockMvcRequestBuilders.get(PATH_PREFIX + "/" + addedCreator.getId()
                                    )
                                    .accept(MediaType.APPLICATION_JSON))
                    .andReturn();

            CreatorDto getCreator = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    CreatorDto.class
            );
            assertEquals(addedCreator.getId(), getCreator.getId());
            assertEquals(addedCreator.getName(), getCreator.getName());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @Order(4)
    public void whenDelete_thenReturn204After404() {
        try {
            CreatorSignUpDto creatorSignUpDto = CreatorSignUpDto.builder()
                    .password("password123")
                    .name("toDelete")
                    .registrationDate(LocalDateTime.now())
                    .build();
            MvcResult toDelete = mvc.perform(MockMvcRequestBuilders.post(PATH_PREFIX)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(creatorSignUpDto)))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andReturn();

            CreatorDto toDeleteDto = objectMapper.readValue(
                    toDelete.getResponse().getContentAsString(),
                    CreatorDto.class
            );

            SignInDto signInDto = SignInDto.builder()
                    .username(toDeleteDto.getName())
                    .password(creatorSignUpDto.getPassword())
                    .role(Role.CREATOR.name())
                    .build();

            MvcResult result = mvc.perform(
                    MockMvcRequestBuilders.post(
                                    PATH_PREFIX + "/" + "/login"
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(signInDto))
            ).andReturn();
            assertEquals(200, result.getResponse().getStatus());

            String toDeleteToken = preToken(result.getResponse().getContentAsString());


            MvcResult deleteResult = mvc.perform(
                            MockMvcRequestBuilders.delete(PATH_PREFIX + "/" +
                                            toDeleteDto.getId())
                                    .header(JwtAuthenticationFilter.HEADER_NAME, toDeleteToken)
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(MockMvcResultMatchers.status().isNoContent())
                    .andReturn();

            MvcResult afterDelete = mvc.perform(
                            MockMvcRequestBuilders.get(PATH_PREFIX + "/" +
                                            objectMapper.readValue(toDelete.getResponse().getContentAsString(),
                                                    CreatorDto.class).getId()
                                    )
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    private String preToken(String token) {
        token = token.substring(
                token.indexOf(":") + 2,
                token.length() - 2
        );
        return JwtAuthenticationFilter.BEARER_PREFIX + token;

    }

}
