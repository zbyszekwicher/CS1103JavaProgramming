package application;
	
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Main extends Application {
	private String userInput = "";
	private ArrayList<String> searchHistory = new ArrayList<>();
	private WeatherFetcher weatherFetcher = new WeatherFetcher();
    private VBox historyBox = new VBox(5);
    private HBox forecastBox = new HBox(10);
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		// root and scene
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 600, 400);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
        primaryStage.setTitle("weather information");

        // top pane
        Label inputLabel = new Label("Enter the city:");
		TextField inputTextField = new TextField();
        Button searchButton = new Button("Search");
        searchButton.setDefaultButton(true);
        searchButton.setOnAction(e -> {
            userInput = inputTextField.getText();
            if ( ! userInput.isEmpty()) {
            	Search();
            }
        });
        HBox topBox = new HBox(10, inputLabel, inputTextField, searchButton);
        topBox.setPadding(new Insets(10));
        topBox.setAlignment(Pos.CENTER);
        root.setTop(topBox);
        
        // middle pane
        forecastBox.setPadding(new Insets(10));
        forecastBox.setAlignment(Pos.CENTER);
        forecastBox.getStyleClass().add("forecast-box");
        root.setCenter(forecastBox);

        // bottom pane
        RadioButton celsiusButton = new RadioButton("Celsius");
        RadioButton fahrenheitButton = new RadioButton("Fahrenheit");
        RadioButton scientificButton = new RadioButton("Scientific");
        ToggleGroup unitsGroup = new ToggleGroup();
        celsiusButton.setToggleGroup(unitsGroup);
        fahrenheitButton.setToggleGroup(unitsGroup);
        scientificButton.setToggleGroup(unitsGroup);
        celsiusButton.setSelected(true);
        
        unitsGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == celsiusButton) {
                weatherFetcher.setUnits(Units.METRIC);
            } else if (newValue == fahrenheitButton) {
            	weatherFetcher.setUnits(Units.FAHRENHEIT);
            } else if (newValue == scientificButton) {
            	weatherFetcher.setUnits(Units.SCIENTIFIC);
            } });
        
        HBox optionsBox = new HBox(10, new Label("Unit:"), celsiusButton, fahrenheitButton, scientificButton);
        optionsBox.setPadding(new Insets(10));
        optionsBox.setAlignment(Pos.CENTER_RIGHT);
        root.setBottom(optionsBox);
        
		// right pane
        historyBox.setPadding(new Insets(10));
        historyBox.getStyleClass().add("history-box");
        Label historyLabel = new Label("Search History:");
        VBox rightBox = new VBox(10, historyLabel, historyBox);
        rightBox.setPadding(new Insets(10));
        rightBox.setAlignment(Pos.TOP_CENTER);
        root.setRight(rightBox);

        // showing an image if it is an evening
 		LocalTime currentTime = LocalTime.now();
        if (currentTime.isAfter(LocalTime.of(18, 0)) && currentTime.isBefore(LocalTime.of(21, 0))) {
        	root.getStyleClass().add("evening-background");
        } else {
        	root.getStyleClass().add("defalut-background");
		}
        
		primaryStage.show();
	}
	
	// This method uses the WeatherFetcher class to get the data on the weather
	private void Search() {
		try {
			WeatherData weatherData = weatherFetcher.getWeatherData(userInput);
			displayForecast(weatherData);
			addToSearchHistory(userInput);
			//LogData(weatherData);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			forecastBox.getChildren().clear();
			forecastBox.getChildren().add(new Label("INVALID INPUT"));
		}
	}
	
	// This method updates the middle pane, according to the WeatherData passed as an argument
	private void displayForecast(WeatherData weatherData) {
		forecastBox.getChildren().clear();
		for (WeatherData.ForecastData data : weatherData.getForecastArray()) {
			Label dateLabel = new Label(data.date);
			Label temperatureLabel = new Label("Temperature: " + data.temperature + weatherData.getUnits().getScale());
			
			// I do not have access to the weather forecast on this API, so this is the default output
			VBox dayBox = new VBox(1, dateLabel, new Label("Description: no access"), temperatureLabel, 
					new Label("Humidity: no access"), new Label("Wind speed: no access"), new Label("Precipitation: no access"), new Label("cloudCover: no access"));
			
			// ... But I have the access to the full current weather
			if (data.date.equals("current")) {
				ImageView weatherIcon = new ImageView(new Image(data.iconURL));
				weatherIcon.setFitWidth(50);
		        weatherIcon.setFitHeight(50);
		        
				Label descriptionLabel = new Label(data.description);
				Label humidityLabel = new Label("Humidity: " + data.humidity + "%");
				Label windSpeedLabel = new Label("Wind Speed: " + data.windSpeed + " km/h");
				Label precipitationLabel = new Label("Precipitation: " + data.precip + " mm");
				Label cloudCoverLabel = new Label("Cloud Cover: " + data.cloudCover + "%");
				dayBox = new VBox(1, dateLabel, weatherIcon, descriptionLabel, temperatureLabel, 
			            humidityLabel, windSpeedLabel, precipitationLabel, cloudCoverLabel);
			}
			
			dayBox.setPadding(new Insets(10));
			dayBox.getStyleClass().add("day-box");
			dayBox.setAlignment(Pos.CENTER);
			
			forecastBox.getChildren().add(dayBox);
		}
	}
	
	// This method adds the new element to the search history, and displays a new item in the corresponding GUI part.
	private void addToSearchHistory(String city) {
    	String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        searchHistory.add(city);
        Label historyEntry = new Label(city + "\n(" + timestamp + ")");
        historyEntry.setOnMouseClicked(e -> {
            userInput = city;
            Search();
        });
        
        historyBox.getChildren().add(0, historyEntry);
    }
	
}
