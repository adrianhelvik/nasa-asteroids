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
  public final List<Asteroid> asteroids;

  private NasaApiResponse(
      @JsonProperty("element_count") int element_count,
      @JsonProperty("near_earth_objects") SortedMap<String, List<Asteroid>> near_earth_objects
  ) {
    this.element_count = element_count;

    var asteroids = new ArrayList<Asteroid>();

    for (String date : near_earth_objects.keySet()) {
      for (Asteroid asteroid : near_earth_objects.get(date)) {
        asteroids.add(asteroid.withDate(date));
      }
    }

    this.asteroids = asteroids;
  }

  public static NasaApiResponse fromJson(String json) throws Exception {
    return new ObjectMapper().readValue(json, new TypeReference<NasaApiResponse>(){});
  }
}
