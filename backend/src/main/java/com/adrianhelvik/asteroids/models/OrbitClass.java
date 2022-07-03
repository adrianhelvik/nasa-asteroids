package com.adrianhelvik.asteroids.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrbitClass {
  public final String orbit_class_type;
  public final String orbit_class_description;
  public final String orbit_class_range;

  public OrbitClass(
      @JsonProperty("orbit_class_type") String orbit_class_type,
      @JsonProperty("orbit_class_description") String orbit_class_description,
      @JsonProperty("orbit_class_range") String orbit_class_range
  ) {
      this.orbit_class_type = orbit_class_type;
      this.orbit_class_description = orbit_class_description;
      this.orbit_class_range = orbit_class_range;
  }
}
