package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import org.json.JSONObject;

public class WeatherFetcher {
    private static final String API_KEY = "eb245b79f8ca46028a2eddd6840619da";
    private static final String BASE_URL = "http://api.weatherstack.com/forecast";
    private Preferences Preferences;
    
    public WeatherFetcher() {
    	this.Preferences = new Preferences();
	}
    
    // The free access to the weatherstack API does not allow for all the functionalities
    public WeatherData getWeatherData(String city) throws Exception {
    	String urlString  = BASE_URL
    			+ "?access_key=" + API_KEY
    			+ "&query=" + city
    			+ "&units=" + Preferences.units;
    	
    	/*
    	if (Preferences.isForecast) {
    		//	I DO NOT HAVE ACCESS TO THESE FEATURES
    		urlString  += "&forecast_days=" + Preferences.forecast_days 
					+ "&hourly=" + Preferences.hourly
				    + "&interval=" + Preferences.interval;
		}*/
    	
    	// Getting the response from the server
    	HttpURLConnection connection = null;
    	WeatherData weatherData;
    	try {
    		URL requestURL = new URI(urlString).toURL();
        	connection = (HttpURLConnection) requestURL.openConnection();
            connection.setRequestMethod("GET");
            
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
            	throw new Exception("API request failed " + responseCode);
            }
            
            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            
            while ( (inputLine = input.readLine()) != null) {
                response.append(inputLine);
            }
            input.close();
            
            // Creating a new JSON object for easy data fetching
            JSONObject jsonResponse = new JSONObject(response.toString());
            weatherData = new WeatherData(city, Preferences.units);

            // Getting current weather
            String date = "current";
    		String iconURL = jsonResponse.getJSONObject("current").getJSONArray("weather_icons").getString(0);
    		String description = jsonResponse.getJSONObject("current").getJSONArray("weather_descriptions").getString(0);
    		int temperature = jsonResponse.getJSONObject("current").optInt("temperature");
    		int humidity = jsonResponse.getJSONObject("current").optInt("humidity");
    		int windSpeed = jsonResponse.getJSONObject("current").optInt("wind_speed");
    		int precip = jsonResponse.getJSONObject("current").optInt("precip");
    		int cloudCover = jsonResponse.getJSONObject("current").optInt("cloudcover");
    		weatherData.addNewForecastData(date, iconURL, description, temperature, humidity, windSpeed, precip, cloudCover);
    		
    		// Getting the forecast
    		if (Preferences.isForecast) {
    			JSONObject forecast = jsonResponse.getJSONObject("forecast");
    			Iterator<String> keys = forecast.keys();
    			while (keys.hasNext()) {
    				String dateKey = keys.next();
    	            JSONObject dateObject = forecast.getJSONObject(dateKey);
    	            int avgTemp = dateObject.getInt("avgtemp");
    	            weatherData.addNewForecastData(dateKey, null, null, avgTemp, 0, 0, 0, 0);
				}
			}
        
    	// If any exception was thrown, I do not want to return anything, so I throw a new Exception for the Main program to handle.
		} catch (URISyntaxException e) {
			throw new Exception("URISyntaxException: "+ e.getMessage());
		} catch (IOException e) {
			throw new Exception("IOException: " + e.getMessage());
		} finally {
			if (connection != null) {
		        connection.disconnect();
		    }
		}
    	
    	return weatherData;
    }
    
    // Methods for updating preferences
    public void setPreferences(boolean isForecast) {
    	Preferences.isForecast = isForecast;
	}
    public void setPreferences(boolean isForecast, int forecast_days, int hourly, int interval) {
    	Preferences.isForecast = isForecast;
    	Preferences.forecast_days = forecast_days;
    	Preferences.hourly = hourly;
    	Preferences.interval = interval;
	}
    public void setUnits(Units units) {
		Preferences.units = units;
	}
    
    private class Preferences {
    	boolean isForecast = true;
    	int forecast_days = 7;
    	int hourly = 1;
    	int interval = 3;
    	Units units = Units.METRIC;
    }
}

