package com.asr.hotel.service;

import com.asr.hotel.entity.Hotels;

import java.util.List;

public interface HotelService {
    Hotels createHotel(Hotels hotel);

    List<Hotels> getAllHotels();

    Hotels getHotelById(Long id);

    Hotels updateHotel(Hotels hotel);

    String deleteHotel(Long id);
}
