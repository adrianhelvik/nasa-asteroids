package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.*;
import java.net.*;

public class NasaApi {
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
