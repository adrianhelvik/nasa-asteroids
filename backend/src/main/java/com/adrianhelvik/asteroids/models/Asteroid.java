package com.adrianhelvik.asteroids.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.adrianhelvik.asteroids.models.EstimatedDiameter;
import com.adrianhelvik.asteroids.models.CloseApproachData;
import com.adrianhelvik.asteroids.ApiObject;
import java.util.*;

@JsonIgnoreProperties({ "links", "nasa_jpl_url", "is_sentry_object" })
public class Asteroid implements ApiObject {
  public final String id;
  public final String name;
  public final float absolute_magnitude_h;
  public final Map<String, EstimatedDiameter> estimated_diameter;
  public final boolean is_potentially_hazardous_asteroid;
  public final List<CloseApproachData> close_approach_data;
  public final String date;
  public final Double miss_distance_km;
  public final String neo_reference_id;

  public Asteroid(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("absolute_magnitude_h") float absolute_magnitude_h,
    @JsonProperty("estimated_diameter") Map<String, EstimatedDiameter> estimated_diameter,
    @JsonProperty("is_potentially_hazardous_asteroid") boolean is_potentially_hazardous_asteroid,
    @JsonProperty("close_approach_data") List<CloseApproachData> close_approach_data,
    @JsonProperty("neo_reference_id") String neo_reference_id,
    @JsonProperty("date") String date
  ) {
    this.id = id;
    this.name = name;
    this.absolute_magnitude_h = absolute_magnitude_h;
    this.estimated_diameter = estimated_diameter;
    this.is_potentially_hazardous_asteroid = is_potentially_hazardous_asteroid;
    this.close_approach_data = close_approach_data;
    this.neo_reference_id = neo_reference_id;
    this.date = date;
    Double closestMiss = null;
    for (var approach : close_approach_data) {
      double missDistance = Double.parseDouble(approach.miss_distance.kilometers);
      if (closestMiss == null || missDistance < closestMiss) {
        closestMiss = missDistance;
      }
    }
    this.miss_distance_km = closestMiss;
  }

  protected Asteroid withDate(String date) {
    return new Asteroid(
      id,
      name,
      absolute_magnitude_h,
      estimated_diameter,
      is_potentially_hazardous_asteroid,
      close_approach_data,
      neo_reference_id,
      date
    );
  }

  public static Asteroid fromJson(String json) throws Exception {
    return new ObjectMapper().readValue(json, new TypeReference<Asteroid>(){});
  }
}
