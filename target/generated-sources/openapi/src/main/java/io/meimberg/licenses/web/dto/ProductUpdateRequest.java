package io.meimberg.licenses.web.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import java.util.UUID;
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
 * ProductUpdateRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-23T17:42:22.760008697+01:00[Europe/Berlin]", comments = "Generator version: 7.7.0")
public class ProductUpdateRequest {

  private String key;

  private String name;

  private String description;

  private JsonNullable<UUID> manufacturerId = JsonNullable.<UUID>undefined();

  private JsonNullable<UUID> categoryId = JsonNullable.<UUID>undefined();

  public ProductUpdateRequest key(String key) {
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

  public ProductUpdateRequest name(String name) {
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

  public ProductUpdateRequest description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ProductUpdateRequest manufacturerId(UUID manufacturerId) {
    this.manufacturerId = JsonNullable.of(manufacturerId);
    return this;
  }

  /**
   * Get manufacturerId
   * @return manufacturerId
   */
  @Valid 
  @Schema(name = "manufacturerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("manufacturerId")
  public JsonNullable<UUID> getManufacturerId() {
    return manufacturerId;
  }

  public void setManufacturerId(JsonNullable<UUID> manufacturerId) {
    this.manufacturerId = manufacturerId;
  }

  public ProductUpdateRequest categoryId(UUID categoryId) {
    this.categoryId = JsonNullable.of(categoryId);
    return this;
  }

  /**
   * Get categoryId
   * @return categoryId
   */
  @Valid 
  @Schema(name = "categoryId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("categoryId")
  public JsonNullable<UUID> getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(JsonNullable<UUID> categoryId) {
    this.categoryId = categoryId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductUpdateRequest productUpdateRequest = (ProductUpdateRequest) o;
    return Objects.equals(this.key, productUpdateRequest.key) &&
        Objects.equals(this.name, productUpdateRequest.name) &&
        Objects.equals(this.description, productUpdateRequest.description) &&
        equalsNullable(this.manufacturerId, productUpdateRequest.manufacturerId) &&
        equalsNullable(this.categoryId, productUpdateRequest.categoryId);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, name, description, hashCodeNullable(manufacturerId), hashCodeNullable(categoryId));
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
    sb.append("class ProductUpdateRequest {\n");
    sb.append("    key: ").append(toIndentedString(key)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    manufacturerId: ").append(toIndentedString(manufacturerId)).append("\n");
    sb.append("    categoryId: ").append(toIndentedString(categoryId)).append("\n");
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

