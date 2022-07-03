package com.adrianhelvik.asteroids.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public record OrbitalData(String orbit_id, String orbit_determination_date, String first_observation_date,
                          String last_observation_date, int data_arc_in_days, int observations_used,
                          String orbit_uncertainty, String minimum_orbit_intersection,
                          String jupiter_tisserand_invariant, String epoch_osculation, String eccentricity,
                          String semi_major_axis, String inclination, String ascending_node_longitude,
                          String orbital_period, String perihelion_distance, String perihelion_argument,
                          String aphelion_distance, String perihelion_time, String mean_anomaly, String mean_motion,
                          String equinox,
                          OrbitClass orbit_class) {
    public OrbitalData(
            @JsonProperty("orbit_id") String orbit_id,
            @JsonProperty("orbit_determination_date") String orbit_determination_date,
            @JsonProperty("first_observation_date") String first_observation_date,
            @JsonProperty("last_observation_date") String last_observation_date,
            @JsonProperty("data_arc_in_days") int data_arc_in_days,
            @JsonProperty("observations_used") int observations_used,
            @JsonProperty("orbit_uncertainty") String orbit_uncertainty,
            @JsonProperty("minimum_orbit_intersection") String minimum_orbit_intersection,
            @JsonProperty("jupiter_tisserand_invariant") String jupiter_tisserand_invariant,
            @JsonProperty("epoch_osculation") String epoch_osculation,
            @JsonProperty("eccentricity") String eccentricity, @JsonProperty("semi_major_axis") String semi_major_axis,
            @JsonProperty("inclination") String inclination,
            @JsonProperty("ascending_node_longitude") String ascending_node_longitude,
            @JsonProperty("orbital_period") String orbital_period,
            @JsonProperty("perihelion_distance") String perihelion_distance,
            @JsonProperty("perihelion_argument") String perihelion_argument,
            @JsonProperty("aphelion_distance") String aphelion_distance,
            @JsonProperty("perihelion_time") String perihelion_time, @JsonProperty("mean_anomaly") String mean_anomaly,
            @JsonProperty("mean_motion") String mean_motion, @JsonProperty("equinox") String equinox,
            @JsonProperty("orbit_class") OrbitClass orbit_class
    ) {
        this.orbit_id = orbit_id;
        this.orbit_determination_date = orbit_determination_date;
        this.first_observation_date = first_observation_date;
        this.last_observation_date = last_observation_date;
        this.data_arc_in_days = data_arc_in_days;
        this.observations_used = observations_used;
        this.orbit_uncertainty = orbit_uncertainty;
        this.minimum_orbit_intersection = minimum_orbit_intersection;
        this.jupiter_tisserand_invariant = jupiter_tisserand_invariant;
        this.epoch_osculation = epoch_osculation;
        this.eccentricity = eccentricity;
        this.semi_major_axis = semi_major_axis;
        this.inclination = inclination;
        this.ascending_node_longitude = ascending_node_longitude;
        this.orbital_period = orbital_period;
        this.perihelion_distance = perihelion_distance;
        this.perihelion_argument = perihelion_argument;
        this.aphelion_distance = aphelion_distance;
        this.perihelion_time = perihelion_time;
        this.mean_anomaly = mean_anomaly;
        this.mean_motion = mean_motion;
        this.equinox = equinox;
        this.orbit_class = orbit_class;
    }
}

