package com.adrianhelvik.asteroids.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonPropertyOrder({"page", "per_page", "element_count", "data"})
public record ApiResponse<T>(List<T> data, int element_count, int page, int per_page) {
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
