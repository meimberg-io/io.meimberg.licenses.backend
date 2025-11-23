package io.meimberg.licenses.service;

import io.meimberg.licenses.domain.ProductCategory;
import io.meimberg.licenses.repository.ProductCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProductCategoryService {
  private final ProductCategoryRepository productCategoryRepository;

  public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
    this.productCategoryRepository = productCategoryRepository;
  }

  public Optional<ProductCategory> get(UUID id) {
    return productCategoryRepository.findById(id);
  }

  @Transactional(readOnly = true)
  public Page<ProductCategory> list(Pageable pageable) {
    return productCategoryRepository.findAll(pageable);
  }

  @Transactional
  public ProductCategory create(String name) {
    ProductCategory category = new ProductCategory();
    category.setId(UUID.randomUUID());
    category.setName(name);
    return productCategoryRepository.save(category);
  }

  @Transactional
  public ProductCategory update(UUID id, String name) {
    ProductCategory category = productCategoryRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("ProductCategory not found"));
    if (name != null) category.setName(name);
    return productCategoryRepository.save(category);
  }

  @Transactional
  public void delete(UUID id) {
    if (!productCategoryRepository.existsById(id)) {
      throw new EntityNotFoundException("ProductCategory not found");
    }
    productCategoryRepository.deleteById(id);
  }
}

