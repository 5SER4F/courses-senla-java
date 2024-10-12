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
import org.uhanov.dto.purchase.PurchaseDto;
import org.uhanov.dto.purchase.PurchasePostDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.uhanov.conroller.Test2User.addedUser;
import static org.uhanov.conroller.Test4Produc.addedProduct;

@ExtendWith(SpringExtension.class)
@SpringJUnitWebConfig(value = WebAppInitializerTestConfig.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test7Purchase {
    @Autowired
    public WebApplicationContext wac;
    @Autowired
    public ObjectMapper objectMapper;
    public static MockMvc mvc;

    public static PurchaseDto addedPurchase;

    public static final String PATH_PREFIX = "/purchases";

    @BeforeEach
    public void init() {
        mvc = MockMvcBuilders.webAppContextSetup(wac)
                .build();

    }

    @Test
    @Order(1)
    public void whenCreate_ThenReturn201AndSamePurchaseWithId() {
        try {
            PurchasePostDto purchasePostDto = PurchasePostDto.builder()
                    .buyerId(addedUser.getId())
                    .productId(addedProduct.getId())
                    .purchaseDate(LocalDateTime.now())
                    .build();

            MvcResult result = mvc.perform(MockMvcRequestBuilders.post(PATH_PREFIX)
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
                    (addedProduct.getPrice() * addedProduct.getDiscount()) );

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
    @Order(4)
    public void whenDelete_thenReturn204After404() {
        try {
            PurchasePostDto purchasePostDto = PurchasePostDto.builder()
                    .buyerId(addedUser.getId())
                    .productId(addedProduct.getId())
                    .purchaseDate(LocalDateTime.now())
                    .build();

            MvcResult toDelete = mvc.perform(MockMvcRequestBuilders.post(PATH_PREFIX)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(purchasePostDto)))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andReturn();

            MvcResult deleteResult = mvc.perform(
                            MockMvcRequestBuilders.delete(PATH_PREFIX + "/" +
                                            objectMapper.readValue(toDelete.getResponse().getContentAsString(),
                                                    PurchaseDto.class).getId())
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(MockMvcResultMatchers.status().isNoContent())
                    .andReturn();

            MvcResult result = mvc.perform(
                            MockMvcRequestBuilders.get(PATH_PREFIX + "/" +
                                            objectMapper.readValue(toDelete.getResponse().getContentAsString(),
                                                    PurchaseDto.class).getId()
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
