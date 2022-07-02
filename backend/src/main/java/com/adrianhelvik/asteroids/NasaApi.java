package com.adrianhelvik.asteroids;

import org.springframework.web.server.ResponseStatusException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.adrianhelvik.asteroids.models.*;
import org.springframework.http.HttpStatus;
import com.adrianhelvik.asteroids.*;
import java.util.ArrayList;
import java.util.regex.*;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.net.*;

public class NasaApi {
  String apiKey;

  static Pattern datePattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

  private final String baseUrl = "https://api.nasa.gov/neo/rest/v1/feed";
  private String from = null;
  private String to = null;
  private String json = null;

  public NasaApi(String apiKey) {
    this.apiKey = apiKey;
  }

  private String padStart(String base, int length, char padding) {
    var result = base;
    while (result.length() < length) {
      result = padding + base;
    }
    return result;
  }

  private String fmtDate(Date date) {
    var calendar = Calendar.getInstance();

    calendar.setTime(date);

    var year = calendar.get(Calendar.YEAR);
    var month = calendar.get(Calendar.MONTH) + 1;
    var day = calendar.get(Calendar.DAY_OF_MONTH);

    var result = Integer.toString(year)
      + "-"
      + padStart(Integer.toString(month), 2, '0')
      + "-"
      + padStart(Integer.toString(day), 2, '0');

    return result;
  }

  /**
   * Set the `to` parameter.
   *
   * @param from A date string on the format yyyy-mm-dd.
   */
  public NasaApi from(String from) {
    this.from = from;
    return this;
  }

  /**
   * Set the `to` parameter using a Date.
   *
   * @param from A Date object.
   */
  public NasaApi from(Date from) {
    this.from = fmtDate(from);
    return this;
  }

  /**
   * Set the `from` parameter.
   *
   * @param to A date string on the format yyyy-mm-dd.
   */
  public NasaApi to(String to) {
    this.to = to;
    return this;
  }

  /**
   * Set the `from` parameter using a Date object.
   *
   * @param to A Date object.
   */
  public NasaApi to(Date to) {
    this.to = fmtDate(to);
    return this;
  }

  /**
   * Run the request against the cached Nasa API.
   */
  public List<Asteroid> request() throws Exception {
    var apiResponse = NasaApiResponse.fromJson(performRequest());
    var asteroids = new ArrayList<Asteroid>();

    for (Asteroid asteroid : apiResponse.asteroids) {
      asteroids.add(asteroid);
    }

    return asteroids;
  }

  private String param(String input) throws Exception {
    return URLEncoder.encode(input, "UTF-8");
  }

  private boolean isValidDate(String date) {
    if (date == null) return false;
    return datePattern.matcher(date).find();
  }

  private void validateDate(String name, String date) {
    if (!isValidDate(date)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The parameter '" + name + "' must be a string using the format 'yyyy-mm-dd'");
  }

  private void validateParams() throws Exception {
    validateDate("from", from);
    validateDate("to", to);
    if (apiKey == null) throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The parameter 'apiKey' must be configured");
  }

  String performRequest() throws Exception {
    validateParams();
    return RequestCache.get(baseUrl + "?start_date=" + param(from) + "&end_date=" + param(to) + "&api_key=" + param(apiKey));
  }
}
