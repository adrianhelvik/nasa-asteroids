package com.adrianhelvik.asteroids.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.adrianhelvik.asteroids.*;

public class MissDistance {
  public final String astronomical;
  public final String lunar;
  public final String kilometers;
  public final String miles;

  public MissDistance(
    @JsonProperty("astronomical") String astronomical,
    @JsonProperty("lunar") String lunar,
    @JsonProperty("kilometers") String kilometers,
    @JsonProperty("miles") String miles
  ) {
    this.astronomical = astronomical;
    this.lunar = lunar;
    this.kilometers = kilometers;
    this.miles = miles;
  }
}

