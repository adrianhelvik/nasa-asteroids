package com.adrianhelvik.asteroids;

import com.adrianhelvik.asteroids.ErrorNotifier;

public class DummyErrorNotifier implements ErrorNotifier {
  public void report(Throwable e) {
    System.out.println("-------------------------------");
    System.out.println("----- Unhandled exception -----");
    System.out.println("-------------------------------");
    e.printStackTrace();
    System.out.println("-------------------------------");
  }
}
