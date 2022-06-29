package com.adrianhelvik.asteroids.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.adrianhelvik.asteroids.models.Asteroid;
import java.util.*;

@JsonIgnoreProperties({ "links" })
public class NasaApiResponse {
  public final int element_count;
  public final Map<String, List<Asteroid>> near_earth_objects;

  private NasaApiResponse(
      @JsonProperty("element_count") int element_count,
      @JsonProperty("near_earth_objects") Map<String, List<Asteroid>> near_earth_objects
  ) {
    this.element_count = element_count;
    this.near_earth_objects = near_earth_objects;
  }

  public static NasaApiResponse fromJson(String json) throws Exception {
    return new ObjectMapper().readValue(json, new TypeReference<NasaApiResponse>(){});
  }
}
