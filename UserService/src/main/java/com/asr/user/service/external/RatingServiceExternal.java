package com.asr.user.service.external;

import com.asr.user.service.entity.Rating;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.logging.Logger;

@FeignClient(name = "RATING-SERVICE")
public interface RatingServiceExternal {

    Logger logger = Logger.getLogger(RatingServiceExternal.class.getName());

    @CircuitBreaker(name = "rating-service", fallbackMethod = "getRatingByUserFallback")
    @GetMapping("/rating-service/ratings/users/{userId}")
    ArrayList<Rating> getRating(@PathVariable("userId") Long userId);

    default ArrayList<Rating> getRatingByUserFallback(Long userId, Throwable throwable) {
        logger.log(java.util.logging.Level.SEVERE, "Fallback method called for userId: " + userId, throwable);
        System.out.println("Fallback method called for userId: " + userId + " due to: " + throwable.getMessage());
        return new ArrayList<>();
    }
}
