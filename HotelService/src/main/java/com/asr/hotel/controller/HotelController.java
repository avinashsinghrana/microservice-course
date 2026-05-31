package com.asr.hotel.controller;

import com.asr.hotel.entity.Hotels;
import com.asr.hotel.service.HotelService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @PostMapping("/hotel")
    public ResponseEntity<Hotels> createHotel(@RequestBody Hotels hotel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.createHotel(hotel));
    }

    @CircuitBreaker(name = "hotel-service", fallbackMethod = "getHotelByIdFallback")
    @GetMapping("/hotels/{id}")
    public ResponseEntity<Hotels> getHotelById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @GetMapping("/hotels")
    public ResponseEntity<?> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @PutMapping("/upt/hotel")
    public ResponseEntity<Hotels> updateHotel(@RequestBody Hotels hotel) {
        return ResponseEntity.ok(hotelService.updateHotel(hotel));
    }

    @DeleteMapping("/delete/hotels/{id}")
    public ResponseEntity<String> deleteHotel(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.deleteHotel(id));
    }

    public ResponseEntity<Hotels> getHotelByIdFallback(Long id, Throwable throwable) {
        log.error("Fallback method called for hotelId: {} due to: {}", id, throwable.getMessage());
        Hotels fallbackHotel = new Hotels();
        return ResponseEntity.ok(fallbackHotel);
    }
}
