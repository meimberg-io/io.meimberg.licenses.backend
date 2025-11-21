package io.meimberg.licenses.service;

import io.meimberg.licenses.domain.ProductVariant;
import io.meimberg.licenses.repository.ProductVariantRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductVariantService {
  private final ProductVariantRepository productVariantRepository;

  public ProductVariantService(ProductVariantRepository productVariantRepository) {
    this.productVariantRepository = productVariantRepository;
  }

  public Optional<ProductVariant> get(UUID id) {
    return productVariantRepository.findById(id);
  }

  public List<ProductVariant> listByProduct(UUID productId) {
    return productVariantRepository.findByProductId(productId);
  }
}


