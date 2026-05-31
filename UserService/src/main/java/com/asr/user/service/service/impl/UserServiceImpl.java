package com.asr.user.service.service.impl;

import com.asr.user.service.entity.Rating;
import com.asr.user.service.entity.User;
import com.asr.user.service.exception.UserServiceException;
import com.asr.user.service.repository.UserRepository;
import com.asr.user.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public User saveUser(User user) {
        log.info("Saving user: {}", user);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        User referenceById = userRepository.getReferenceById(id);
        if (referenceById == null) {
            log.warn("User with id: {} not found", id);
            throw new UserServiceException("User not found with id: " + id);
        }
        ArrayList<Rating> response = restTemplate.getForObject("http://RATINGSERVICE/ratings/users/" + referenceById.getId(), ArrayList.class);
        log.info("Fetched ratings for user with id: {}: {}", id, response);
        referenceById.setRatings(response);
        return referenceById;
    }

    @Override
    public String deleteUserById(Long id) {
        log.info("Deleting user with id: {}", id);
        userRepository.deleteById(id);
        return "User deleted with id: " + id;
    }
}
