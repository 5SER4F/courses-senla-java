package org.uhanov.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class GenreDTO {
    private UUID id;
    private String name;
    private UUID lastChanger;

}
