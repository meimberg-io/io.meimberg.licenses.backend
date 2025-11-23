package io.meimberg.licenses.web.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.openapitools.jackson.nullable.JsonNullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ProductVariantUpdateRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-23T01:28:09.699733449+01:00[Europe/Berlin]", comments = "Generator version: 7.7.0")
public class ProductVariantUpdateRequest {

  private String key;

  private String name;

  private JsonNullable<Integer> capacity = JsonNullable.<Integer>undefined();

  @Valid
  private JsonNullable<Map<String, Object>> attributes = JsonNullable.<Map<String, Object>>undefined();

  public ProductVariantUpdateRequest key(String key) {
    this.key = key;
    return this;
  }

  /**
   * Get key
   * @return key
   */
  
  @Schema(name = "key", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("key")
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public ProductVariantUpdateRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ProductVariantUpdateRequest capacity(Integer capacity) {
    this.capacity = JsonNullable.of(capacity);
    return this;
  }

  /**
   * Get capacity
   * @return capacity
   */
  
  @Schema(name = "capacity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("capacity")
  public JsonNullable<Integer> getCapacity() {
    return capacity;
  }

  public void setCapacity(JsonNullable<Integer> capacity) {
    this.capacity = capacity;
  }

  public ProductVariantUpdateRequest attributes(Map<String, Object> attributes) {
    this.attributes = JsonNullable.of(attributes);
    return this;
  }

  public ProductVariantUpdateRequest putAttributesItem(String key, Object attributesItem) {
    if (this.attributes == null || !this.attributes.isPresent()) {
      this.attributes = JsonNullable.of(new HashMap<>());
    }
    this.attributes.get().put(key, attributesItem);
    return this;
  }

  /**
   * Get attributes
   * @return attributes
   */
  
  @Schema(name = "attributes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attributes")
  public JsonNullable<Map<String, Object>> getAttributes() {
    return attributes;
  }

  public void setAttributes(JsonNullable<Map<String, Object>> attributes) {
    this.attributes = attributes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductVariantUpdateRequest productVariantUpdateRequest = (ProductVariantUpdateRequest) o;
    return Objects.equals(this.key, productVariantUpdateRequest.key) &&
        Objects.equals(this.name, productVariantUpdateRequest.name) &&
        equalsNullable(this.capacity, productVariantUpdateRequest.capacity) &&
        equalsNullable(this.attributes, productVariantUpdateRequest.attributes);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, name, hashCodeNullable(capacity), hashCodeNullable(attributes));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductVariantUpdateRequest {\n");
    sb.append("    key: ").append(toIndentedString(key)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    capacity: ").append(toIndentedString(capacity)).append("\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

