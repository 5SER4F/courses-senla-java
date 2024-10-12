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
import org.uhanov.dto.creator.CreatorAuthDto;
import org.uhanov.dto.creator.CreatorDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringJUnitWebConfig(value = WebAppInitializerTestConfig.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test6 {
    @Autowired
    public WebApplicationContext wac;
    @Autowired
    public ObjectMapper objectMapper;
    public static MockMvc mvc;

    public static CreatorDto addedCreator;

    public static final String PATH_PREFIX = "/creators";

    @BeforeEach
    public void init() {
        mvc = MockMvcBuilders.webAppContextSetup(wac)
                .build();

    }

    @Test
    @Order(1)
    public void whenCreate_ThenReturn201AndSameStaffWithId() {
        CreatorAuthDto creatorAuthDto = CreatorAuthDto.builder()
                .password("password123")
                .name("CreatorName")
                .registrationDate(LocalDateTime.now())
                .build();
        try {
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post(PATH_PREFIX)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(creatorAuthDto)))
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

        assertEquals(addedCreator.getName(), creatorAuthDto.getName());
    }

    @Test
    @Order(3)
    public void whenUpdate_thenReturn200() {
        try {
            MvcResult result = mvc.perform(
                    MockMvcRequestBuilders.patch(
                                    PATH_PREFIX + "/" + addedCreator.getId()
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(CreatorAuthDto.builder().name("UpdateName")
                                    .build()))
            ).andReturn();
            addedCreator.setName("UpdateName");
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
            CreatorAuthDto creatorAuthDto = CreatorAuthDto.builder()
                    .password("password123")
                    .name("toDelete")
                    .registrationDate(LocalDateTime.now())
                    .build();
            MvcResult toDelete = mvc.perform(MockMvcRequestBuilders.post(PATH_PREFIX)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(creatorAuthDto)))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andReturn();

            MvcResult deleteResult = mvc.perform(
                            MockMvcRequestBuilders.delete(PATH_PREFIX + "/" +
                                            objectMapper.readValue(toDelete.getResponse().getContentAsString(),
                                                    CreatorDto.class).getId())
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(MockMvcResultMatchers.status().isNoContent())
                    .andReturn();

            MvcResult result = mvc.perform(
                            MockMvcRequestBuilders.get(PATH_PREFIX + "/" +
                                            objectMapper.readValue(toDelete.getResponse().getContentAsString(),
                                                    CreatorDto.class).getId()
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
