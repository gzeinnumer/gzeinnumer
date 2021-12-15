package com.app.traveling.data;

import android.app.Application;

import com.app.traveling.model.Airport;
import com.app.traveling.model.Flight;
import com.app.traveling.model.FlightBooking;
import com.app.traveling.model.Hotel;
import com.app.traveling.model.HotelBooking;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariable extends Application {

    private static GlobalVariable mInstance;
    private Flight flight;
    private Hotel hotel;
    private List<Airport> airports = new ArrayList<>();
    private List<String> cities = new ArrayList<>();
    private List<FlightBooking> flightBookings= new ArrayList<>();
    private List<HotelBooking> hotelBookings = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        airports = Constant.getAirportData(this);
        cities = Constant.getCityData(this);
        flightBookings = Constant.getFlightBookingData(this);
        hotelBookings = Constant.getHotelBookingData(this);

        flight = new Flight();
        flight.origin = airports.get(10);
        flight.destination = airports.get(airports.size() - 1);

        hotel = new Hotel();
        hotel.city = cities.get(cities.size() - 1);

    }

    public static synchronized GlobalVariable getInstance() {
        return mInstance;
    }

    public List<Airport> getAirports() {
        return airports;
    }

    public Flight getFlight() {
        return flight;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public List<String> getCities() {
        return cities;
    }

    public List<FlightBooking> getFlightBookings() {
        return flightBookings;
    }

    public List<HotelBooking> getHotelBookings() {
        return hotelBookings;
    }
}
