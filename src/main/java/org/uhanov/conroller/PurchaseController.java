package org.uhanov.conroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.uhanov.dto.PurchaseDTO;
import org.uhanov.service.api.PurchaseService;

import java.util.UUID;

@Controller
@Data
@RequiredArgsConstructor
public class PurchaseController implements CRUDController<PurchaseDTO> {
    private final PurchaseService service;
    private final ObjectMapper objectMapper;

    @Override
    public Object add(PurchaseDTO dto) {
        return writeAsString(service.create(dto));
    }

    @Override
    public Object update(PurchaseDTO dto) {
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
