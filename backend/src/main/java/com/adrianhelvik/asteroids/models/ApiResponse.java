package com.adrianhelvik.asteroids.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.adrianhelvik.asteroids.models.*;
import com.adrianhelvik.asteroids.*;
import java.util.List;

@JsonPropertyOrder({ "page", "per_page", "element_count", "data" })
public class ApiResponse<T extends ApiObject> {
  public final List<T> data;
  public final int element_count;
  public final int page;
  public final int per_page;

  public ApiResponse(
    @JsonProperty("data") List<T> data,
    @JsonProperty("element_count") int element_count,
    @JsonProperty("page") int page,
    @JsonProperty("per_page") int per_page
  ) {
    this.data = data;
    this.element_count = element_count;
    this.per_page = per_page;
    this.page = page;
  }
}
