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
import org.uhanov.dto.agerating.AgeRatingDto;
import org.uhanov.dto.agerating.AgeRatingPostDto;
import org.uhanov.dto.creator.CreatorDto;
import org.uhanov.dto.creator.CreatorSignUpDto;
import org.uhanov.dto.genre.GenreDto;
import org.uhanov.dto.genre.GenrePostDto;
import org.uhanov.dto.product.ProductDto;
import org.uhanov.dto.product.ProductPostDto;
import org.uhanov.dto.purchase.PurchaseDto;
import org.uhanov.dto.purchase.PurchasePostDto;
import org.uhanov.dto.staff.StaffFullDto;
import org.uhanov.dto.staff.StaffSignUpDto;
import org.uhanov.dto.user.UserFullDto;
import org.uhanov.dto.user.UserSignUpDto;
import org.uhanov.security.JwtAuthenticationFilter;
import org.uhanov.security.Role;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringJUnitWebConfig(value = SecurityWebApplicationTestInitializer.class)
@ContextConfiguration(classes = SecurityTestConfig.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test7Purchase {
    @Autowired
    public WebApplicationContext wac;
    @Autowired
    public ObjectMapper objectMapper;
    public static MockMvc mvc;

    private static PurchaseDto addedPurchase;

    private static UserFullDto addedUser;

    private static ProductDto addedProduct;


    private static GenreDto addedGenre;

    private static StaffFullDto addedStaff;

    private static AgeRatingDto addedAgeRating;

    private static CreatorDto addedCreator;

    public static String addedUserToken;

    public static String addedStaffToken;

    public static final String PATH_PREFIX = "/purchases";

    @BeforeEach
    public void init() {
        mvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        if (addedStaff == null) {
            try {
                StaffSignUpDto staffSignUpDto = StaffSignUpDto.builder()
                        .password("password123")
                        .username("PURCH_STAFF")
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
                        .name("PURCH_CREATOR")
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
                        .name("PURCH_GENRE")
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
                        .name("PURCH_AR")
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


                ProductPostDto productPostDto = ProductPostDto.builder()
                        .name("PURCH_PRODUCT")
                        .dateAdded(LocalDate.now())
                        .price(Test4Product.PRICE)
                        .discount(Test4Product.DISCOUNT)
                        .creatorId(addedCreator.getId())
                        .ageRatingId(addedAgeRating.getId())
                        .genresIds(Set.of(addedGenre.getId()))
                        .build();

                MvcResult result6 = mvc.perform(MockMvcRequestBuilders.post(Test4Product.PATH_PREFIX)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productPostDto)))
                        .andReturn();

                assertEquals(result6.getResponse().getStatus(), HttpStatus.CREATED.value());


                addedProduct = objectMapper.readValue(
                        result6.getResponse().getContentAsString(),
                        ProductDto.class
                );

                UserSignUpDto userSignUpDto = UserSignUpDto.builder()
                        .password("password")
                        .firstname("John")
                        .surname("Doe")
                        .nickname("PURCH_USER")
                        .birthDate(LocalDate.of(1990, 1, 1))
                        .registrationDate(LocalDateTime.now())
                        .country("USA")
                        .balance(1000.0)
                        .build();

                MvcResult result7 = mvc.perform(
                                MockMvcRequestBuilders.post(Test2User.PATH_PREFIX)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(userSignUpDto))
                        )
                        .andReturn();

                assertEquals(result7.getResponse().getStatus(), HttpStatus.CREATED.value());

                addedUser = objectMapper.readValue(
                        result7.getResponse().getContentAsString(),
                        UserFullDto.class
                );


                SignInDto signInDto = SignInDto.builder()
                        .username(addedUser.getNickname())
                        .password(userSignUpDto.getPassword())
                        .role(Role.USER.name())
                        .build();
                MvcResult userTokenResult = mvc.perform(
                        MockMvcRequestBuilders.post(
                                        Test2User.PATH_PREFIX + "/" + "/login"
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signInDto))
                ).andReturn();

                assertEquals(200, userTokenResult.getResponse().getStatus());

                addedUserToken = preToken(userTokenResult.getResponse().getContentAsString());


                SignInDto signInStaffDto = SignInDto.builder()
                        .username(addedStaff.getUsername())
                        .password(staffSignUpDto.getPassword())
                        .role(Role.STAFF.name())
                        .build();

                MvcResult staffTokenResult = mvc.perform(
                        MockMvcRequestBuilders.post(
                                        Test1Staff.PATH_PREFIX + "/" + "/login"
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signInStaffDto))
                ).andReturn();
                assertEquals(200, staffTokenResult.getResponse().getStatus());

                addedStaffToken = preToken(staffTokenResult.getResponse().getContentAsString());


            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }

    }

    @Test
    @Order(1)
    public void whenCreate_ThenReturn201AndSamePurchaseWithId() {
        try {
            PurchasePostDto purchasePostDto = PurchasePostDto.builder()
                    .buyerId(addedUser.getId())
                    .productId(addedProduct.getId())
                    .purchaseDate(LocalDateTime.now())
                    .cost(BigDecimal.valueOf(100))
                    .build();


            MvcResult result = mvc.perform(MockMvcRequestBuilders.post(PATH_PREFIX)
                            .header(JwtAuthenticationFilter.HEADER_NAME, addedUserToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(purchasePostDto)))
                    .andReturn();

            assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());

            addedPurchase = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    PurchaseDto.class
            );

            assertEquals(addedPurchase.getBuyer().getId(), purchasePostDto.getBuyerId());
            assertEquals(addedPurchase.getProduct().getId(), purchasePostDto.getProductId());
            assertEquals(addedPurchase.getCost().doubleValue(), addedProduct.getPrice() -
                    (addedProduct.getPrice() * addedProduct.getDiscount()));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @Order(2)
    public void whenGet_ThenReturn201AndPurchaseWithPassedId() {
        try {
            MvcResult result = mvc.perform(
                            MockMvcRequestBuilders.get(PATH_PREFIX + "/" + addedPurchase.getId()
                                    )
                                    .header(JwtAuthenticationFilter.HEADER_NAME, addedUserToken)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            PurchaseDto getPurchace = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    PurchaseDto.class
            );

            assertEquals(addedPurchase.getId(), getPurchace.getId());
            assertEquals(addedPurchase.getBuyer().getId(), getPurchace.getBuyer().getId());
            assertEquals(addedPurchase.getProduct().getId(), getPurchace.getProduct().getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @Order(3)
    public void whenDelete_thenReturn204After404() {
        try {
            MvcResult deleteResult = mvc.perform(
                            MockMvcRequestBuilders.delete(PATH_PREFIX + "/" +
                                            addedPurchase.getId())
                                    .accept(MediaType.APPLICATION_JSON)
                                    .header(JwtAuthenticationFilter.HEADER_NAME, addedStaffToken)
                    ).andExpect(MockMvcResultMatchers.status().isNoContent())
                    .andReturn();

            MvcResult result = mvc.perform(
                            MockMvcRequestBuilders.get(PATH_PREFIX + "/" +
                                            addedPurchase.getId())
                                    .header(JwtAuthenticationFilter.HEADER_NAME, addedStaffToken)
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
