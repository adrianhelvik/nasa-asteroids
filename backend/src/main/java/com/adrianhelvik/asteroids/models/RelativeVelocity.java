package com.adrianhelvik.asteroids.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RelativeVelocity(String kilometers_per_second, String kilometers_per_hour, String miles_per_hour) {
  public RelativeVelocity(
          @JsonProperty("kilometers_per_second") String kilometers_per_second,
          @JsonProperty("kilometers_per_hour") String kilometers_per_hour,
          @JsonProperty("miles_per_hour") String miles_per_hour
  ) {
    this.kilometers_per_second = kilometers_per_second;
    this.kilometers_per_hour = kilometers_per_hour;
    this.miles_per_hour = miles_per_hour;
  }
}