package com.couriercompany.courier_company_api.repositories;

import com.couriercompany.courier_company_api.entities.Location;
import com.couriercompany.courier_company_api.enums.LocationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("select l from Location l where l.name = ?1 and l.locationType= ?2")
    Optional<Location> findByLocationNameAndType(String name, LocationType type);

    Optional<Location> findLocationByName(String name);
}
