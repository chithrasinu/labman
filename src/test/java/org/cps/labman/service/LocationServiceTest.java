package org.cps.labman.service;

import org.cps.labman.dto.LocationDto;
import org.cps.labman.exception.DataNotFoundException;
import org.cps.labman.persistence.model.Location;
import org.cps.labman.persistence.repo.LocationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.Optional;

class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    private LocationService locationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        locationService = new LocationService(locationRepository);
    }

    @Test
    void testCreate() {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(1L);
        locationDto.setLocation("Location A");

        Location location = new Location();
        location.setId(locationDto.getId());
        location.setLocation(locationDto.getLocation());

        Mockito.when(locationRepository.save(Mockito.any(Location.class))).thenReturn(location);

        locationService.create(locationDto);

        Mockito.verify(locationRepository, Mockito.times(1)).save(Mockito.any(Location.class));
    }

    @Test
    void testEdit() throws DataNotFoundException {
        Long locationId = 1L;

        Location location = new Location();
        location.setId(locationId);
        location.setLocation("Location A");

        Mockito.when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));

        LocationDto locationDto = locationService.edit(locationId);

        Assertions.assertEquals(location.getId(), locationDto.getId());
        Assertions.assertEquals(location.getLocation(), locationDto.getLocation());
    }

    @Test
    void testFindAll() {
        Iterable<Location> locations = Mockito.mock(Iterable.class);
        Mockito.when(locationRepository.findAll()).thenReturn(locations);

        Iterable<Location> result = locationService.findAll();

        Assertions.assertEquals(locations, result);
    }

    @Test
    void testFindLocation() throws DataNotFoundException {
        Long locationId = 1L;

        Location location = new Location();
        location.setId(locationId);
        location.setLocation("Location A");

        Mockito.when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));

        Location result = locationService.findLocation(locationId);

        Assertions.assertEquals(location, result);
    }

    @Test
    void testFindLocationNotFound() {
        Long locationId = 1L;

        Mockito.when(locationRepository.findById(locationId)).thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            locationService.findLocation(locationId);
        });
    }

    @Test
    void testDelete() throws DataNotFoundException {
        Long locationId = 1L;

        Location location = new Location();
        location.setId(locationId);
        location.setLocation("Location A");

        Mockito.when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));

        locationService.delete(locationId);

        Mockito.verify(locationRepository, Mockito.times(1)).deleteById(locationId);
    }

    @Test
    void testDeleteNotFound() {
        Long locationId = 1L;

        Mockito.when(locationRepository.findById(locationId)).thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            locationService.delete(locationId);
        });
    }
}
