package org.uhanov.conroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
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
import org.uhanov.dto.staff.StaffFullDto;
import org.uhanov.dto.staff.StaffSignUpDto;
import org.uhanov.security.JwtAuthenticationFilter;
import org.uhanov.security.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringJUnitWebConfig(value = SecurityWebApplicationTestInitializer.class)
@ContextConfiguration(classes = SecurityTestConfig.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test1Staff {
    @Autowired
    public WebApplicationContext wac;
    @Autowired
    public ObjectMapper objectMapper;
    public static MockMvc mvc;

    public static StaffFullDto addedStaff;

    public static String addedStaffToken;

    public static final String PATH_PREFIX = "/staff";
    public static final String ADDED_STAFF_PASSWORD = "password123";

    @BeforeEach
    public void init() {
        mvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

    }

    @Test
    @Order(1)
    public void whenCreate_ThenReturn201AndSameStaffWithId() {
        StaffSignUpDto staffSignUpDto = StaffSignUpDto.builder()
                .password(ADDED_STAFF_PASSWORD)
                .username("John")
                .surname("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .registrationDate(LocalDateTime.now())
                .build();
        try {
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post(PATH_PREFIX)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(staffSignUpDto)))
                    .andReturn();

            assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());

            addedStaff = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    StaffFullDto.class
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        assertNotNull(addedStaff.getId());

        assertEquals(addedStaff.getUsername(), staffSignUpDto.getUsername());
        assertEquals(addedStaff.getSurname(), staffSignUpDto.getSurname());
        assertEquals(addedStaff.getBirthDate(), staffSignUpDto.getBirthDate());
    }

    @Test
    @Order(2)
    public void whenSignIn_thenReturn200() {
        try {
            SignInDto signInDto = SignInDto.builder()
                    .username(addedStaff.getUsername())
                    .password(ADDED_STAFF_PASSWORD)
                    .role(Role.STAFF.name())
                    .build();

            MvcResult result = mvc.perform(
                    MockMvcRequestBuilders.post(
                                    PATH_PREFIX + "/" + "/login"
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(signInDto))
            ).andReturn();
            assertEquals(200, result.getResponse().getStatus());

            addedStaffToken = preToken(result.getResponse().getContentAsString());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @Order(3)
    public void whenUpdate_thenReturn200() {
        try {
            MvcResult result = mvc.perform(
                    MockMvcRequestBuilders.patch(
                                    PATH_PREFIX + "/" + addedStaff.getId()
                            )
                            .header(JwtAuthenticationFilter.HEADER_NAME, addedStaffToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(StaffSignUpDto.builder().surname("UpdateSurname")
                                    .build()))
            ).andReturn();
            addedStaff.setSurname("UpdateSurname");
            assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @Order(3)
    public void whenGet_ThenReturn201AndStaffWithPassedId() {
        try {
            MvcResult result = mvc.perform(
                            MockMvcRequestBuilders.get(PATH_PREFIX + "/" + addedStaff.getId()
                                    )
                                    .header(JwtAuthenticationFilter.HEADER_NAME, addedStaffToken)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andReturn();

            StaffFullDto getStaff = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    StaffFullDto.class
            );
            assertEquals(addedStaff.getId(), getStaff.getId());
            assertEquals(addedStaff.getUsername(), getStaff.getUsername());
            assertEquals(addedStaff.getSurname(), getStaff.getSurname());
            assertEquals(addedStaff.getBirthDate(), getStaff.getBirthDate());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @Order(4)
    public void whenDelete_thenReturn204After404() {
        try {
            StaffSignUpDto staffAuthDto = StaffSignUpDto.builder()
                    .password("password123")
                    .username("toDelete")
                    .surname("toDelete")
                    .birthDate(LocalDate.of(1990, 1, 1))
                    .registrationDate(LocalDateTime.now())
                    .build();

            MvcResult toDelete = mvc.perform(MockMvcRequestBuilders.post(PATH_PREFIX)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(staffAuthDto)))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andReturn();

            StaffFullDto toDeleteDto = objectMapper.readValue(
                    toDelete.getResponse().getContentAsString(),
                    StaffFullDto.class
            );


            SignInDto signInDto = SignInDto.builder()
                    .username(toDeleteDto.getUsername())
                    .password(staffAuthDto.getPassword())
                    .role(Role.STAFF.name())
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
                                            objectMapper.readValue(toDelete.getResponse().getContentAsString(),
                                                    StaffFullDto.class).getId())
                                    .accept(MediaType.APPLICATION_JSON)
                                    .header(JwtAuthenticationFilter.HEADER_NAME, toDeleteToken)
                    ).andExpect(MockMvcResultMatchers.status().isNoContent())
                    .andReturn();

            MvcResult afterDelete = mvc.perform(
                            MockMvcRequestBuilders.get(PATH_PREFIX + "/" +
                                            objectMapper.readValue(toDelete.getResponse().getContentAsString(),
                                                    StaffFullDto.class).getId()
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
