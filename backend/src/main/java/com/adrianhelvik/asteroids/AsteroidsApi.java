package com.adrianhelvik.asteroids;

import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.server.ResponseStatusException;
import static com.adrianhelvik.asteroids.RedisSingleton.redis;
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
@CrossOrigin(origins = "*")
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

    buffer.append("<!doctype html>");
    buffer.append("<style>body { font-family: sans-serif; }</style>");
    buffer.append("<meta charset=utf-8>");
    buffer.append("<h1>Routes:</h1>");
    buffer.append("<ul>");
    buffer.append("<li>GET /v1/asteroids?from={date}&to={date}</li>");
    buffer.append("<li>GET /v1/largest-asteroid/{year}</li>");
    buffer.append("</ul>");

    return buffer.toString();
  }

  /**
   * Retrieve all asteroids between two dates.
   *
   * TODO: Stream data from the Nasa API to achieve faster response times.
   *
   * @param from  The initial date using the format yyyy-mm-dd
   * @param to    The final date (inclusive) using the format yyyy-mm-dd
   */
  @GetMapping("/v1/asteroids")
  public ResponseEntity<String> getAsteroids(
    @RequestParam("from") String from,
    @RequestParam("to") String to,
    @RequestParam(name="page", defaultValue="1") int page
  ) throws Exception {
    if (page < 1) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The parameter 'page' must be greater than or equal to 1");
    }

    var allAsteroids = new NasaApi(apiKey)
      .from(from)
      .to(to)
      .request();

    var asteroids = new ArrayList<Asteroid>();
    int perPage = 10;
    int i = 0;

    for (var asteroid : allAsteroids) {
      if (i >= perPage * (page - 1)) {
        asteroids.add(asteroid);
      }

      i += 1;

      if (asteroids.size() >= perPage) {
        break;
      }
    }

    return ResponseEntity.status(200).body(
      new ObjectMapper().writeValueAsString(
        new ApiResponse<Asteroid>(
          asteroids,
          allAsteroids.size(),
          page,
          perPage
        )
      )
    );
  }

  @GetMapping("/v1/weekly-asteroids/{week}")
  public ResponseEntity<String> getAsteroidsInWeek(@PathVariable("week") int week) throws Exception {
    System.out.printf("Retrieving weekly asteroids for week: %d%n", week);

    var today = new Date();

    var calendar = Calendar.getInstance();
    calendar.setFirstDayOfWeek(Calendar.MONDAY);
    calendar.setTime(today);
    calendar.set(Calendar.WEEK_OF_YEAR, week);

    var year = calendar.get(Calendar.YEAR);

    calendar.setWeekDate(year, week, Calendar.MONDAY);
    var monday = calendar.getTime();

    calendar.setWeekDate(year, week, Calendar.SUNDAY);
    var finalDay = calendar.getTime();

    if (finalDay.after(today)) finalDay = today;

    var asteroids = new NasaApi(apiKey)
        .from(monday)
        .to(finalDay)
        .request();

    Collections.sort(asteroids, new Comparator<Asteroid>() {
      @Override
      public int compare(Asteroid a, Asteroid b) {
        if (a.miss_distance_km == b.miss_distance_km) return 0;
        if (a.miss_distance_km < b.miss_distance_km) return -1;
        return 1;
      }
    });

    // Store the asteroids for later retrieval. For a production
    // implementation I'd use a relational database instead of Redis.
    for (var asteroid : asteroids) {
      redis.set("asteroid:" + asteroid.id, new ObjectMapper().writeValueAsString(asteroid));
    }

    return ResponseEntity.status(200).body(
      new ObjectMapper().writeValueAsString(
        new ApiResponse<Asteroid>(
          asteroids,
          asteroids.size(),
          0,
          asteroids.size()
        )
      )
    );
  }

  @GetMapping("/v1/asteroids/{id}")
  public ResponseEntity<String> getAsteroidById(@PathVariable("id") String id) {
    var json = redis.get("asteroid:" + id);

    if (json == null) return ResponseEntity.status(404).body("Not found");

    return ResponseEntity.status(200).body(json);
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
    var asteroids = new NasaApi(apiKey)
      .from(Integer.toString(year) + "-01-01")
      .to(Integer.toString(year) + "-12-31")
      .request();

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
