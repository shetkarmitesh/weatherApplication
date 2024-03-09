package com.weattherapp.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


@WebServlet("/ApiServlet1")
public class ApiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//		
//		PrintWriter out = response.getWriter();
//		out.print("hello");
		//		city name from user
		String cityName= request.getParameter("cityName");
//		api key fromo openweathermap
		String apiKey="dad25cb79869288e5d8b34c87f743e72";
//		api url for openweathermap api request
		String apiUrl= "https://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid="+apiKey;
		
		
//		API Integration
		
		URL url =new URL(apiUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
//		Reading the data from network
		InputStream inputStream = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(inputStream);
		
//		want to store in string
		StringBuilder responseContent = new StringBuilder();
		
		
//		to take input from reader create scanner object 
		Scanner sc = new Scanner(reader);
		
		while (sc.hasNext()) {
			responseContent.append(sc.nextLine());
		}
		
		sc.close();
		System.out.println(responseContent);
		
//		Typing casting = parsing data into json format
		Gson gson= new Gson();
		JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
		System.out.println(jsonObject);
		
//		Date and time
		long dateTimestamp = jsonObject.get("dt").getAsLong()*1000;
		String date = new Date(dateTimestamp).toString();
		
		System.out.println(date);
		
//		temperature
		double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int temperatureCelsius = (int) (temperatureKelvin-273.15);
		System.out.println(temperatureCelsius);
		
		//Humidity
        int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
        
        //Wind Speed
        double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
        
        //Weather Condition
        String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
        
     // Set the data as request attributes (for sending to the jsp page)
        request.setAttribute("date", date);
        request.setAttribute("city", cityName);
        request.setAttribute("temperature", temperatureCelsius);
        request.setAttribute("humidity", humidity);
        request.setAttribute("windSpeed", windSpeed);
        request.setAttribute("weatherCondition", weatherCondition);
        request.setAttribute("weatherData", responseContent.toString());
        
        connection.disconnect();
        
        // Forward the request to the weather.jsp page for rendering
        request.getRequestDispatcher("index.jsp").forward(request, response);
	}

}
