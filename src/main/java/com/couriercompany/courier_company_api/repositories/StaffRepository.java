package com.couriercompany.courier_company_api.repositories;

import com.couriercompany.courier_company_api.entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
}
