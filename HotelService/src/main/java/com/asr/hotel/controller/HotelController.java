package com.asr.hotel.controller;

import com.asr.hotel.entity.Hotels;
import com.asr.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @PostMapping("/hotel")
    public ResponseEntity<Hotels> createHotel(@RequestBody Hotels hotel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.createHotel(hotel));
    }

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
}
