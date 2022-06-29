package com.example.demo;

import com.example.demo.RequestCache;
import org.springframework.boot.SpringApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.models.NasaApiResponse;
import com.example.demo.NasaApi;
import java.util.*;
import java.net.*;
import java.io.*;

/**
 * TODO:
 * - Show 10 asteroids that passed the closest to Earth between two user-provided dates
 * - Show characteristics of the largest asteroid (estimated diameter) passing Earth during a user-provided year
 */

@SpringBootApplication
@RestController
public class DemoApplication {
  private static String apiKey = System.getenv("NASA_API_KEY");

	public static void main(String[] args) {
    if (apiKey == null) {
      throw new RuntimeException("The environment variable NASA_API_KEY must be set!");
    }

		SpringApplication.run(DemoApplication.class, args);
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
