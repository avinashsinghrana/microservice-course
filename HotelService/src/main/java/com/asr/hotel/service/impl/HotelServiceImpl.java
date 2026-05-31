package com.asr.hotel.service.impl;

import com.asr.hotel.entity.Hotels;
import com.asr.hotel.repository.HotelRepository;
import com.asr.hotel.service.HotelService;
import com.asr.hotel.exception.HotelServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public Hotels createHotel(Hotels hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    public List<Hotels> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotels getHotelById(Long id) {
        return hotelRepository.getReferenceById(id);
    }

    @Override
    public Hotels updateHotel(Hotels hotel) {
        Hotels referenceById = hotelRepository.getReferenceById(hotel.getId());
        if (referenceById == null) {
            throw new HotelServiceException("Hotel with id " + hotel.getId() + " not found.");
        }
        return hotelRepository.save(hotel);
    }

    @Override
    public String deleteHotel(Long id) {
        hotelRepository.deleteById(id);
        return "Hotel with id " + id + " has been deleted.";
    }
}
