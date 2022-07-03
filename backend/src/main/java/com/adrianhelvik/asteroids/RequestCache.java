package com.adrianhelvik.asteroids;

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

import static com.adrianhelvik.asteroids.RedisSingleton.redis;

import java.net.http.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.net.*;
import java.io.*;

public class RequestCache {
    private static final int cacheSeconds = Integer.parseInt(System.getenv("CACHE_SECONDS"));

    private static String getFromCache(String url) throws Exception {
      String cached;
      int tries = 0;

      while (true) {
        var requesting = redis.get("requesting:" + url);
        cached = redis.get("request:" + url);

        if (cached == null && requesting != null && tries < 10) {
          System.out.println("Failed to get response from cache");
          Thread.sleep(1);
          tries++;
        } else {
          break;
        }
      }

      return cached;
    }

    public static String get(String url) throws Exception {
        System.out.println("GET " + url);
        String cached = getFromCache(url);

        if (cached != null) {
            System.out.println("Returned cached response");
            return cached;
        }

        var errorStatus = redis.get("failure:" + url);

        if (errorStatus != null) failure(errorStatus);

        redis.setex("requesting:" + url, 5, "yes");

        var response = get(new URL(url));

        redis.setex("request:" + url, cacheSeconds, response);

        return response;
    }

    static void failure(String errorStatus) throws Exception {
        throw new Exception("Request to NASA API failed with status code " + errorStatus + "!");
    }

    private static String get(URL url) throws Exception {
        System.out.println("Requesting data from " + url.toString());

        var connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int status = connection.getResponseCode();

        if (status != 200) {
            redis.setex("failure:" + url, 5, Integer.toString(status));
            failure(Integer.toString(status));
        }

        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        connection.disconnect();

        return content.toString();
    }
}
