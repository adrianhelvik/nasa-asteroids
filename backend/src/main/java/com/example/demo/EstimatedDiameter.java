package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EstimatedDiameter {
  public final float estimated_diameter_min;
  public final float estimated_diameter_max;

  public EstimatedDiameter(
    @JsonProperty("estimated_diameter_min") float estimated_diameter_min,
    @JsonProperty("estimated_diameter_max") float estimated_diameter_max
  ) {
    this.estimated_diameter_min = estimated_diameter_min;
    this.estimated_diameter_max = estimated_diameter_max;
  }
}
