package com.adrianhelvik.asteroids.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties({"designation", "links", "orbital_data", "nasa_jpl_url", "is_sentry_object"})
public class DetailedAsteroid extends Asteroid {
    public final OrbitalData orbital_data;

    public DetailedAsteroid(
            @JsonProperty("id") String id, @JsonProperty("name") String name,
            @JsonProperty("absolute_magnitude_h") float absolute_magnitude_h,
            @JsonProperty("estimated_diameter") Map<String, EstimatedDiameter> estimated_diameter,
            @JsonProperty("is_potentially_hazardous_asteroid") boolean is_potentially_hazardous_asteroid,
            @JsonProperty("close_approach_data") List<CloseApproachData> close_approach_data,
            @JsonProperty("neo_reference_id") String neo_reference_id, @JsonProperty("date") String date,
            @JsonProperty("orbital_data") OrbitalData orbital_data
    ) {
        super(id, name, absolute_magnitude_h, estimated_diameter, is_potentially_hazardous_asteroid,
              close_approach_data, neo_reference_id, date
        );
        this.orbital_data = orbital_data;
    }
}
@JsonIgnoreProperties({ "orbit_class" })
class OrbitalData {
    public final String orbit_id;
    public final String orbit_determination_date;
    public final String first_observation_date;
    public final String last_observation_date;
    public final int data_arc_in_days;
    public final int observations_used;
    public final String orbit_uncertainty;
    public final String minimum_orbit_intersection;
    public final String jupiter_tisserand_invariant;
    public final String epoch_osculation;
    public final String eccentricity;
    public final String semi_major_axis;
    public final String inclination;
    public final String ascending_node_longitude;
    public final String orbital_period;
    public final String perihelion_distance;
    public final String perihelion_argument;
    public final String aphelion_distance;
    public final String perihelion_time;
    public final String mean_anomaly;
    public final String mean_motion;
    public final String equinox;

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
            @JsonProperty("eccentricity") String eccentricity,
            @JsonProperty("semi_major_axis") String semi_major_axis,
            @JsonProperty("inclination") String inclination,
            @JsonProperty("ascending_node_longitude") String ascending_node_longitude,
            @JsonProperty("orbital_period") String orbital_period,
            @JsonProperty("perihelion_distance") String perihelion_distance,
            @JsonProperty("perihelion_argument") String perihelion_argument,
            @JsonProperty("aphelion_distance") String aphelion_distance,
            @JsonProperty("perihelion_time") String perihelion_time,
            @JsonProperty("mean_anomaly") String mean_anomaly,
            @JsonProperty("mean_motion") String mean_motion,
            @JsonProperty("equinox") String equinox
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
    }
}
