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
import io.lettuce.core.api.sync.*;
import io.lettuce.core.api.*;
import io.lettuce.core.*;
import java.net.http.*;
import java.util.*;
import java.net.*;
import java.io.*;

/**
 * I would refactor this into an autowired dependency.
 */
public class RedisSingleton {
  public static RedisCommands<String, String> redis;
  private static int cacheSeconds = Integer.parseInt(System.getenv("CACHE_SECONDS"));

  static {
    RedisClient redisClient = RedisClient.create(System.getenv("REDIS_URL"));
    StatefulRedisConnection<String, String> connection = redisClient.connect();
    redis = connection.sync();
  }
}
