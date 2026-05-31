package com.lcwd.rating.services.impl;

import com.lcwd.rating.dto.Hotel;
import com.lcwd.rating.entities.Rating;
import com.lcwd.rating.repository.RatingRepository;
import com.lcwd.rating.services.RatingService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class RatingServiceImpl implements RatingService {


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RatingRepository repository;

    @Override
    public Rating create(Rating rating) {
        return repository.save(rating);
    }

    @Override
    public List<Rating> getRatings() {
        return repository.findAll();
    }

    @Override
    public List<Rating> getRatingByUserId(String userId) {
        List<Rating> ratings = repository.findByUserId(userId);
        for (Rating rating : ratings) {
            Hotel hotel = getHotelWithFallback(rating.getHotelId());
            rating.setHotel(hotel);
        }
        return ratings;
    }

    // 1. The Circuit Breaker monitors this method for failures (like timeouts, exceptions, etc.).
    @CircuitBreaker(name = "hotelServiceBreaker", fallbackMethod = "hotelFallback")
    public Hotel getHotelWithFallback(String hotelId) {
        String url = "http://HOTEL-SERVICE/hotel-service/hotels/" + hotelId;
        return restTemplate.getForObject(url, Hotel.class);
    }

    public Hotel hotelFallback(String hotelId, Throwable throwable) {
        log.error("Hotel service is down! Returning dummy hotel data. Error: {}", throwable.getMessage());

        Hotel dummyHotel = new Hotel();
        dummyHotel.setName("Temporary Hotel Data (Service Unavailable)");
        dummyHotel.setLocation("Unknown");
        dummyHotel.setAbout("This information is currently cached or unavailable because the underlying service timed out.");

        return dummyHotel;
    }

    @Override
    public List<Rating> getRatingByHotelId(String hotelId) {
        return repository.findByHotelId(hotelId);
    }
}
