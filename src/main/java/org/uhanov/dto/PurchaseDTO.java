package org.uhanov.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class PurchaseDTO {
    private UUID id;
    private UUID userId;
    private UUID productId;
    private Double cost;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp purchaseDate;
}
