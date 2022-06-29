package com.adrianhelvik.asteroids.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.adrianhelvik.asteroids.models.*;
import com.adrianhelvik.asteroids.*;
import java.util.List;

@JsonPropertyOrder({ "element_count", "data" })
public class ApiResponse<T extends ApiObject> {
  public final List<T> data;
  public final int element_count;

  public ApiResponse(
    @JsonProperty("data") List<T> data,
    @JsonProperty("element_count") int element_count
  ) {
    this.data = data;
    this.element_count = element_count;
  }
}
