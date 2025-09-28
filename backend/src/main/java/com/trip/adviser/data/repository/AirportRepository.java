package com.trip.adviser.data.repository;

import com.trip.adviser.data.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AirportRepository extends JpaRepository<Airport, Long> {
    
    @Query(value = "SELECT * FROM airports WHERE UNACCENT(LOWER(municipality)) = UNACCENT(LOWER(:city)) AND scheduled_service = 'yes' order by priority limit 1", nativeQuery = true)
    Airport findAirportsByCity(@Param("city") String city);
}
