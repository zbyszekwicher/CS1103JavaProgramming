package application;

import java.util.ArrayList;

public class WeatherData {
	private String city;
	private ArrayList<ForecastData> forecastArray;
	private Units units;
	
	// This class represents a weather forecast for a certain location for some amount of time
	// Data for each day (or time interval) is added as an instance of the ForecastData to the forecastArray
	public WeatherData(String city, Units units) {
		this.forecastArray = new ArrayList<>();
		this.city = city;
		this.units = units;
	}
	
	// Adding new data to the forecastArray
	public void addNewForecastData(String date, String iconURL, String description,
			int temperature, int humidity, int windSpeed, int precip, int cloudCover) {
		forecastArray.add(new ForecastData(date, iconURL, description, temperature, humidity, windSpeed, precip, cloudCover));
	}
	
	// Getters
	public ArrayList<ForecastData> getForecastArray() {
		return forecastArray;
	}
	public String getCity() {
        return city;
    }
    public Units getUnits() {
        return units;
    }
	
    // This inner class stores weather data for a certain date
	public class ForecastData { 
		String date;
		String iconURL;
		String description;
		int temperature;
		int humidity;
		int windSpeed;
		int precip;
		int cloudCover;
		
		public ForecastData(
				String date,
				String iconURL,
				String description,
				int temperature,
				int humidity,
				int windSpeed,
				int precip,
				int cloudCover) {
			this.date = date;
			this.iconURL = iconURL;
			this.description = description;
			this.temperature = temperature;
			this.humidity = humidity;
			this.windSpeed = windSpeed;
			this.precip = precip;
			this.cloudCover = cloudCover;
		}
	}
}
