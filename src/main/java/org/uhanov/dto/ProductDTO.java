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
public class ProductDTO {
    private UUID id;
    private UUID creatorId;
    private String name;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateAdded;
    private UUID ageRatingId;
    private Double price;

}
