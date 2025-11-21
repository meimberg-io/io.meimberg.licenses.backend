package io.meimberg.licenses.service;

import io.meimberg.licenses.domain.Product;
import io.meimberg.licenses.repository.ProductRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {
  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Optional<Product> get(UUID id) {
    return productRepository.findById(id);
  }
}


