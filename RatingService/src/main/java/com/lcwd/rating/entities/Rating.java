package com.lcwd.rating.entities;

import com.lcwd.rating.dto.Hotel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    private String userId;

    private String hotelId;

    private  int rating;

    private  String feedback;

    @Transient
    private Hotel hotel;
}
