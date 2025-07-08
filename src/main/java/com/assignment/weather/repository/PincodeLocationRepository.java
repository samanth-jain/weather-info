package com.assignment.weather.repository;

import com.assignment.weather.model.PincodeLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PincodeLocationRepository extends JpaRepository<PincodeLocation, Long> {
    Optional<PincodeLocation> findByPincode(String pincode);
} 