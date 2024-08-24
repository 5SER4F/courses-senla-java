package org.uhanov.conroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.uhanov.dto.CreatorDTO;
import org.uhanov.service.api.CreatorService;

import java.util.UUID;

@Controller
@Data
@RequiredArgsConstructor
public class CreatorController implements CRUDController<CreatorDTO> {
    private final CreatorService service;
    private final ObjectMapper objectMapper;

    @Override
    public Object add(CreatorDTO dto) {
        return writeAsString(service.create(dto));
    }

    @Override
    public Object update(CreatorDTO dto) {
        return writeAsString(service.update(dto));
    }

    @Override
    public Object delete(UUID uuid) {
        return service.delete(uuid) ? "200 OK" : "404 not found";
    }

    @Override
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
