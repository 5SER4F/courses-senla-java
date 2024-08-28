package org.uhanov.conroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.uhanov.dto.CreatorAuthDTO;
import org.uhanov.service.api.CreatorService;

import java.util.UUID;

@Controller
@Data
@RequiredArgsConstructor
public class CreatorController {
    private final CreatorService service;
    private final ObjectMapper objectMapper;


    public Object add(CreatorAuthDTO dto) {
        return writeAsString(service.create(dto));
    }


    public void update(CreatorAuthDTO dto) {
        service.update(dto);
    }


    public Object delete(UUID uuid) {
        return service.delete(uuid) ? "200 OK" : "404 not found";
    }


    public Object get(UUID uuid) {
        return writeAsString(service.getById(uuid));
    }

    private String writeAsString(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return "500 Internal Server Error";
        }
    }
}
