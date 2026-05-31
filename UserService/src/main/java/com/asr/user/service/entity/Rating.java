package com.asr.user.service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
@Entity
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id")
    private Long ratingId;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "hotel_id")
    private String hotelId;
    @Column(name = "rating")
    private int rating;
    @Column(name = "feedback", length = 1000)
    private String feedback;
}
