package org.uhanov.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.uhanov.model.Product;
import org.uhanov.model.Staff;

import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenreDTO {
    private UUID id;
    private String name;
    private Set<Product> productsWithGenre;
    private Staff lastChanger;

}
