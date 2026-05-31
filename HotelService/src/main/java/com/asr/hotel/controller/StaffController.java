package com.asr.hotel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/staff")
public class StaffController {
    // Implement staff-related endpoints here
    @GetMapping
    public ResponseEntity<List<String>> getAllStaff() {
        List<String> staffList = Arrays.asList("John Doe", "Jane Smith", "Alice Johnson");
        return ResponseEntity.ok(staffList);
    }
}
