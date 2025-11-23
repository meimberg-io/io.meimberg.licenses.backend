package io.meimberg.licenses.web.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * VariantAvailability
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-23T17:42:22.760008697+01:00[Europe/Berlin]", comments = "Generator version: 7.7.0")
public class VariantAvailability {

  private JsonNullable<Integer> capacity = JsonNullable.<Integer>undefined();

  private Integer assignedActiveCount;

  private Boolean available;

  public VariantAvailability() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public VariantAvailability(Integer capacity, Integer assignedActiveCount, Boolean available) {
    this.capacity = JsonNullable.of(capacity);
    this.assignedActiveCount = assignedActiveCount;
    this.available = available;
  }

  public VariantAvailability capacity(Integer capacity) {
    this.capacity = JsonNullable.of(capacity);
    return this;
  }

  /**
   * Get capacity
   * @return capacity
   */
  @NotNull 
  @Schema(name = "capacity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("capacity")
  public JsonNullable<Integer> getCapacity() {
    return capacity;
  }

  public void setCapacity(JsonNullable<Integer> capacity) {
    this.capacity = capacity;
  }

  public VariantAvailability assignedActiveCount(Integer assignedActiveCount) {
    this.assignedActiveCount = assignedActiveCount;
    return this;
  }

  /**
   * Get assignedActiveCount
   * @return assignedActiveCount
   */
  @NotNull 
  @Schema(name = "assignedActiveCount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("assignedActiveCount")
  public Integer getAssignedActiveCount() {
    return assignedActiveCount;
  }

  public void setAssignedActiveCount(Integer assignedActiveCount) {
    this.assignedActiveCount = assignedActiveCount;
  }

  public VariantAvailability available(Boolean available) {
    this.available = available;
    return this;
  }

  /**
   * Get available
   * @return available
   */
  @NotNull 
  @Schema(name = "available", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("available")
  public Boolean getAvailable() {
    return available;
  }

  public void setAvailable(Boolean available) {
    this.available = available;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VariantAvailability variantAvailability = (VariantAvailability) o;
    return Objects.equals(this.capacity, variantAvailability.capacity) &&
        Objects.equals(this.assignedActiveCount, variantAvailability.assignedActiveCount) &&
        Objects.equals(this.available, variantAvailability.available);
  }

  @Override
  public int hashCode() {
    return Objects.hash(capacity, assignedActiveCount, available);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VariantAvailability {\n");
    sb.append("    capacity: ").append(toIndentedString(capacity)).append("\n");
    sb.append("    assignedActiveCount: ").append(toIndentedString(assignedActiveCount)).append("\n");
    sb.append("    available: ").append(toIndentedString(available)).append("\n");
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

