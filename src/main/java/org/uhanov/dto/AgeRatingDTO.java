package org.uhanov.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.uhanov.model.Product;
import org.uhanov.model.Staff;

import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgeRatingDTO {
    private UUID id;
    private String name;
    private List<Product> productsWithAgeRating;
    private Staff lastChanger;

}
