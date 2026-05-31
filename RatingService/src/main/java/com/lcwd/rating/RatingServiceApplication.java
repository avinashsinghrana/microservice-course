package com.lcwd.rating;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class RatingServiceApplication {



	public static void main(String[] args) {
		SpringApplication.run(RatingServiceApplication.class, args);
	}


	/**
	 * If we want to call any other service without using IP and Host name, we can use RestTemplate and annotate it with @LoadBalanced to enable client-side load balancing. This way, we can call other services using their service names registered in the service registry (like Eureka) instead of hardcoding IP addresses or hostnames.
	 * For example, if we want to call the Hotel Service from the Rating Service, we can use RestTemplate to make a REST call to the Hotel Service using its service name (e.g., "HOTELSERVICE") instead of hardcoding the IP address or hostname of the Hotel Service. This allows for better scalability and flexibility in a microservices architecture.
	 * @LoadBalanced - annotation is used to indicate that the RestTemplate should use Ribbon for client-side load balancing. This means that when we make a REST call using this RestTemplate, it will automatically distribute the requests across multiple instances of the target service (if there are multiple instances registered in the service registry) based on the load balancing strategy configured in Ribbon.
	 * @return
	 */
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
