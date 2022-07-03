package com.adrianhelvik.asteroids.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class DetailedAsteroid extends Asteroid {
    public DetailedAsteroid(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("absolute_magnitude_h") float absolute_magnitude_h,
        @JsonProperty("estimated_diameter") Map<String, EstimatedDiameter> estimated_diameter,
        @JsonProperty("is_potentially_hazardous_asteroid") boolean is_potentially_hazardous_asteroid,
        @JsonProperty("close_approach_data") List<CloseApproachData> close_approach_data,
        @JsonProperty("neo_reference_id") String neo_reference_id,
        @JsonProperty("date") String date
    ) {
        super(
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
}