package com.adrianhelvik.asteroids;

public interface ErrorNotifier {
  public void report(Throwable exception);
}
