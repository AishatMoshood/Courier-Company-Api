package com.couriercompany.courier_company_api.repositories;

import com.couriercompany.courier_company_api.entities.Address;
import com.couriercompany.courier_company_api.entities.Location;
import com.couriercompany.courier_company_api.enums.LocationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findLocationByName(String name);

    boolean existsByName(String name);

    boolean existsByAddress(Address address);
}
