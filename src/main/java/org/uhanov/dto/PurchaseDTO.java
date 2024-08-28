package org.uhanov.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import org.uhanov.dto.serializer.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class PurchaseDTO {
    private UUID id;
    private UUID userId;
    private UUID productId;
    private Double cost;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime purchaseDate;
}
