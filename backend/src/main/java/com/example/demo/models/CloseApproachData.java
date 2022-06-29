package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.demo.models.*;

@JsonIgnoreProperties({ "close_approach_date_full", "epoch_date_close_approach" })
public class CloseApproachData {
  public final String close_approach_date;
  public final MissDistance miss_distance;
  public final RelativeVelocity relative_velocity;
  public final String orbiting_body;

  public CloseApproachData(
    @JsonProperty("close_approach_date") String close_approach_date,
    @JsonProperty("miss_distance") MissDistance miss_distance,
    @JsonProperty("relative_velocity") RelativeVelocity relative_velocity,
    @JsonProperty("orbiting_body") String orbiting_body
  ) {
    this.close_approach_date = close_approach_date;
    this.miss_distance = miss_distance;
    this.relative_velocity = relative_velocity;
    this.orbiting_body = orbiting_body;
  }
}

