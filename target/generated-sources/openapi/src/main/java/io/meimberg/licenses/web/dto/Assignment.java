package io.meimberg.licenses.web.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
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
 * Assignment
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-23T01:28:09.699733449+01:00[Europe/Berlin]", comments = "Generator version: 7.7.0")
public class Assignment {

  private UUID id;

  private UUID userId;

  private UUID productVariantId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("ACTIVE"),
    
    REVOKED("REVOKED");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> startsAt = JsonNullable.<OffsetDateTime>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> endsAt = JsonNullable.<OffsetDateTime>undefined();

  private JsonNullable<String> note = JsonNullable.<String>undefined();

  public Assignment() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Assignment(UUID id, UUID userId, UUID productVariantId, StatusEnum status) {
    this.id = id;
    this.userId = userId;
    this.productVariantId = productVariantId;
    this.status = status;
  }

  public Assignment id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull @Valid 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Assignment userId(UUID userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   * @return userId
   */
  @NotNull @Valid 
  @Schema(name = "userId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("userId")
  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public Assignment productVariantId(UUID productVariantId) {
    this.productVariantId = productVariantId;
    return this;
  }

  /**
   * Get productVariantId
   * @return productVariantId
   */
  @NotNull @Valid 
  @Schema(name = "productVariantId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("productVariantId")
  public UUID getProductVariantId() {
    return productVariantId;
  }

  public void setProductVariantId(UUID productVariantId) {
    this.productVariantId = productVariantId;
  }

  public Assignment status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public Assignment startsAt(OffsetDateTime startsAt) {
    this.startsAt = JsonNullable.of(startsAt);
    return this;
  }

  /**
   * Get startsAt
   * @return startsAt
   */
  @Valid 
  @Schema(name = "startsAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startsAt")
  public JsonNullable<OffsetDateTime> getStartsAt() {
    return startsAt;
  }

  public void setStartsAt(JsonNullable<OffsetDateTime> startsAt) {
    this.startsAt = startsAt;
  }

  public Assignment endsAt(OffsetDateTime endsAt) {
    this.endsAt = JsonNullable.of(endsAt);
    return this;
  }

  /**
   * Get endsAt
   * @return endsAt
   */
  @Valid 
  @Schema(name = "endsAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endsAt")
  public JsonNullable<OffsetDateTime> getEndsAt() {
    return endsAt;
  }

  public void setEndsAt(JsonNullable<OffsetDateTime> endsAt) {
    this.endsAt = endsAt;
  }

  public Assignment note(String note) {
    this.note = JsonNullable.of(note);
    return this;
  }

  /**
   * Get note
   * @return note
   */
  
  @Schema(name = "note", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("note")
  public JsonNullable<String> getNote() {
    return note;
  }

  public void setNote(JsonNullable<String> note) {
    this.note = note;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Assignment assignment = (Assignment) o;
    return Objects.equals(this.id, assignment.id) &&
        Objects.equals(this.userId, assignment.userId) &&
        Objects.equals(this.productVariantId, assignment.productVariantId) &&
        Objects.equals(this.status, assignment.status) &&
        equalsNullable(this.startsAt, assignment.startsAt) &&
        equalsNullable(this.endsAt, assignment.endsAt) &&
        equalsNullable(this.note, assignment.note);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userId, productVariantId, status, hashCodeNullable(startsAt), hashCodeNullable(endsAt), hashCodeNullable(note));
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
    sb.append("class Assignment {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    productVariantId: ").append(toIndentedString(productVariantId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    startsAt: ").append(toIndentedString(startsAt)).append("\n");
    sb.append("    endsAt: ").append(toIndentedString(endsAt)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
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

