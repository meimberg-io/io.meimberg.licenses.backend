package io.meimberg.licenses.web;

import io.meimberg.licenses.domain.Product;
import io.meimberg.licenses.domain.ProductVariant;
import io.meimberg.licenses.repository.ProductRepository;
import io.meimberg.licenses.repository.ProductVariantRepository;
import io.meimberg.licenses.web.api.ProductsApi;
import io.meimberg.licenses.web.dto.PageProduct;
import io.meimberg.licenses.web.dto.PageProductVariant;
import io.meimberg.licenses.web.dto.ProductCreateRequest;
import io.meimberg.licenses.web.dto.ProductUpdateRequest;
import io.meimberg.licenses.web.dto.ProductVariantCreateRequest;
import io.meimberg.licenses.web.mapper.ProductMapper;
import io.meimberg.licenses.web.mapper.ProductVariantMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ProductsController implements ProductsApi {
  private static final Logger log = LoggerFactory.getLogger(ProductsController.class);
  private final ProductRepository productRepository;
  private final ProductVariantRepository productVariantRepository;
  private final ProductMapper productMapper;
  private final ProductVariantMapper productVariantMapper;

  public ProductsController(
      ProductRepository productRepository,
      ProductVariantRepository productVariantRepository,
      ProductMapper productMapper,
      ProductVariantMapper productVariantMapper
  ) {
    this.productRepository = productRepository;
    this.productVariantRepository = productVariantRepository;
    this.productMapper = productMapper;
    this.productVariantMapper = productVariantMapper;
  }

  @Override
  public ResponseEntity<PageProduct> productsGet(Integer page, Integer size, String sort) {
    try {
      log.debug("productsGet called with page={}, size={}, sort={}", page, size, sort);
      Pageable pageable = buildPageable(page, size, sort);
      log.debug("Created pageable: {}", pageable);
      Page<Product> p = productRepository.findAll(pageable);
      log.debug("Found {} products", p.getTotalElements());
      List<io.meimberg.licenses.web.dto.Product> content = p.getContent().stream()
          .map(productMapper::toDto)
          .toList();
      PageProduct dto = new PageProduct();
      dto.setContent(content);
      dto.setTotalElements((int) p.getTotalElements());
      dto.setTotalPages(p.getTotalPages());
      dto.setSize(p.getSize());
      dto.setNumber(p.getNumber());
      return ResponseEntity.ok(dto);
    } catch (Exception e) {
      log.error("Error in productsGet: {}", e.getMessage(), e);
      throw e;
    }
  }

  @Override
  public ResponseEntity<Void> productsIdDelete(UUID id) {
    if (!productRepository.existsById(id)) throw new EntityNotFoundException("Product not found");
    productRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<io.meimberg.licenses.web.dto.Product> productsIdGet(UUID id) {
    Product entity = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
    return ResponseEntity.ok(productMapper.toDto(entity));
  }

  @Override
  public ResponseEntity<io.meimberg.licenses.web.dto.Product> productsIdPut(UUID id, @Valid ProductUpdateRequest body) {
    Product entity = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
    if (body.getKey() != null) entity.setKey(body.getKey());
    if (body.getName() != null) entity.setName(body.getName());
    if (body.getDescription() != null) entity.setDescription(body.getDescription());
    entity = productRepository.save(entity);
    return ResponseEntity.ok(productMapper.toDto(entity));
  }

  @Override
  public ResponseEntity<io.meimberg.licenses.web.dto.Product> productsPost(@Valid ProductCreateRequest body) {
    Product entity = new Product();
    entity.setId(UUID.randomUUID());
    entity.setKey(body.getKey());
    entity.setName(body.getName());
    entity.setDescription(body.getDescription());
    entity = productRepository.save(entity);
    return ResponseEntity.created(URI.create("/api/v1/products/" + entity.getId())).body(productMapper.toDto(entity));
  }

  @Override
  public ResponseEntity<PageProductVariant> productsProductIdVariantsGet(UUID productId, Integer page, Integer size, String sort) {
    Pageable pageable = buildPageable(page, size, sort);
    Page<ProductVariant> pv = productVariantRepository.findByProductId(productId, pageable);
    List<io.meimberg.licenses.web.dto.ProductVariant> content = pv.getContent().stream()
        .map(productVariantMapper::toDto)
        .toList();
    PageProductVariant dto = new PageProductVariant();
    dto.setContent(content);
    dto.setTotalElements((int) pv.getTotalElements());
    dto.setTotalPages(pv.getTotalPages());
    dto.setSize(pv.getSize());
    dto.setNumber(pv.getNumber());
    return ResponseEntity.ok(dto);
  }

  @Override
  public ResponseEntity<io.meimberg.licenses.web.dto.ProductVariant> productsProductIdVariantsPost(UUID productId, @Valid ProductVariantCreateRequest body) {
    Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product not found"));
    ProductVariant variant = new ProductVariant();
    variant.setId(UUID.randomUUID());
    variant.setProduct(product);
    variant.setKey(body.getKey());
    variant.setName(body.getName());
    if (body.getCapacity() != null && body.getCapacity().isPresent()) {
      variant.setCapacity(body.getCapacity().get());
    }
    if (body.getAttributes() != null && body.getAttributes().isPresent()) {
      variant.setAttributes(productVariantMapper.toJson(body.getAttributes().get()));
    }
    variant = productVariantRepository.save(variant);
    return ResponseEntity.created(URI.create("/api/v1/variants/" + variant.getId())).body(productVariantMapper.toDto(variant));
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


