package org.uhanov.conroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.uhanov.dto.agerating.AgeRatingDto;
import org.uhanov.dto.agerating.AgeRatingPostDto;
import org.uhanov.dto.creator.CreatorDto;
import org.uhanov.dto.creator.CreatorSignUpDto;
import org.uhanov.dto.genre.GenreDto;
import org.uhanov.dto.genre.GenrePostDto;
import org.uhanov.dto.product.ProductDto;
import org.uhanov.dto.product.ProductPostDto;
import org.uhanov.dto.staff.StaffFullDto;
import org.uhanov.dto.staff.StaffSignUpDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringJUnitWebConfig(value = SecurityWebApplicationTestInitializer.class)
@ContextConfiguration(classes = SecurityTestConfig.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test4Product {
    @Autowired
    public WebApplicationContext wac;
    @Autowired
    public ObjectMapper objectMapper;
    public static MockMvc mvc;

    private static ProductDto addedProduct;

    private static GenreDto addedGenre;

    private static StaffFullDto addedStaff;

    private static AgeRatingDto addedAgeRating;

    private static CreatorDto addedCreator;

    public static final String PATH_PREFIX = "/products";

    public static final double PRICE = 100;

    public static final double DISCOUNT = 0.4;

    @BeforeEach
    public void init() {
        mvc = MockMvcBuilders.webAppContextSetup(wac)
                .build();

        if (addedStaff == null) {
            try {
                StaffSignUpDto staffSignUpDto = StaffSignUpDto.builder()
                        .password("password123")
                        .username("PRODUCT_STAFF")
                        .surname("Doe")
                        .birthDate(LocalDate.of(1990, 1, 1))
                        .registrationDate(LocalDateTime.now())
                        .build();
                MvcResult result = mvc.perform(MockMvcRequestBuilders.post(Test1Staff.PATH_PREFIX)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(staffSignUpDto)))
                        .andReturn();

                assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());

                addedStaff = objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        StaffFullDto.class
                );

                CreatorSignUpDto creatorSignUpDto = CreatorSignUpDto.builder()
                        .password("password123")
                        .name("PRODUCT_CREATOR")
                        .registrationDate(LocalDateTime.now())
                        .build();

                MvcResult result4 = mvc.perform(MockMvcRequestBuilders.post(Test6Creator.PATH_PREFIX)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(creatorSignUpDto)))
                        .andReturn();

                assertEquals(result4.getResponse().getStatus(), HttpStatus.CREATED.value());

                addedCreator = objectMapper.readValue(
                        result4.getResponse().getContentAsString(),
                        CreatorDto.class
                );

                GenrePostDto genrePostDto = GenrePostDto.builder()
                        .name("PRODUCT_GENRE")
                        .lastChangerId(addedStaff.getId())
                        .build();

                MvcResult result1 = mvc.perform(MockMvcRequestBuilders.post(Test5Genres.PATH_PREFIX)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(genrePostDto)))
                        .andReturn();

                assertEquals(result1.getResponse().getStatus(), HttpStatus.CREATED.value());
                addedGenre = objectMapper.readValue(
                        result1.getResponse().getContentAsString(),
                        GenreDto.class
                );

                AgeRatingPostDto ageRatingPostDto = AgeRatingPostDto.builder()
                        .name("PRODUCT_AR")
                        .lastChangerId(addedStaff.getId())
                        .build();

                MvcResult result2 = mvc.perform(MockMvcRequestBuilders.post(Test3AgeRating.PATH_PREFIX)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(ageRatingPostDto)))
                        .andReturn();

                assertEquals(result2.getResponse().getStatus(), HttpStatus.CREATED.value());
                addedAgeRating = objectMapper.readValue(
                        result2.getResponse().getContentAsString(),
                        AgeRatingDto.class
                );

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }

    }

    @Test
    @Order(1)
    public void whenCreate_ThenReturn201AndSameProductWithId() {
        ProductPostDto productPostDto = ProductPostDto.builder()
                .name("ProductName")
                .dateAdded(LocalDate.now())
                .price(PRICE)
                .discount(DISCOUNT)
                .creatorId(addedCreator.getId())
                .ageRatingId(addedAgeRating.getId())
                .genresIds(Set.of(addedGenre.getId()))
                .build();
        try {
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post(PATH_PREFIX)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(productPostDto)))
                    .andReturn();

            assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());


            addedProduct = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    ProductDto.class
            );


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        assertNotNull(addedProduct.getId());

        assertEquals(addedProduct.getName(), productPostDto.getName());
        assertEquals(addedProduct.getPrice(), productPostDto.getPrice());
        assertEquals(addedProduct.getDiscount(), productPostDto.getDiscount());

        assertEquals(addedProduct.getCreator().getId(), productPostDto.getCreatorId());
        assertEquals(addedProduct.getAgeRating().getId(), productPostDto.getAgeRatingId());
        assertEquals(addedProduct.getGenres().stream().findFirst().get().getId(),
                productPostDto.getGenresIds().stream().findFirst().get());
    }

    @Test
    @Order(2)
    public void whenUpdate_thenReturn200() {
        try {
            MvcResult result = mvc.perform(
                    MockMvcRequestBuilders.patch(
                                    PATH_PREFIX + "/" + addedProduct.getId()
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(ProductPostDto.builder().name("UpdateName")
                                    .build()))
            ).andReturn();
            addedProduct.setName("UpdateName");
            assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @Order(3)
    public void whenGet_ThenReturn201AndProductWithPassedId() {
        try {
            MvcResult result = mvc.perform(
                            MockMvcRequestBuilders.get(PATH_PREFIX + "/" + addedProduct.getId()
                                    )
                                    .accept(MediaType.APPLICATION_JSON))
                    .andReturn();

            ProductDto getProduct = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    ProductDto.class
            );
            assertEquals(getProduct, addedProduct);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @Order(4)
    public void whenDelete_thenReturn204After404() {
        try {
            ProductPostDto Product = ProductPostDto.builder()
                    .name("ProductName")
                    .dateAdded(LocalDate.now())
                    .price(PRICE)
                    .discount(DISCOUNT)
                    .creatorId(addedCreator.getId())
                    .ageRatingId(addedAgeRating.getId())
                    .genresIds(Set.of(addedGenre.getId()))
                    .build();
            MvcResult toDelete = mvc.perform(MockMvcRequestBuilders.post(PATH_PREFIX)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(Product)))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andReturn();

            MvcResult deleteResult = mvc.perform(
                            MockMvcRequestBuilders.delete(PATH_PREFIX + "/" +
                                            objectMapper.readValue(toDelete.getResponse().getContentAsString(),
                                                    ProductDto.class).getId())
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(MockMvcResultMatchers.status().isNoContent())
                    .andReturn();

            MvcResult result = mvc.perform(
                            MockMvcRequestBuilders.get(PATH_PREFIX + "/" +
                                            objectMapper.readValue(toDelete.getResponse().getContentAsString(),
                                                    ProductDto.class).getId()
                                    )
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }
}
