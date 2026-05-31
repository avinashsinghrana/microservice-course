package com.asr.user.service.external;

import com.asr.user.service.entity.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;

@FeignClient(name = "RATINGSERVICE")
public interface RatingServiceExternal {

    @GetMapping("/rating-service/ratings/users/{userId}")
    ArrayList<Rating> getRating(@PathVariable("userId") Long userId);
}
