package com.example.event_project.repository;

import com.example.event_project.model.Adress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// todo: literówka: address
public interface AdressRepository extends JpaRepository<Adress, Long> {

    Optional<Adress> findByCityAndAndStreetAndNumberOfBuilding(String city, String streed, String number);
}
