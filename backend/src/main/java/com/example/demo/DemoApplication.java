package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.web.bind.annotation.PathVariable;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.api.sync.*;
import io.lettuce.core.api.*;
import io.lettuce.core.*;
import java.net.http.*;
import java.util.*;
import java.net.*;
import java.io.*;

// var urlString = "https://api.nasa.gov/neo/rest/v1/feed?start_date=" + URLEncoder.encode(startDate, "UTF-8") + "&end_date=" + URLEncoder.encode(endDate, "UTF-8") + "&api_key=" + apiKey();

/**
 * - Show 10 asteroids that passed the closest to Earth between two user-provided dates
 * - Show characteristics of the largest asteroid (estimated diameter) passing Earth during a user-provided year
 */

@SpringBootApplication
@RestController
public class DemoApplication {
  String apiKey = System.getenv("NASA_API_KEY");

	public static void main(String[] args) {
    var apiKey = System.getenv("NASA_API_KEY");

    if (System.getenv("NASA_API_KEY") == null) {
      throw new RuntimeException("The environment variable NASA_API_KEY must be set!");
    }

		SpringApplication.run(DemoApplication.class, args);
	}

  List<Asteroid> jsonToList(String json) throws Exception {
    return new ObjectMapper()
      .readValue(json, new TypeReference<List<Asteroid>>(){});
  }

  String toJson(NasaApiResponse response) throws Exception {
    return new ObjectMapper()
      .writeValueAsString(response);
  }

  @GetMapping("/")
  public ResponseEntity<String> hello() throws Exception {
    var asteroids = new NasaApi(apiKey)
      .from("2022-01-02")
      .to("2022-20-02")
      .request();

    return ResponseEntity.status(200).body(toJson(asteroids));
  }
}

/* REAL REQUEST
class RequestCache {
  private static String get(URL url) throws Exception {
    System.out.println("Requesting data from " + url.toString());

    var connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");

    int status = connection.getResponseCode();

    if (status != 200) {
      throw new Exception("Request to NASA API failed with status code " + Integer.toString(status) + "!");
    }

    BufferedReader in = new BufferedReader(
        new InputStreamReader(connection.getInputStream()));

    String inputLine;
    StringBuffer content = new StringBuffer();
    while ((inputLine = in.readLine()) != null) {
      content.append(inputLine);
    }

    in.close();
    connection.disconnect();

    return content.toString();
  }
  
  public static String get(String url) throws Exception {
    return get(new URL(url));
  }
}
*/

class RequestCache {
  private static RedisCommands<String, String> syncCommands;
  private static int cacheSeconds = Integer.parseInt(System.getenv("CACHE_SECONDS"));

  static {
    RedisClient redisClient = RedisClient.create(System.getenv("REDIS_URL"));
    StatefulRedisConnection<String, String> connection = redisClient.connect();
    syncCommands = connection.sync();
  }

  private static String get(URL url) throws Exception {
    System.out.println("Requesting data from " + url.toString());

    var connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");

    int status = connection.getResponseCode();

    if (status != 200) {
      throw new Exception("Request to NASA API failed with status code " + Integer.toString(status) + "!");
    }

    BufferedReader in = new BufferedReader(
        new InputStreamReader(connection.getInputStream()));

    String inputLine;
    StringBuffer content = new StringBuffer();
    while ((inputLine = in.readLine()) != null) {
      content.append(inputLine);
    }

    in.close();
    connection.disconnect();

    return content.toString();
  }
  
  public static String get(String url) throws Exception {
    var cached = syncCommands.get(url);

    if (cached != null) {
      System.out.println("Returned cached response");
      return cached;
    }

    var response = get(new URL(url));

    syncCommands.setex(url, cacheSeconds, response);

    return response;
  }
}

class NasaApi {
  String apiKey;

  private final String baseUrl = "https://api.nasa.gov/neo/rest/v1/feed";
  private String from = "";
  private String to = "";
  private String json = "";

  public NasaApi(String apiKey) {
    this.apiKey = apiKey;
  }

  public NasaApi from(String from) {
    this.from = from;
    return this;
  }

  public NasaApi to(String to) {
    this.to = to;
    return this;
  }

  String param(String input) throws Exception {
    return URLEncoder.encode(input, "UTF-8");
  }

  void validateParams() throws Exception {
    if (from == null) throw new Exception("from must be provided");
    if (to == null) throw new Exception("to must be provided");
    if (apiKey == null) throw new Exception("apiKey must be provided");
  }

  String performRequest() throws Exception {
    validateParams();
    return RequestCache.get(baseUrl + "?from=" + param(from) + "&to=" + param(to) + "&api_key=" + param(apiKey));
  }

  public NasaApiResponse request() throws Exception {
    return NasaApiResponse.fromJson(performRequest());
  }
}

@JsonIgnoreProperties({ "links" })
class NasaApiResponse {
  public final int element_count;
  public final Map<String, List<Asteroid>> near_earth_objects;

  private NasaApiResponse(
      @JsonProperty("element_count") int element_count,
      @JsonProperty("near_earth_objects") Map<String, List<Asteroid>> near_earth_objects
  ) {
    this.element_count = element_count;
    this.near_earth_objects = near_earth_objects;
  }

  public static NasaApiResponse fromJson(String json) throws Exception {
    return new ObjectMapper().readValue(json, new TypeReference<NasaApiResponse>(){});
  }
}

@JsonIgnoreProperties({ "links", "neo_reference_id", "nasa_jpl_url", "is_sentry_object" })
class Asteroid {
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

class EstimatedDiameter {
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

@JsonIgnoreProperties({ "close_approach_date_full", "epoch_date_close_approach" })
class CloseApproachData {
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

class MissDistance {
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

class RelativeVelocity {
  public final String kilometers_per_second;
  public final String kilometers_per_hour;
  public final String miles_per_hour;

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
