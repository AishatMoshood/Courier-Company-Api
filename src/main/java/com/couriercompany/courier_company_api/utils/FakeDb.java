package com.couriercompany.courier_company_api.utils;

import com.couriercompany.courier_company_api.entities.Location;
import com.couriercompany.courier_company_api.enums.LocationType;
import com.couriercompany.courier_company_api.repositories.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class FakeDb {

    @Bean
    @Qualifier("MyOtherCommand")
    public CommandLineRunner myCommandLineRunner(LocationRepository locationRepository) {
        return argument -> {
            if(!locationRepository.existsById(1L)) {
                Location location1 = Location.builder()
                        .name("National Stadium")
                        .locationType(LocationType.ORIGIN)
                        .longitude(6.4999)
                        .latitude(3.3609)
                        .build();

                Location location2 = Location.builder()
                        .name("Oriental Hotel")
                        .locationType(LocationType.DESTINATION)
                        .longitude(6.4358)
                        .latitude(3.4447)
                        .build();

                List<Location> locations = List.of(location1,location2);
                locationRepository.saveAll(locations);
            }
        };
    }
}