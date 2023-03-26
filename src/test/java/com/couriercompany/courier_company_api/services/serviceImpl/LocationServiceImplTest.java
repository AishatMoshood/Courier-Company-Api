package com.couriercompany.courier_company_api.services.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.couriercompany.courier_company_api.entities.Address;
import com.couriercompany.courier_company_api.entities.Location;
import com.couriercompany.courier_company_api.enums.LocationType;
import com.couriercompany.courier_company_api.exceptions.AlreadyExistsException;
import com.couriercompany.courier_company_api.exceptions.EmptyListException;
import com.couriercompany.courier_company_api.exceptions.ResourceNotFoundException;
import com.couriercompany.courier_company_api.pojos.LocationPojo;
import com.couriercompany.courier_company_api.repositories.AddressRepository;
import com.couriercompany.courier_company_api.repositories.LocationRepository;
import com.couriercompany.courier_company_api.services.PersonService;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {LocationServiceImpl.class})
@ExtendWith(SpringExtension.class)
class LocationServiceImplTest {
    @MockBean
    private AddressRepository addressRepository;

    @MockBean
    private LocationRepository locationRepository;

    @Autowired
    private LocationServiceImpl locationServiceImpl;

    @MockBean
    private PersonService personService;

    @Test
    void addLocation_toReturnString() {
        LocationRepository locationRepository = mock(LocationRepository.class);
        when(locationRepository.existsByAddress(any())).thenReturn(true);
        when(locationRepository.existsByName(any())).thenReturn(true);
        PersonService personService = mock(PersonService.class);
        when(personService.confirmIfUserLoggedIn()).thenReturn(true);
        LocationServiceImpl locationServiceImpl = new LocationServiceImpl(locationRepository, mock(AddressRepository.class),
                personService);
        assertThrows(AlreadyExistsException.class, () -> locationServiceImpl
                .addLocation(new LocationPojo("Lagos Location", "13 Asajon Way", "sangotedo", "Lagos", "Nigeria", LocationType.ORIGIN)));
        verify(locationRepository).existsByName(any());
        verify(personService).confirmIfUserLoggedIn();
    }

