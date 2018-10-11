package com.apap.tutorial5.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.apap.tutorial5.model.FlightModel;

/**
 *  FlightDB
 *  @author Priscilla Tiffany
 */
@Repository
public interface FlightDB extends JpaRepository<FlightModel, Long>{
	@Query("SELECT f FROM FlightModel f WHERE f.flightNumber = ?1")
	List<FlightModel> findFlightByName(String flightNumber);
}
