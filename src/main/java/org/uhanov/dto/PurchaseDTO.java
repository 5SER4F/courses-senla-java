package org.uhanov.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.uhanov.dto.serializer.LocalDateTimeDeserializer;
import org.uhanov.dto.serializer.LocalDateTimeSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDTO {
    private UUID id;
    private UUID userId;
    private UUID productId;
    private BigDecimal cost;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime purchaseDate;

    private UserShortDTO buyer;
    private ProductDTO product;
}
