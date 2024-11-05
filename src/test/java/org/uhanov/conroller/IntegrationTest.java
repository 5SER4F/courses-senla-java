package org.uhanov.conroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import org.uhanov.dto.creator.CreatorDto;
import org.uhanov.dto.creator.CreatorPostDto;
import org.uhanov.dto.genre.GenreDto;
import org.uhanov.dto.genre.GenrePostDto;
import org.uhanov.dto.product.ProductDto;
import org.uhanov.dto.product.ProductGetAllParams;
import org.uhanov.dto.product.ProductPostDto;
import org.uhanov.dto.purchase.PurchaseDto;
import org.uhanov.dto.purchase.PurchasePostDto;
import org.uhanov.dto.user.CustomerFullDto;
import org.uhanov.dto.user.CustomerPostDto;
import org.uhanov.model.AgeRating;
import org.uhanov.model.Staff;
import org.uhanov.model.user.AccountStatus;
import org.uhanov.model.user.Role;
import org.uhanov.repository.api.StaffRepository;
import org.uhanov.security.JwtAuthenticationFilter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringJUnitWebConfig(value = SecurityWebApplicationTestInitializer.class)
@ContextConfiguration(classes = SecurityTestConfig.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationTest {
    @Autowired
    public WebApplicationContext wac;
    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public PasswordEncoder passwordEncoder;

    public static MockMvc mvc;

    private static PurchaseDto addedPurchase;

    private static CustomerFullDto addedCustomer;

    private static ProductDto addedProduct;

    private static GenreDto addedGenre;

    private static Staff addedStaff;


    private static CreatorDto addedCreator;

    public static String addedUserToken;
    public static String addedCreatorToken;

    public static String addedStaffToken;

    public static final String ACCOUNT_PATH = "/account";
    public static final String CUSTOMER_POST_PATH = ACCOUNT_PATH + "/signUp/customer";
    public static final String CREATOR_POST_PATH = ACCOUNT_PATH + "/signUp/creator";
    public static final String GENRE_PATH = "/genres";
    public static final String PRODUCT_PATH = "/products";
    private static final String PURCHASE_PATH = "/purchases";


    public static final String CUSTOMER_PASSWORD = "CustomerPassword";
    public static final String CREATOR_PASSWORD = "CreatorPassword";
    public static final String STAFF_PASSWORD = "SuperPassword";

    public static final Double PRICE = 100.0;

    public static final double DISCOUNT = 0.1;


    @BeforeAll
    public void initBeforeAll() {
        System.out.println(wac);
        mvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @Order(1)
    public void whenCreateCustomer_ThenReturn201AndCreatedCustomer() {
        CustomerPostDto userSignUpDto = CustomerPostDto.builder()
                .password(CUSTOMER_PASSWORD)
                .username("CustomerUsername")
                .email("Useremail@gmail.com")
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

            addedCustomer = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    CustomerFullDto.class
            );

            System.out.println("Added user:" + addedCustomer);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        assertNotNull(addedCustomer.getId());

        assertEquals(addedCustomer.getFirstname(), userSignUpDto.getFirstname());
        assertEquals(addedCustomer.getSurname(), userSignUpDto.getSurname());
        assertEquals(addedCustomer.getBirthDate(), userSignUpDto.getBirthDate());
        assertEquals(addedCustomer.getNickname(), userSignUpDto.getNickname());
    }


    @Test
    @Order(2)
    public void whenSignInAsCustomer_thenReturnOkAndJwtToken() {
        try {
            SignInDto signInDto = SignInDto.builder()
                    .username(addedCustomer.getUsername())
                    .password(CUSTOMER_PASSWORD)
                    .build();
            MvcResult result = mvc.perform(
                    MockMvcRequestBuilders.post(
                                    ACCOUNT_PATH + "/signIn"
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(signInDto))
            ).andReturn();

            assertEquals(200, result.getResponse().getStatus());

            addedUserToken = preToken(result.getResponse().getContentAsString());

            MvcResult resultAddMoney = mvc.perform(
                            MockMvcRequestBuilders.patch(
                                            "/customer" + "/" + addedCustomer.getId()
                                    )
                                    .header(JwtAuthenticationFilter.HEADER_NAME, addedUserToken)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(CustomerPostDto.builder()
                                            .balance(1000.0)
                                            .build()))
                    )
                    .andReturn();
            assertEquals(resultAddMoney.getResponse().getStatus(), HttpStatus.OK.value());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @Order(3)
    public void whenCreateCreator_ThenReturn201AndCreatedCreator() {
        CreatorPostDto creatorSignUpDto = CreatorPostDto.builder()
                .password(CREATOR_PASSWORD)
                .username("CreatorUsername")
                .email("CreatorEmail@gmail.com")
                .name("CreatorName")
                .registrationDate(LocalDateTime.now())
                .build();
        try {
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post(CREATOR_POST_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(creatorSignUpDto)))
                    .andReturn();

            assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());

            addedCreator = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    CreatorDto.class
            );

            System.out.println(addedCreator);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        assertNotNull(addedCreator.getId());

        assertEquals(addedCreator.getName(), creatorSignUpDto.getName());
    }

    @Test
    @Order(4)
    public void whenSignInCreator_thenReturn200() {
        try {
            SignInDto signInDto = SignInDto.builder()
                    .username(addedCreator.getUsername())
                    .password(CREATOR_PASSWORD)
                    .build();

            MvcResult result = mvc.perform(
                    MockMvcRequestBuilders.post(
                                    ACCOUNT_PATH + "/signIn"
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
    @Order(5)
    public void whenSignInStaff_thenReturn200() {
        try {
            Staff staff = Staff.builder()
                    .role(Role.STAFF)
                    .username("SUPERADMIN")
                    .password(passwordEncoder.encode(STAFF_PASSWORD))
                    .email("staffemail@gmail.com")
                    .registrationDate(LocalDateTime.now())
                    .accountStatus(AccountStatus.CREATED)
                    .firstname("staffFirstName")
                    .surname("staffSurname")
                    .nickname("StaffNickName")
                    .birthDate(LocalDate.now().minusYears(30))
                    .build();

            addedStaff = wac.getBean(StaffRepository.class)
                    .save(
                            staff
                    );

            SignInDto signInDto = SignInDto.builder()
                    .username("SUPERADMIN")
                    .password(STAFF_PASSWORD)
                    .build();

            MvcResult result = mvc.perform(
                    MockMvcRequestBuilders.post(
                                    ACCOUNT_PATH + "/signIn"
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(signInDto))
            ).andReturn();
            assertEquals(200, result.getResponse().getStatus());

            addedStaffToken = preToken(result.getResponse().getContentAsString());

            System.out.println(addedStaff.getId());
            System.out.println(addedStaffToken);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @Order(6)
    public void whenCreateAndUpdateGenre_thenReturn201AndGenre() {
        try {
            GenrePostDto genresPostDto = GenrePostDto.builder()
                    .name("genre")
                    .build();

            MvcResult result = mvc.perform(MockMvcRequestBuilders.post(GENRE_PATH)
                            .header(JwtAuthenticationFilter.HEADER_NAME, addedStaffToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(genresPostDto)))
                    .andReturn();

            assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());
            addedGenre = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    GenreDto.class
            );

            System.out.println(addedGenre);

            assertEquals(addedGenre.getName(), genresPostDto.getName());


            MvcResult resultOfUpdate = mvc.perform(
                    MockMvcRequestBuilders.patch(
                                    GENRE_PATH + "/" + addedGenre.getId()
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(JwtAuthenticationFilter.HEADER_NAME, addedStaffToken)
                            .content(objectMapper.writeValueAsString(GenrePostDto.builder().name("UpdateName")
                                    .build()))
            ).andReturn();

            addedGenre.setName("UpdateName");

            assertEquals(resultOfUpdate.getResponse().getStatus(), HttpStatus.OK.value());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @Order(7)
    public void whenGetAndGetAllGenre_thenReturnSameGenre() {
        try {
            MvcResult result = mvc.perform(
                            MockMvcRequestBuilders.get(GENRE_PATH + "/" + addedGenre.getId()
                                    )
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            GenreDto getGenre = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    GenreDto.class
            );
            assertEquals(addedGenre.getId(), getGenre.getId());
            assertEquals(addedGenre.getName(), getGenre.getName());

            MvcResult resultGetAll = mvc.perform(
                            MockMvcRequestBuilders.get(GENRE_PATH)
                                    .param("page", "0")
                                    .param("size", "15")
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            String page = resultGetAll.getResponse().getContentAsString();

            assertTrue(page.contains(addedGenre.getId().toString()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @Order(8)
    public void whenCreateAndUpdateProduct_thenReturn201AndProduct() {
        ProductPostDto productPostDto = ProductPostDto.builder()
                .name("ProductName")
                .dateAdded(LocalDate.now())
                .price(PRICE)
                .discount(DISCOUNT)
                .creatorId(addedCreator.getId())
                .ageRating(AgeRating.EC)
                .genresIds(Set.of(addedGenre.getId()))
                .build();
        try {
            addedCreatorToken = addedCreatorToken.trim();
            System.out.println(addedCreatorToken);
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post(PRODUCT_PATH)
                            .header(JwtAuthenticationFilter.HEADER_NAME, addedCreatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(productPostDto)))
                    .andReturn();

            assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());


            addedProduct = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    ProductDto.class
            );

            System.out.println(addedProduct);

            assertNotNull(addedProduct.getId());

            assertEquals(addedProduct.getName(), productPostDto.getName());
            assertEquals(addedProduct.getPrice(), productPostDto.getPrice());
            assertEquals(addedProduct.getDiscount(), productPostDto.getDiscount());

            assertEquals(addedProduct.getCreator().getId(), productPostDto.getCreatorId());
            assertEquals(addedProduct.getGenres().stream().findFirst().get().getId(),
                    productPostDto.getGenresIds().stream().findFirst().get());

            MvcResult resultUpdate = mvc.perform(
                    MockMvcRequestBuilders.patch(
                                    PRODUCT_PATH + "/" + addedProduct.getId()
                            )
                            .header(JwtAuthenticationFilter.HEADER_NAME, addedCreatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(ProductPostDto.builder().name("UpdateName")
                                    .build()))
            ).andReturn();
            addedProduct.setName("UpdateName");
            assertEquals(resultUpdate.getResponse().getStatus(), HttpStatus.OK.value());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    @Test
    @Order(9)
    public void whenGetAndGetAllProduct_thenReturnSameProduct() {
        try {
            MvcResult result = mvc.perform(
                            MockMvcRequestBuilders.get(PRODUCT_PATH + "/" + addedProduct.getId()
                                    )
                                    .accept(MediaType.APPLICATION_JSON))
                    .andReturn();

            ProductDto getProduct = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    ProductDto.class
            );
            assertEquals(getProduct, addedProduct);

            System.out.println(addedProduct);

            ProductGetAllParams productGetAllParams = new ProductGetAllParams(
                    List.of(addedGenre.getName()),
                    List.of(addedCreator.getName()),
                    LocalDate.now().minusYears(1),
                    LocalDate.now().plusYears(1)
            );

            MvcResult resultGetAll = mvc.perform(
                            MockMvcRequestBuilders.get(PRODUCT_PATH + "/all"
                                    )
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(productGetAllParams))
                                    .accept(MediaType.APPLICATION_JSON))
                    .andReturn();

            assertEquals(resultGetAll.getResponse().getStatus(), 200);
            String contentAsString = resultGetAll.getResponse().getContentAsString();
            assertTrue(contentAsString.contains(addedProduct.getId().toString()));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @Order(10)
    public void whenCreateAndUpdatePurchase_thenReturn201AndProduct() {
        try {
            PurchasePostDto purchasePostDto = PurchasePostDto.builder()
                    .buyerId(addedCustomer.getId())
                    .productId(addedProduct.getId())
                    .purchaseDate(LocalDateTime.now())
                    .cost(BigDecimal.valueOf(100))
                    .build();


            MvcResult result = mvc.perform(MockMvcRequestBuilders.post(PURCHASE_PATH)
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
            assertEquals(addedPurchase.getFinalCost().doubleValue(), addedProduct.getPrice() -
                    (addedProduct.getPrice() * addedProduct.getDiscount()));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    @Order(11)
    public void whenGetCustomerLibraryAndPurchase_thenReturn200AndCreatedPurchaseAndProduct() {
        try {
            MvcResult resultLibrary = mvc.perform(
                    MockMvcRequestBuilders.get(
                                    "/customer/" + addedCustomer.getId() + "/library"
                            )
                            .header(JwtAuthenticationFilter.HEADER_NAME, addedUserToken)
                            .accept(MediaType.APPLICATION_JSON)
            ).andReturn();

            assertTrue(resultLibrary.getResponse().getContentAsString()
                    .contains(addedProduct.getId().toString()));


            MvcResult resultCustomerPurchase = mvc.perform(
                    MockMvcRequestBuilders.get(
                                    "/customer/" + addedCustomer.getId() + "/purchase"
                            )
                            .header(JwtAuthenticationFilter.HEADER_NAME, addedUserToken)
                            .accept(MediaType.APPLICATION_JSON)
            ).andReturn();

            assertTrue(resultCustomerPurchase.getResponse().getContentAsString()
                    .contains(addedPurchase.getId().toString()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public MvcResult createUser(CustomerPostDto dto) throws Exception {
        return mvc.perform(
                        MockMvcRequestBuilders.post(CUSTOMER_POST_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
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
