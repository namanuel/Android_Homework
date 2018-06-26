package at.namanuel.bsaweatherapp;

import java.io.Serializable;
import java.util.Date;

public class Weather implements Serializable {
    String date;
    String icon;
    String description;
    String temp;
    double pressure;
    double humidity;
    double clouds;
    double windSpeed;
    double windDirection;
    double rain;
    double snow;
    int conditions;

    public Weather(String date, String icon, String description, String temp, double pressure, double humidity, double clouds, double windSpeed, double windDirection, double rain, double snow, int conditions) {
        this.date = date;
        this.icon = icon;
        this.description = description;
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.clouds = clouds;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.rain = rain;
        this.snow = snow;
        this.conditions = conditions;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getClouds() {
        return clouds;
    }

    public void setClouds(double clouds) {
        this.clouds = clouds;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
    }

    public double getRain() {
        return rain;
    }
    public void setRain(double rain) {
        this.rain = rain;
    }

    public double getSnow() { return snow;    }

    public void setSnow(double snow) {
        this.snow = snow;
    }
    public int getConditions() { return conditions;    }

    public void setSnow(int conditions) {
        this.conditions = conditions;
    }
}
