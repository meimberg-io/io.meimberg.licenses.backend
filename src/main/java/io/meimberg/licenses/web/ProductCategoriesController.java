package io.meimberg.licenses.web;

import io.meimberg.licenses.domain.ProductCategory;
import io.meimberg.licenses.service.ProductCategoryService;
import io.meimberg.licenses.web.api.ProductCategoriesApi;
import io.meimberg.licenses.web.dto.ProductCategoryCreateRequest;
import io.meimberg.licenses.web.dto.ProductCategoryUpdateRequest;
import io.meimberg.licenses.web.dto.PageProductCategory;
import io.meimberg.licenses.web.mapper.ProductCategoryMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class ProductCategoriesController implements ProductCategoriesApi {
  private final ProductCategoryService productCategoryService;
  private final ProductCategoryMapper productCategoryMapper;

  public ProductCategoriesController(ProductCategoryService productCategoryService, ProductCategoryMapper productCategoryMapper) {
    this.productCategoryService = productCategoryService;
    this.productCategoryMapper = productCategoryMapper;
  }

  @Override
  public ResponseEntity<PageProductCategory> productCategoriesGet(Integer page, Integer size, String sort) {
    Pageable pageable = buildPageable(page, size, sort);
    Page<ProductCategory> p = productCategoryService.list(pageable);
    List<io.meimberg.licenses.web.dto.ProductCategory> content = p.getContent().stream()
        .map(productCategoryMapper::toDto)
        .toList();
    PageProductCategory dto = new PageProductCategory();
    dto.setContent(content);
    dto.setTotalElements((int) p.getTotalElements());
    dto.setTotalPages(p.getTotalPages());
    dto.setSize(p.getSize());
    dto.setNumber(p.getNumber());
    return ResponseEntity.ok(dto);
  }

  @Override
  public ResponseEntity<io.meimberg.licenses.web.dto.ProductCategory> productCategoriesIdGet(UUID id) {
    ProductCategory entity = productCategoryService.get(id)
        .orElseThrow(() -> new EntityNotFoundException("ProductCategory not found"));
    return ResponseEntity.ok(productCategoryMapper.toDto(entity));
  }

  @Override
  public ResponseEntity<io.meimberg.licenses.web.dto.ProductCategory> productCategoriesIdPut(UUID id, @Valid ProductCategoryUpdateRequest body) {
    ProductCategory updated = productCategoryService.update(id, body.getName());
    return ResponseEntity.ok(productCategoryMapper.toDto(updated));
  }

  @Override
  public ResponseEntity<Void> productCategoriesIdDelete(UUID id) {
    productCategoryService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<io.meimberg.licenses.web.dto.ProductCategory> productCategoriesPost(@Valid ProductCategoryCreateRequest body) {
    ProductCategory created = productCategoryService.create(body.getName());
    return ResponseEntity.created(URI.create("/api/v1/product-categories/" + created.getId()))
        .body(productCategoryMapper.toDto(created));
  }

  private Pageable buildPageable(Integer page, Integer size, String sort) {
    int p = page == null ? 0 : page;
    int s = size == null ? 20 : size;
    if (sort == null || sort.isBlank()) return PageRequest.of(p, s);
    Sort.Direction dir = Sort.Direction.ASC;
    String prop = sort;
    if (sort.contains(",")) {
      String[] parts = sort.split(",", 2);
      prop = parts[0];
      dir = Sort.Direction.fromOptionalString(parts[1]).orElse(Sort.Direction.ASC);
    }
    return PageRequest.of(p, s, Sort.by(dir, prop));
  }
}

