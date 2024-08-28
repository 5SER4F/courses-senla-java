package org.uhanov.model.patcher;

import org.springframework.stereotype.Component;
import org.uhanov.dto.ProductDTO;
import org.uhanov.model.Product;

@Component
public class ProductPatcher extends EntityPatcher<Product, ProductDTO> {
}
