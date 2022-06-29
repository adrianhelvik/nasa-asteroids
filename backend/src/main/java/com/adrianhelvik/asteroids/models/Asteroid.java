package com.adrianhelvik.asteroids.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.adrianhelvik.asteroids.models.EstimatedDiameter;
import com.adrianhelvik.asteroids.models.CloseApproachData;
import java.util.*;

@JsonIgnoreProperties({ "links", "neo_reference_id", "nasa_jpl_url", "is_sentry_object" })
public class Asteroid {
  public final String id;
  public final String name;
  public final float absolute_magnitude_h;
  public final Map<String, EstimatedDiameter> estimated_diameter;
  public final boolean is_potentially_hazardous_asteroid;
  public final List<CloseApproachData> close_approach_data;

  public Asteroid(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("absolute_magnitude_h") float absolute_magnitude_h,
    @JsonProperty("estimated_diameter") Map<String, EstimatedDiameter> estimated_diameter,
    @JsonProperty("is_potentially_hazardous_asteroid") boolean is_potentially_hazardous_asteroid,
    @JsonProperty("close_approach_data") List<CloseApproachData> close_approach_data
  ) {
    this.id = id;
    this.name = name;
    this.absolute_magnitude_h = absolute_magnitude_h;
    this.estimated_diameter = estimated_diameter;
    this.is_potentially_hazardous_asteroid = is_potentially_hazardous_asteroid;
    this.close_approach_data = close_approach_data;
  }
}

