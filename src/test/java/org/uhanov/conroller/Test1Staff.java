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
import org.uhanov.dto.staff.StaffAuthDto;
import org.uhanov.dto.staff.StaffFullDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringJUnitWebConfig(value = WebAppInitializerTestConfig.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test1Staff {
    @Autowired
    public WebApplicationContext wac;
    @Autowired
    public ObjectMapper objectMapper;
    public static MockMvc mvc;

    private static StaffFullDto addedStaff;

    public static final String PATH_PREFIX = "/staff";

    @BeforeEach
    public void init() {
        mvc = MockMvcBuilders.webAppContextSetup(wac)
                .build();

    }

    @Test
    @Order(1)
    public void whenCreate_ThenReturn201AndSameStaffWithId() {
        StaffAuthDto staffAuthDto = StaffAuthDto.builder()
                .password("password123")
                .firstname("John")
                .surname("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .registrationDate(LocalDateTime.now())
                .build();
        try {
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post(PATH_PREFIX)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(staffAuthDto)))
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

        assertEquals(addedStaff.getFirstname(), staffAuthDto.getFirstname());
        assertEquals(addedStaff.getSurname(), staffAuthDto.getSurname());
        assertEquals(addedStaff.getBirthDate(), staffAuthDto.getBirthDate());
    }

    @Test
    @Order(3)
    public void whenUpdate_thenReturn200() {
        try {
            MvcResult result = mvc.perform(
                    MockMvcRequestBuilders.patch(
                                    PATH_PREFIX + "/" + addedStaff.getId()
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(StaffAuthDto.builder().firstname("UpdateName")
                                    .build()))
            ).andReturn();
            addedStaff.setFirstname("UpdateName");
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
                                    .accept(MediaType.APPLICATION_JSON))
                    .andReturn();

            StaffFullDto getStaff = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    StaffFullDto.class
            );
            assertEquals(addedStaff.getId(), getStaff.getId());
            assertEquals(addedStaff.getFirstname(), getStaff.getFirstname());
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
            StaffAuthDto staffAuthDto = StaffAuthDto.builder()
                    .password("password123")
                    .firstname("toDelete")
                    .surname("toDelete")
                    .birthDate(LocalDate.of(1990, 1, 1))
                    .registrationDate(LocalDateTime.now())
                    .build();
            MvcResult toDelete = mvc.perform(MockMvcRequestBuilders.post(PATH_PREFIX)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(staffAuthDto)))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andReturn();

            MvcResult deleteResult = mvc.perform(
                    MockMvcRequestBuilders.delete(PATH_PREFIX + "/" +
                                    objectMapper.readValue(toDelete.getResponse().getContentAsString(),
                                            StaffFullDto.class).getId())
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isNoContent())
                    .andReturn();

            MvcResult result = mvc.perform(
                            MockMvcRequestBuilders.get(PATH_PREFIX + "/" +
                                            objectMapper.readValue(toDelete.getResponse().getContentAsString(),
                                                    StaffFullDto.class).getId()
                                    )
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();

        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

}
