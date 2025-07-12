package org.danila.weather;

public enum Condition {
    SUNNY("солнечно"),
    CLOUDY("облачно"),
    RAIN("дождь");

    private final String ru;

    Condition(String ru) { this.ru = ru; }

    @Override public String toString() { return ru; }
}
