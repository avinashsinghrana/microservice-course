package com.asr.hotel.repository;

import com.asr.hotel.entity.Hotels;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotels, Long> {
}