    @Test
    void removeLocation_toReturnString() {
        Address address = new Address();
        address.setCity("Lagos");
        address.setCountry("Nig");
        address.setCreatedAt(Date.from(LocalDate.of(2023, 1, 13).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        address.setId(1L);
        address.setLocation(new Location());
        address.setState("Lagos");
        address.setStreet("Asajon");
        address.setUpdatedAt(Date.from(LocalDate.of(2023, 1, 13).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        address.updatedAt();

        Location location = new Location();
        location.setAddress(address);
        location.setCreatedAt(Date.from(LocalDate.of(2023, 1, 13).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        location.setId(1L);
        location.setLatitude(10.0d);
        location.setLocationType(LocationType.ORIGIN);
        location.setLongitude(10.0d);
        location.setName("Lag");
        location.setUpdatedAt(Date.from(LocalDate.of(2023, 1, 13).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        location.updatedAt();

        Address address1 = new Address();
        address1.setCity("Ilorin");
        address1.setCountry("Nigeria");
        address1.setCreatedAt(Date.from(LocalDate.of(2023, 2, 13).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        address1.setId(2L);
        address1.setLocation(location);
        address1.setState("Kwara");
        address1.setStreet("Taiwo");
        address1.setUpdatedAt(Date.from(LocalDate.of(2023, 1, 13).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        address1.updatedAt();

        Location location1 = new Location();
        location1.setAddress(address1);
        location1.setCreatedAt(Date.from(LocalDate.of(2023, 1, 13).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        location1.setId(2L);
        location1.setLatitude(12.000d);
        location1.setLocationType(LocationType.ORIGIN);
        location1.setLongitude(13.000d);
        location1.setName("Name");
        location1.setUpdatedAt(Date.from(LocalDate.of(2023, 1, 13).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        location1.updatedAt();

        Optional<Location> ofResult = Optional.of(location1);
        doNothing().when(locationRepository).delete(any());
        when(locationRepository.findById(any())).thenReturn(ofResult);
        when(personService.confirmIfUserLoggedIn()).thenReturn(true);
        assertEquals("Location deleted successfully", locationServiceImpl.removeLocation(1L));
        verify(locationRepository).findById(any());
        verify(locationRepository).delete(any());
        verify(personService).confirmIfUserLoggedIn();
    }

    @Test
    void updateLocation_toReturnString() {
        LocationRepository locationRepository = mock(LocationRepository.class);
        when(locationRepository.findById(any())).thenReturn(Optional.empty());

        Address address = new Address();
        address.setCity("Lagos");
        address.setCountry("Nig");
        address.setCreatedAt(Date.from(LocalDate.of(2023, 1, 13).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        address.setId(1L);
        address.setLocation(new Location());
        address.setState("Lagos");
        address.setStreet("Asajon");
        address.setUpdatedAt(Date.from(LocalDate.of(2023, 1, 13).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        address.updatedAt();

        Location location = new Location();
        location.setAddress(address);
        location.setCreatedAt(Date.from(LocalDate.of(2023, 1, 13).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        location.setId(1L);
        location.setLatitude(10.0d);
        location.setLocationType(LocationType.ORIGIN);
        location.setLongitude(10.0d);
        location.setName("Lag");
        location.setUpdatedAt(Date.from(LocalDate.of(2023, 1, 13).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        location.updatedAt();

        Address address1 = new Address();
        address1.setCity("Ilorin");
        address1.setCountry("Nigeria");
        address1.setCreatedAt(Date.from(LocalDate.of(2023, 2, 13).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        address1.setId(2L);
        address1.setLocation(location);
        address1.setState("Kwara");
        address1.setStreet("Taiwo");
        address1.setUpdatedAt(Date.from(LocalDate.of(2023, 1, 13).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        address1.updatedAt();

        Location location1 = new Location();
        location1.setAddress(address1);
        location1.setCreatedAt(Date.from(LocalDate.of(2023, 1, 13).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        location1.setId(2L);
        location1.setLatitude(12.000d);
        location1.setLocationType(LocationType.ORIGIN);
        location1.setLongitude(13.000d);
        location1.setName("Name");
        location1.setUpdatedAt(Date.from(LocalDate.of(2023, 1, 13).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        location1.updatedAt();

        AddressRepository addressRepository = mock(AddressRepository.class);
        when(addressRepository.findByLocationId(any())).thenReturn(Optional.of(address1));
        PersonService personService = mock(PersonService.class);
        when(personService.confirmIfUserLoggedIn()).thenReturn(true);
        LocationServiceImpl locationServiceImpl = new LocationServiceImpl(locationRepository, addressRepository,
                personService);
        assertThrows(ResourceNotFoundException.class, () -> locationServiceImpl
                .updateLocation(new LocationPojo("Lag", "Asajon", "Lagos", "Lagos", "Nig", LocationType.ORIGIN), 1L));
        verify(locationRepository).findById(any());
        verify(personService).confirmIfUserLoggedIn();
    }


    @Test
    void getAllLocations_toReturnJSON() {
        when(locationRepository.findAll()).thenReturn(new ArrayList<>());
        when(personService.confirmIfUserLoggedIn()).thenReturn(true);
        assertThrows(EmptyListException.class, () -> locationServiceImpl.getAllLocations(1, 3, "Sorting Field", true));
        verify(locationRepository).findAll();
        verify(personService).confirmIfUserLoggedIn();
    }


    @Test
    void getOptimalRoute_toReturnJSON() {
        when(locationRepository.findAll()).thenReturn(new ArrayList<>());
        when(personService.confirmIfUserLoggedIn()).thenReturn(true);
        assertThrows(EmptyListException.class, () -> locationServiceImpl.getOptimalRoute("Origin", "Destination"));
        verify(locationRepository).findAll();
        verify(personService).confirmIfUserLoggedIn();
    }
}

