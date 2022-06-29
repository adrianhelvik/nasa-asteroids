package com.adrianhelvik.asteroids;

import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.adrianhelvik.asteroids.RequestCache;
import com.adrianhelvik.asteroids.models.*;
import org.springframework.http.HttpStatus;
import com.adrianhelvik.asteroids.*;
import java.util.*;
import java.net.*;
import java.io.*;

@SpringBootApplication
@RestController
public class AsteroidsApi {
  private static String apiKey = System.getenv("NASA_API_KEY");

  /**
   * Error notifier.
   *
   * I would use environment dependent dependency injection to
   * inject this property, but as it's my first day using Spring
   * I'll leave it out for now.
   *
   * In production I would have probably use Sentry or Rollbar.
   * I would probably also split the api across multiple controllers. 😀
   */
  private ErrorNotifier errorNotifier = new DummyErrorNotifier();

	public static void main(String[] args) {
    if (apiKey == null) {
      throw new RuntimeException("The environment variable NASA_API_KEY must be set!");
    }

		SpringApplication.run(AsteroidsApi.class, args);
	}

  @GetMapping("/")
  public String documentation() {
    var buffer = new StringBuffer();

    buffer.append("Documentation:");

    return buffer.toString();
  }

  /**
   * Retrieve all asteroids between two dates.
   *
   * @param from  The initial date using the format yyyy-mm-dd
   * @param to    The final date (inclusive) using the format yyyy-mm-dd
   */
  @GetMapping("/v1/asteroids")
  public ResponseEntity<String> getAsteroids(
    @RequestParam("from") String from,
    @RequestParam("to") String to
  ) throws Exception {
    var allAsteroidsResponse = new NasaApi(apiKey)
      .from(from)
      .to(to)
      .request();

    var allAsteroids = allAsteroidsResponse.data;
    var element_count = allAsteroidsResponse.element_count;

    var asteroids = new ArrayList<Asteroid>();
    var perPage = 10;

    for (var asteroid : allAsteroids) {
      asteroids.add(asteroid);
      if (asteroids.size() >= perPage) {
        break;
      }
    }

    return ResponseEntity.status(200).body(
      new ObjectMapper().writeValueAsString(
        new ApiResponse<Asteroid>(
          asteroids,
          element_count 
        )
      )
    );
  }

  /**
   * Retrieve the largest asteroid.
   *
   * The reason why I decided against GET /v1/asteroids/largest/{year},
   * is that we rely on an external API, and we have no real way of
   * knowing whether "largest" is going to be used as an ID.
   * It seems pretty improbable that this will happen in a Nasa API,
   * but generally I prefer to avoid potentially ambigous patterns.
   *
   * @param potentiallyHazardous Show the largest *hazardous* asteroid instead.
   */
  @GetMapping("/v1/largest-asteroid/{year}")
  public ResponseEntity<String> getLargestAstroids(
    @PathVariable("year") int year,
    @RequestParam(name = "potentially-hazardous", defaultValue = "false") boolean potentiallyHazardous
  ) throws Exception {
    var allAsteroidsResponse = new NasaApi(apiKey)
      .from(Integer.toString(year) + "-01-01")
      .to(Integer.toString(year) + "-12-31")
      .request();

    var asteroids = allAsteroidsResponse.data;
    var element_count = allAsteroidsResponse.element_count;

    Asteroid largest = null;

    for (var asteroid : asteroids) {
      if (potentiallyHazardous && !asteroid.is_potentially_hazardous_asteroid) {
        continue;
      }
      if (largest == null || asteroid.absolute_magnitude_h > largest.absolute_magnitude_h) {
        largest = asteroid;
      }
    }

    return ResponseEntity.status(200).body(
      new ObjectMapper().writeValueAsString(largest)
    );
  }

  @ExceptionHandler
  public ResponseEntity<String> exceptionHandler(Throwable throwable) {
    if (throwable instanceof ResponseStatusException) {
      var exception = (ResponseStatusException) throwable;
      return ResponseEntity.status(exception.getStatus()).body(exception.getReason());
    } else if (throwable instanceof MissingServletRequestParameterException) {
      var exception = (MissingServletRequestParameterException) throwable;
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    } else {
      errorNotifier.report(throwable);
      return ResponseEntity.status(500).body("Something went wrong!");
    }
  }
}
