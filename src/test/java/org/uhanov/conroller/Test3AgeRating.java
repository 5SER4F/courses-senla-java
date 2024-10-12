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
import org.uhanov.dto.agerating.AgeRatingDto;
import org.uhanov.dto.agerating.AgeRatingPostDto;
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
public class Test3AgeRating {
    @Autowired
    public WebApplicationContext wac;
    @Autowired
    public ObjectMapper objectMapper;
    public static MockMvc mvc;

    private static AgeRatingDto addedAgeRating;

    private static StaffFullDto addedStaff;

    public static final String PATH_PREFIX = "/age_ratings";

    @BeforeEach
    public void init() {
        mvc = MockMvcBuilders.webAppContextSetup(wac)
                .build();

        if (addedStaff == null) {
            StaffAuthDto staffAuthDto = StaffAuthDto.builder()
                    .password("password123")
                    .firstname("John")
                    .surname("Doe")
                    .birthDate(LocalDate.of(1990, 1, 1))
                    .registrationDate(LocalDateTime.now())
                    .build();
            try {
                MvcResult result = mvc.perform(MockMvcRequestBuilders.post(Test1Staff.PATH_PREFIX)
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
        }

    }

    @Test
    @Order(1)
    public void whenCreate_ThenReturn201AndSameAgeRatingWithId() {
        try {
            AgeRatingPostDto ageRatingPostDto = AgeRatingPostDto.builder()
                    .name("ageRating")
                    .lastChangerId(addedStaff.getId())
                    .build();

            MvcResult result = mvc.perform(MockMvcRequestBuilders.post(PATH_PREFIX)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(ageRatingPostDto)))
                    .andReturn();

            assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());
            addedAgeRating = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    AgeRatingDto.class
            );

            assertEquals(addedAgeRating.getLastChanger().getId(), ageRatingPostDto.getLastChangerId());
            assertEquals(addedAgeRating.getName(), ageRatingPostDto.getName());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @Order(2)
    public void whenUpdate_thenReturn200() {
        try {
            MvcResult result = mvc.perform(
                    MockMvcRequestBuilders.patch(
                                    PATH_PREFIX + "/" + addedAgeRating.getId()
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(AgeRatingPostDto.builder().name("UpdateName")
                                    .build()))
            ).andReturn();
            addedAgeRating.setName("UpdateName");
            assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @Order(3)
    public void whenGet_ThenReturn201AndAgeRatingWithPassedId() {
        try {
            MvcResult result = mvc.perform(
                            MockMvcRequestBuilders.get(PATH_PREFIX + "/" + addedAgeRating.getId()
                                    )
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            AgeRatingDto getAr = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    AgeRatingDto.class
            );
            assertEquals(addedAgeRating.getId(), getAr.getId());
            assertEquals(addedAgeRating.getName(), getAr.getName());
            assertEquals(addedAgeRating.getLastChanger().getId(), getAr.getLastChanger().getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @Order(4)
    public void whenDelete_thenReturn204After404() {
        try {
            AgeRatingPostDto ageRatingPostDto = AgeRatingPostDto.builder()
                    .name("toDelete")
                    .lastChangerId(addedStaff.getId())
                    .build();

            MvcResult toDelete = mvc.perform(MockMvcRequestBuilders.post(PATH_PREFIX)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(ageRatingPostDto)))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andReturn();

            MvcResult deleteResult = mvc.perform(
                            MockMvcRequestBuilders.delete(PATH_PREFIX + "/" +
                                            objectMapper.readValue(toDelete.getResponse().getContentAsString(),
                                                    AgeRatingDto.class).getId())
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(MockMvcResultMatchers.status().isNoContent())
                    .andReturn();

            MvcResult result = mvc.perform(
                            MockMvcRequestBuilders.get(PATH_PREFIX + "/" +
                                            objectMapper.readValue(toDelete.getResponse().getContentAsString(),
                                                    AgeRatingDto.class).getId()
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
