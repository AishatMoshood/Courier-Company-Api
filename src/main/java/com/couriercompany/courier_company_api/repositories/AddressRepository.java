package com.couriercompany.courier_company_api.repositories;

import com.couriercompany.courier_company_api.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByLocationId(Long locationId);
}
