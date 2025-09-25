package com.trip.adviser.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trip.adviser.data.entity.*;

@Repository
public interface GeonameRepository extends JpaRepository<Geoname, Integer> {
}
